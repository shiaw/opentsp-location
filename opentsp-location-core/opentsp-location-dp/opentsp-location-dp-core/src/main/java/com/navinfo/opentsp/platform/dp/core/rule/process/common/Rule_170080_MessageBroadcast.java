package com.navinfo.opentsp.platform.dp.core.rule.process.common;

import com.navinfo.opentsp.platform.dp.core.rule.handler.common.CommonRegularProcess;
import com.navinfo.opentsp.platform.dp.core.rule.handler.common.CommonRuleAnno;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCommonCache;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.rule.status.common.Rule_170080_Status;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCBroadcastInfoNotice.BroadcastInfoNotice;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCMessageBroadcast.MessageBroadcast;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCMessageBroadcastSave;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCServerStationComment.ServerStationComment;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTerminalMessageSwitch.TerminalMessageSwitch;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCDispatchMessage;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.List;

/**
 * 青汽平台暂不用此规则--黄修伟--20170821添加此注释；后续如用到，再放开
 * 相关TTS播报的规则暂时不用，屏蔽——hk，注释掉112行
 * 规则编码：messageBroadcast	170080	区域信息播报  消息ID：0x2500
 * 其中使用到其它规则：terminalBroadcastSwitch	170130	终端信息广播开关    消息ID：0x2520
 *              	  serverStationComment	170140	服务站点评通知（通用规则，不需要数据支撑） 消息ID:0x2522
 */
@CommonRuleAnno(ruleId = RegularCode.messageBroadcast_VALUE)
public class Rule_170080_MessageBroadcast extends CommonRegularProcess {

	@Resource
	private RuleCache ruleCache;

	@Resource
	private RuleCommonCache ruleCommonCache;

	@Resource
	private Rule_170080_Status rule_170080_Status;

	@Value("${rule.170080.process:false}")
	private boolean isTerminalMessage;

	@Override
	public GpsLocationDataEntity process(double distance , final AreaEntity areaEntity, final GpsLocationDataEntity dataEntity)  {
		//个性规则  获取终端信息广播开关
		//Boolean isTerminalMessage = false;// 获取终端信息广播开关,默认关闭
		if(isTerminalMessage) {
			RuleEntity terminalMessageRuleEntity = ruleCache.getRuleEntity(dataEntity.getTerminalId());
			if(terminalMessageRuleEntity!=null){
				TerminalMessageSwitch	terminalMessage = terminalMessageRuleEntity.getTerminalMessage();
				if(terminalMessage!=null){
					isTerminalMessage=terminalMessage.getMessageSwitch();
				}
			}

			//通用规则
			final List<RuleEntity> ruleCommonEntityList = ruleCommonCache.getRuleEntity(RegularCode.messageBroadcast_VALUE);
			if(ruleCommonEntityList==null){
				return dataEntity;
			}
			for(RuleEntity ruleEntity:ruleCommonEntityList){
				List<MessageBroadcast> rules;
				try {
					rules = ruleEntity.getMessageBroadcast();
				} catch (Exception e) {
					log.error(dataEntity.getTerminalId()+"_"+ruleEntity.getRegularCode()+"获取getMessageBroadcast错误!",e);
					return dataEntity;
				}
				if(rules != null && rules.size() > 0) {
					for(MessageBroadcast rule : rules){
						if(rule.getAreaId()==areaEntity.getAreaId()){//取到区域对应的规则数据
							String statusStr = dataEntity.getTerminalId()+"_" + rule.getAreaId()+"";//终端id+区域id
							Boolean messageBroadCache = rule_170080_Status.getmessageBroadCacheById(statusStr);
							//判断当前位置是否在推送半径内, 比较距离和服务站半径的大小
							if(distance<=areaEntity.getDatas().get(0).getRadiusLength()){
								//此时在服务站内，需要判断缓存保存的上一个状态
								if(rule_170080_Status.IscontainsKey(statusStr)){
									if(messageBroadCache==null){
										//上次保存的记录是null  认为是异常    增加缓存,在服务站内状态
										rule_170080_Status.addmessageBroadCache(statusStr, true);
									}else{
										if(messageBroadCache){
											//上次保存为true：车一直在服务站内部，暂时不做处理
										}else{
											//上次保存为false：第一次进入此区域，①、发送终端提示；②、添加车在此服务站内部的缓存,③、DA存储
											//1、发送终端TTS播报
											if(isTerminalMessage){
												//sendDispatchMessage(rule,areaEntity,dataEntity);
											}
											//2、广播信息通知
											//broadcastInfoNotice(areaEntity.getAreaId(),rule.getBroadcastContent(),dataEntity);

											//2、添加车在此服务站内部的缓存
											rule_170080_Status.addmessageBroadCache(statusStr, true);
											rule_170080_Status.addArrivingDateCache(statusStr, dataEntity.getGpsDate());
											//3、DA
											saveToDA(rule,areaEntity,dataEntity);
										}
									}
								}else{
									//不存在此车缓存状态，认为是首次上线，此时处理：②、添加车在此服务站内部的缓存

									//2、添加车在此服务站内部的缓存
									rule_170080_Status.addmessageBroadCache(statusStr, true);
								}
							}else{//此时在服务站外部，需要判断缓存保存的上一个状态

								if(!rule_170080_Status.IscontainsKey(statusStr)){
									//不存在此车缓存状态，认为是首次上线，此时处理：①、添加车在此服务站内部的缓存 状态为false
									//1、添加车在此服务站内部的缓存
									rule_170080_Status.addmessageBroadCache(statusStr, false);
								}else{
									//存在上次记录的缓存，需要判断缓存状态
									if(messageBroadCache){
										//上次保存为true：说明上次在此服务站内部， 这次处理：1、修改缓存转态 为 false，车已经出服务站,2、发送服务站点评通知
										rule_170080_Status.addmessageBroadCache(statusStr, false);
										if(rule_170080_Status.getArrivingDateCacheById(statusStr)!=null){
											//serverStationComment(rule_170080_Status.getArrivingDateCacheById(statusStr),areaEntity.getAreaId(),dataEntity);
										}
									}else{
										//上次保存为false：说明车一直在外部，暂时不做处理
									}
								}
							}
						}
					}
				}

			}

		}
		return dataEntity;
	}
	
