package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCDeleteRegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCRegularDataSave.RegularDataSave;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCAreaStatusNotifySwitch.AreaStatusNotifySwitch;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;

@RPAnno(id="2527")
public class RP_2527_AreaStatusNotifySwitch extends RPCommand {

	@Autowired
	private RuleCache ruleCache;

	@Override
	public int processor(Packet packet) {
		try {
			AreaStatusNotifySwitch areaStatusNotifySwitch = AreaStatusNotifySwitch.parseFrom(packet.getContent());
			long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
			Boolean notifySwitch = false; //终端区域状态通知开关  true：通知；false不通知，默认false
			if(areaStatusNotifySwitch.getSwitch()){
				notifySwitch = true;
			}
			//1、修改终端规则数据RuleCache缓存状态
			RuleEntity ruleEntity = ruleCache.getRuleEntity(terminalId);
			if(ruleEntity == null){
				ruleEntity = new RuleEntity();
				ruleEntity.setTerminal(terminalId);
			}
			ruleEntity.setAreaStatusNotifySwitch(notifySwitch);
			ruleCache.addRuleEntity(ruleEntity);
			
			Boolean result = false;
			if(notifySwitch){
				//2、转发DA存储规则   
				result = saveToDA(packet);
			}else{
				//删除规则
				result = deleteToDA(packet);
			}
			//返回3001
			if(result){
				this.commonResponses(4,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);
			}else{
				this.commonResponses(4,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.failure_VALUE);
			}
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	public Boolean saveToDA(Packet packet){
		Boolean result =true;
		RegularData.Builder regularDataBuilder = RegularData.newBuilder();
		regularDataBuilder.setTerminalId(Convert.uniqueMarkToLong(packet.getUniqueMark()));
		regularDataBuilder.setRegularCode(RegularCode.inOrOutAreaNotifySwitchPara);
		regularDataBuilder.setLastModifyDate(System.currentTimeMillis()/1000);
		regularDataBuilder.setType(RegularType.individual);
		RegularDataSave.Builder regularDataSaveBuilder = RegularDataSave.newBuilder();
		regularDataSaveBuilder.addDatas(regularDataBuilder.build());
		
		Packet _packet = new Packet(true);
		_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_packet.setTo(Convert.uniqueMarkToLong(packet.getUniqueMark()));
		_packet.setFrom(NodeHelper.getNodeCode());
		_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_packet.setCommand(AllCommands.DataAccess.RegularDataSave_VALUE);
		_packet.setContent(regularDataSaveBuilder.build().toByteArray());
		try {
			super.writeToDataAccess(_packet);
		}catch (Exception e) {
			result = false;
		}
		return result;
	}
	public Boolean deleteToDA(Packet packet){
		Boolean result =false;
		long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
		List<Long> values = new ArrayList<Long>();
		//构建DA数据
		LCDeleteRegularData.DeleteRegularData.Builder regularDelBuilder = LCDeleteRegularData.DeleteRegularData.newBuilder();
		regularDelBuilder.setTerminalId(terminalId);
		regularDelBuilder.setRegularCode(LCRegularCode.RegularCode.inOrOutAreaNotifySwitchPara);
		regularDelBuilder.addAllRegularIdentify(values);

		Packet _out_packet = new Packet(true);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setCommand(AllCommands.DataAccess.DeleteRegularData_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setContent(regularDelBuilder.build().toByteArray());
		try {
			super.writeToDataAccess(_out_packet);
		}catch (Exception e) {
			result = false;
		}
		return result;
	}
}
