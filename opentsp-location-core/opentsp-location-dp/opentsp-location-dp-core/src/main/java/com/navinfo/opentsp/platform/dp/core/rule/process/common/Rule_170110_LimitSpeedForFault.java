package com.navinfo.opentsp.platform.dp.core.rule.process.common;

import com.navinfo.opentsp.platform.dp.core.rule.handler.common.CommonRegularProcess;
import com.navinfo.opentsp.platform.dp.core.rule.handler.common.CommonRuleAnno;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.dp.core.cache.DictCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCommonCache;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsVehicleBreakdown;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign;
import com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDictType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCMessageBroadcastSave;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCDispatchMessage;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.DictEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.rule.status.common.Rule_170110_Status;
import com.navinfo.opentsp.platform.dp.core.rule.status.common.Rule_170110_Status.Rule_170110_StatusEntry;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * 根据故障码限速，暂时屏蔽——hk
 * 规则编码：limitSpeedForFault	170110	故障限速（规则内容来自字典）  
 *
 */
@CommonRuleAnno(ruleId = RegularCode.limitSpeedForFault_VALUE)
public class Rule_170110_LimitSpeedForFault extends CommonRegularProcess {

	@Resource
	private RuleCommonCache ruleCommonCache;

	@Resource
	private DictCache dictCache;

	@Resource
	private Rule_170110_Status rule_170110_Status;

	@Override
	public GpsLocationDataEntity process(double distance, AreaEntity areaEntity, GpsLocationDataEntity dataEntity) {
		final List<RuleEntity> ruleCommonEntityList = ruleCommonCache.getRuleEntity(RegularCode.limitSpeedForFault_VALUE);
		if(ruleCommonEntityList == null){
			return dataEntity;
		}
		for(RuleEntity ruleEntity:ruleCommonEntityList){
			List<OutRegionToLSpeed> rules = ruleEntity.getOutregionToLSpeed();
			for(OutRegionToLSpeed rule: rules){
				if(rule == null)
					return dataEntity;
				//判断是否有故障附加信息
				if(dataEntity.getGpsVehicleBreakdownAddition().getBreakdowns().size() <= 0){
					return dataEntity;
				}
				Map<Integer, DictEntity> dictMap = dictCache.getDicts(LCDictType.DictType.breakdownCode);
				String newMd5 = "";
				// TODO: 16/8/24 更改MD5方式
				//String newMd5 = MD5.encrypt(dataEntity.getGpsVehicleBreakdownAddition().getBreakdowns().toString(), 16);
				Rule_170110_StatusEntry entry = rule_170110_Status.getRule_170110_StatusEntry(dataEntity.getTerminalId());
				if(null != newMd5 && newMd5.equals(entry.getBreakdownCodeMd5())){
					//本次上传的故障码和上次的一样，不做处理
				}else{
					//取出本次上传的故障码对应的数据库字典实体
					List<DictEntity> breakdownEntities = new ArrayList<DictEntity>();
					if(dictMap.size() > 0) {
						Set<Integer> keys = dictMap.keySet();
						for(Integer keyId : keys) {
							DictEntity dict = dictMap.get(keyId);
							int dictCode = dict.getDataCode();
							for(GpsVehicleBreakdown gps : dataEntity.getGpsVehicleBreakdownAddition().getBreakdowns()){
//								if(dictCode == gps.getValue()){
//									breakdownEntities.add(dict);
//								}
							}
						}
					}
					String breakNames = "";
					int minValue = Integer.MAX_VALUE;
					//取出本次上传的所有故障名称和最小限速值进行播报
					for(DictEntity dic : breakdownEntities){
						breakNames += dic.getName()+",";
						int currentValue = Integer.parseInt(dic.getDictValue());
						if(minValue > currentValue){
							minValue = currentValue;
						}
					}
					//第一次上传故障码或者故障码不同，发送播报通知
					String content = "您的车辆发生"+breakNames+"故障，限速为："+minValue+"千米每小时";
					//给终端播报
					sendDispatchMessage(content,dataEntity);
					//存储播报日志到DA
					saveToDA(content,dataEntity);
					//保存进缓存状态
					entry.setTerminalID(dataEntity.getTerminalId());
					entry.setBreakdownCodeMd5(newMd5);
					rule_170110_Status.addBreakdownStatus(entry);
				}
			}
		}
		return dataEntity;
	}

	
	/***************************
	 * 发送终端播报
	 * 
	 * @param dataEntity
	 * @param
	 */
	private void sendDispatchMessage(String content, GpsLocationDataEntity dataEntity){
		LCDispatchMessage.DispatchMessage.Builder  dispatchMessBuilder = LCDispatchMessage.DispatchMessage.newBuilder();
		dispatchMessBuilder.setMessageContent(content);
		MessageSign.Builder msBuilder = LCMessageSign.MessageSign.newBuilder();
		msBuilder.setIsBroadcast(true);
		dispatchMessBuilder.setSigns(msBuilder.build());
		
		Packet _packet = new Packet(true);
		_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_packet.setTo(dataEntity.getTerminalId());
		_packet.setUniqueMark(dataEntity.getTerminalId()+"");
		_packet.setCommand(AllCommands.Terminal.DispatchMessage_VALUE);
		_packet.setContent(dispatchMessBuilder.build().toByteArray());
		//// TODO: 16/8/24
//		RPCommand rpc= new RP_2151_DispatchMessage();
//		rpc.processor(_packet);
		super.writeToTermianl.processor(_packet);
	}
	
	private void saveToDA(String content, GpsLocationDataEntity dataEntity){
		LCMessageBroadcastSave.MessageBroadcastSave.Builder messcastBuilder = LCMessageBroadcastSave.MessageBroadcastSave.newBuilder();
		messcastBuilder.setTerminalId(dataEntity.getTerminalId());
		messcastBuilder.setBroadcastDate(System.currentTimeMillis()/1000);
		messcastBuilder.setBroadcastContent(content);
		MessageSign.Builder msBuilder = LCMessageSign.MessageSign.newBuilder();
		msBuilder.setIsBroadcast(true);
		messcastBuilder.setSigns(msBuilder.build());
		
		Packet _packet = new Packet(true);
		_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_packet.setTo(dataEntity.getTerminalId());
		_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_packet.setCommand(AllCommands.DataAccess.MessageBroadcastSave_VALUE);
		_packet.setContent(messcastBuilder.build().toByteArray());
		super.writeToDataAccess(_packet);
	}

}