	/***************************
	 * 发送终端播报
	 * 
	 * @param rule
	 * @param areaEntity
	 * @param dataEntity
	 */
	private void sendDispatchMessage(MessageBroadcast rule, AreaEntity areaEntity,GpsLocationDataEntity dataEntity){
		LCDispatchMessage.DispatchMessage.Builder  dispatchMessBuilder = LCDispatchMessage.DispatchMessage.newBuilder();
		dispatchMessBuilder.setMessageContent(rule.getBroadcastContent());
		dispatchMessBuilder.setSigns(rule.getSigns());//
		
		Packet _packet = new Packet(true);
		_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_packet.setTo(dataEntity.getTerminalId());
		_packet.setFrom(NodeHelper.getNodeCode());
		_packet.setUniqueMark(dataEntity.getUniqueMark());
		_packet.setCommand(AllCommands.Terminal.DispatchMessage_VALUE);
		_packet.setContent(dispatchMessBuilder.build().toByteArray());
		//// TODO: 16/8/24  
//		RPCommand rpc= new RP_2151_DispatchMessage();
		writeToTermianl.processor(_packet);
		log.error("终端信息播报： "+dataEntity.getUniqueMark()+" , "+areaEntity.getAreaId()+" , "+rule.getBroadcastContent());
	}
	
	/**
	 * Da 存储
	 * @param rule
	 * @param areaEntity
	 * @param dataEntity
	 */
	private void saveToDA(MessageBroadcast rule, AreaEntity areaEntity, GpsLocationDataEntity dataEntity){
		LCMessageBroadcastSave.MessageBroadcastSave.Builder messcastBuilder = LCMessageBroadcastSave.MessageBroadcastSave.newBuilder();
		messcastBuilder.setTerminalId(dataEntity.getTerminalId());
		messcastBuilder.setAreaIdentify(areaEntity.getAreaId());
		messcastBuilder.setBroadcastDate(System.currentTimeMillis()/1000);
		messcastBuilder.setBroadcastContent(rule.getBroadcastContent());
		messcastBuilder.setSigns(rule.getSigns());
		
		Packet _packet = new Packet(true);
		_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_packet.setTo(dataEntity.getTerminalId());
		_packet.setFrom(NodeHelper.getNodeCode());
		_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_packet.setCommand(AllCommands.DataAccess.MessageBroadcastSave_VALUE);
		_packet.setContent(messcastBuilder.build().toByteArray());
		super.writeToDataAccess(_packet);
	}
	
	
	/**
	 * 消息ID：0x2521
	 * 消息回复平台通用应答（0x1100），通知上层业务系统广播信息，推送给关联终端的APP应用。

	 * @param areaIdentify
	 * @param broadcastContent
	 * @param dataEntity
	 */
	public void broadcastInfoNotice(long areaIdentify, String broadcastContent, GpsLocationDataEntity dataEntity){
		BroadcastInfoNotice.Builder broadcastInfoNoticeBuilder = BroadcastInfoNotice.newBuilder();
		broadcastInfoNoticeBuilder.setAreaIdentify(areaIdentify);
		broadcastInfoNoticeBuilder.setBroadcastContent(broadcastContent);
		
		Packet _packet = new Packet(true);
		_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_packet.setTo(dataEntity.getTerminalId());
		_packet.setFrom(NodeHelper.getNodeCode());
		_packet.setUniqueMark(dataEntity.getUniqueMark());
		_packet.setCommand(AllCommands.Terminal.BroadcastInfoNotice_VALUE);
		_packet.setContent(broadcastInfoNoticeBuilder.build().toByteArray());
		//采用一致性hash向RP节点发送数据
		//RPClusters.execute(_packet);
		sendKafkaHandler.writeKafKaToRP(_packet);
		log.error("通知上层业务系统广播信息(2521) : "+dataEntity.getUniqueMark()+" , "+areaIdentify+" , "+broadcastContent);
	}
	
	/**
	 * 消息ID：0x2522
	 * 消息回复平台通用应答（0x1100），上层应用系统推送终端关联的APP应用。
	 * @param arrivingDate
	 * @param dataEntity
	 */
	public void serverStationComment(long arrivingDate,long areaIdentify,GpsLocationDataEntity dataEntity){
		ServerStationComment.Builder serverStationCommentBuilder = ServerStationComment.newBuilder();
		serverStationCommentBuilder.setArrivingDate(arrivingDate);
		serverStationCommentBuilder.setAreaId(areaIdentify);
		
		Packet _packet = new Packet(true);
		_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_packet.setTo(dataEntity.getTerminalId());
		_packet.setFrom(NodeHelper.getNodeCode());
		_packet.setUniqueMark(dataEntity.getUniqueMark());
		_packet.setCommand(AllCommands.Terminal.ServerStationComment_VALUE);
		_packet.setContent(serverStationCommentBuilder.build().toByteArray());
		//采用一致性hash向RP节点发送数据
		//RPClusters.execute(_packet);
		sendKafkaHandler.writeKafKaToRP(_packet);
		log.error("向上层应用系统推送终端关联的APP应用(2522) : "+dataEntity.getUniqueMark()+" , "+areaIdentify+" , "+arrivingDate);
	}
}
