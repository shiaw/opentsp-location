package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCommonCache;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTerminalBroadcastSwitch.TerminalBroadcastSwitch;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTerminalMessageSwitch.TerminalMessageSwitch;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCRegularDataSave.RegularDataSave;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;

import javax.annotation.Resource;

/**
 * 终端信息广播开关
 * 	 1、修改终端规则数据RuleCache缓存状态 ，
 *   2、转发DA存储   
 *   3、回复寰游
 * 
 * @author Administrator
 *
 */

@RPAnno(id="2520")
public class RP_2520_TerminalBroadcastSwitch extends RPCommand {

	@Resource
	RuleCache ruleCache;

	@Override
	public int processor(Packet packet) {
		try {
			TerminalBroadcastSwitch terminalBroadcastSwitch = TerminalBroadcastSwitch.parseFrom(packet.getContent());
			long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
			Boolean messageSwitch=false;//信息广播开关  true：开启；false关闭 ,默认
			if(terminalBroadcastSwitch.getBroadcastSwitch()){
				messageSwitch=true;
			}
			
			//1、修改终端规则数据RuleCache缓存状态
//			RuleEntity ruleEntity = RuleCache.getInstance().getRuleEntity(terminalId);
			RuleEntity ruleEntity = ruleCache.getRuleEntity(terminalId);
			if(ruleEntity == null){
				ruleEntity = new RuleEntity();
				ruleEntity.setTerminal(terminalId);
//				RuleCache.getInstance().addRuleEntity(ruleEntity);
				ruleCache.addRuleEntity(ruleEntity);
			}
			
			TerminalMessageSwitch.Builder terminalMessageBuilder = TerminalMessageSwitch.newBuilder();
			terminalMessageBuilder.setMessageSwitch(messageSwitch);
			ruleEntity.setTerminalMessage(terminalMessageBuilder.build());
			
//			RuleCache.getInstance().addRuleEntity(ruleEntity);
			ruleCache.addRuleEntity(ruleEntity);

			//2、转发DA存储   
			Boolean re = saveToDA( packet,terminalMessageBuilder);
			
			//返回3001
			if(re){
				this.commonResponses(4,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);
			}else{
				this.commonResponses(4,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.failure_VALUE);
			}
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return 0;
	}
	
	
	private Boolean saveToDA(Packet packet , TerminalMessageSwitch.Builder terminalMessageBuilder){
		Boolean re =true;
		RegularData.Builder regularDataBuilder = RegularData.newBuilder();
		regularDataBuilder.setTerminalId(Convert.uniqueMarkToLong(packet.getUniqueMark()));
		regularDataBuilder.setRegularCode(RegularCode.terminalBroadcastSwitch);
		regularDataBuilder.setLastModifyDate(System.currentTimeMillis()/1000);
		regularDataBuilder.setType(RegularType.individual);
		regularDataBuilder.setTerminalMessage(terminalMessageBuilder.build());
		
		RegularDataSave.Builder regularDataSaveBuilder = RegularDataSave.newBuilder();
		regularDataSaveBuilder.addDatas(regularDataBuilder.build());
		
		Packet _packet = new Packet(true);
		_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_packet.setTo(Convert.uniqueMarkToLong(packet.getUniqueMark()));
		_packet.setFrom(NodeHelper.getNodeCode());
//		_packet.setUniqueMark(packet.getUniqueMark());
		_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_packet.setCommand(AllCommands.DataAccess.RegularDataSave_VALUE);
		_packet.setContent(regularDataSaveBuilder.build().toByteArray());
		try {
			super.writeToDataAccess(_packet);
		}catch (Exception e) {
			re = false;
		}
		return re;
	}
	
}
