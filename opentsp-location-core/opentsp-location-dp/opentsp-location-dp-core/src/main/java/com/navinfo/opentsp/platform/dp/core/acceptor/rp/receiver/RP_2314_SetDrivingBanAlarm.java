package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDrivingBan.DrivingBan;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCRegularDataSave;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCDrivingBanSetting.DrivingBanSetting;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;

import javax.annotation.Resource;


/******************
 * 设置禁驾协议
 * 
 * @author claus
 *
 */

@RPAnno(id="2314")
public class RP_2314_SetDrivingBanAlarm extends RPCommand {


	@Resource
	RuleCache ruleCache;

	@Override
	public int processor(Packet packet) {
		
		log.info("[DP: DrivingBanAlarm] 禁驾规则 Add操作. 保存对应的禁驾规则到缓存RuleCache.");
		//添加规则到缓存及数据库中 
		addRule(packet);
		
		//返回RP节点，通用应答成功
		super.commonResponses(4, packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);
		
		return 0;
	}
	
	
	/******************************
	 * 添加路线区域对象和规则
	 * 
	 * @param packet
	 * @param sra
	 */
	private void addRule(Packet packet) {
		
		try {
			LCRegularDataSave.RegularDataSave.Builder builder = LCRegularDataSave.RegularDataSave.newBuilder();
			long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
			
			//
//			RuleEntity rule = RuleCache.getInstance().getRuleEntity(terminalId);
			RuleEntity rule = ruleCache.getRuleEntity(terminalId);
	    	if(rule == null){
	    		rule = new RuleEntity();
	    		rule.setTerminal(terminalId);
	    	}
	    	
			DrivingBanSetting driveFrobiden = DrivingBanSetting.parseFrom(packet.getContent());
			RegularData.Builder regularData = RegularData.newBuilder();
			
			//转化禁驾规则
	    	DrivingBan.Builder driBan = DrivingBan.newBuilder();
	    	driBan.setBanIdentify(driveFrobiden.getBanIdentify());
	    	driBan.setStartDate(driveFrobiden.getBeginDate());
	    	driBan.setEndDate(driveFrobiden.getEndDate());
	    	driBan.setIsEveryDay(driveFrobiden.getIsEveryDay());
	    	rule.setDrivingBanRule(driBan.build());
	    	
	    	//创建DA存储规则对象
	    	regularData.setTerminalId(terminalId);
	    	regularData.setType(RegularType.individual);//是否为通用规则
	    	regularData.setRegularCode( LCRegularCode.RegularCode.drivingBan );
	    	regularData.setLastModifyDate( System.currentTimeMillis() / 1000 );
	    	regularData.setDrivingBan(driBan);
	    	builder.addDatas(regularData);
	    	
	    	//缓存当前禁驾协议规则
//	    	RuleCache.getInstance().addRuleEntity(rule);
	    	ruleCache.addRuleEntity(rule);

	    	//发送RegularDataSave给DA做存储
			log.info("[DP: SetDringBanAlarm] 禁驾规则  ADD 操作. 转发给DA做添加区域信息和规则操作[0x0933]. ");
			Packet _out_packet = new Packet(true);
			_out_packet.setCommand(AllCommands.DataAccess.RegularDataSave_VALUE);
			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
			_out_packet.setContent(builder.build().toByteArray());
			super.writeToDataAccess(_out_packet);
			
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
