package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCMessageBroadcast.MessageBroadcast;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCRegularDataSave;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCMessageBroadcastInArea.MessageBroadcastInArea;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCommonCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCommonCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RPAnno(id="2500")
public class RP_2500_MessageBroadcastInArea extends RPCommand {

	@Resource
	AreaCommonCache areaCommonCache;

	@Resource
	RuleCommonCache ruleCommonCache;


	/**
	 * 暂时只做圆形区域
	 */
	@Override
	public int processor(Packet packet) {
		try {
			MessageBroadcastInArea messageBroadcastInArea = MessageBroadcastInArea.parseFrom(packet.getContent());
			log.error("收到设置区域信息播报请求");
			log.error(messageBroadcastInArea.toString());
			log.info("[DP: RP_2500_MessageBroadcastInArea] 区域信息播报Add操作.");
			boolean result = addCircleArea(packet, messageBroadcastInArea);
			
			//返回1100
			if(result){
				this.commonResponses(1,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);
			}else{
				this.commonResponses(1,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.failure_VALUE);
			}
			//返回1100
//			log.info("[DP->RP] 返回通用应答. 0x1100");
//			Packet _CommonRes_packet = new Packet(true);
//			ServerCommonRes.Builder _CommonRes_packetbuilder = ServerCommonRes.newBuilder();
//			_CommonRes_packetbuilder.setSerialNumber(packet.getSerialNumber());
//			_CommonRes_packetbuilder.setResponseId(AllCommands.Terminal.MessageBroadcastInArea_VALUE);
////			_CommonRes_packetbuilder.setResults(PlatformResponseResult.valueOf(result));
//			_CommonRes_packetbuilder.setResults(PlatformResponseResult.success);
//
//			
//			_CommonRes_packet.setCommand(AllCommands.Platform.ServerCommonRes_VALUE);
//			_CommonRes_packet.setProtocol(LCMessageType.PLATFORM);
//			_CommonRes_packet.setContent(_CommonRes_packetbuilder.build().toByteArray());
//			// 将唯一标识设置为当前节点标识
//			_CommonRes_packet.setSerialNumber(packet.getSerialNumber());
//			_CommonRes_packet.setUniqueMark(packet.getUniqueMark());
//			_CommonRes_packet.setFrom(NodeHelper.getNodeCode());
//			_CommonRes_packet.setTo(packet.getFrom());
//			super.write(_CommonRes_packet);
			
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

//		this.commonResponses(4, packet.getFrom(), packet.getSerialNumber(),
//				packet.getCommand(), PlatformResponseResult.success_VALUE);
		
		return 0;
	}

	/*******************
	 * 添加区域信息播报规则
	 * 
	 * @param packet
	 * @param scaa
	 */
	private boolean addCircleArea(Packet packet, MessageBroadcastInArea messageBroadcastInArea) {
		LCRegularDataSave.RegularDataSave.Builder builder = LCRegularDataSave.RegularDataSave.newBuilder();
		long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
		/*
		 * 服务站区域只有一个。暂时为圆 区域信息： AreaInfo
		 */
		LCAreaInfo.AreaInfo areaInfo = areaInfo(packet, messageBroadcastInArea.getAreaInfo());
		
		long areaIdentify = areaInfo.getAreaIdentify();//区域标识（业务系统提供） 用作通用规则的缓存key
		
		//1、保存区域信息缓存
//		AreaEntity areaEntity = AreaCommonCache.getInstance().addAreaEntity(new AreaEntity(areaInfo));
		AreaEntity areaEntity = areaCommonCache.addAreaEntity(new AreaEntity(areaInfo));

//		if (areaEntity != null) {
//			RuleCommonCache.getInstance()
//					.getRuleEntity(areaIdentify)
//					.removeRuleForArea(areaEntity.getOriginalAreaId(),
//							AreaType.circle);
//		}

		//2、保存新创建的规则到临时缓存中
		List<RegularData> list = ruleInfo(packet, messageBroadcastInArea);
		
		for (RegularData rd : list) {
			RuleEntity ruleEntity = new RuleEntity();
			if(rd!=null){
				ruleEntity.setTerminal(rd.getTerminalId());
				ruleEntity.setAreaIdentify(areaIdentify);
				ruleEntity.setRegularCode(rd.getRegularCode());
				ruleEntity.setType(rd.getType());
				ruleEntity.setMessageBroadcastRule(rd.getMessageBroadcast());
			}
//			RuleCommonCache.getInstance().addRuleEntity(ruleEntity);
			ruleCommonCache.addRuleEntity(ruleEntity);
		}

		builder.addInfos(areaInfo);
		builder.addAllDatas(list);

		if(messageBroadcastInArea.getSaveSign()){//saveSign标示为true时，存储数据库
			// 发送RegularDataSave给DA做存储
			Packet _out_packet = new Packet(true);
			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
			_out_packet.setCommand(AllCommands.DataAccess.RegularDataSave_VALUE);
			_out_packet.setContent(builder.build().toByteArray());
			super.writeToDataAccess(_out_packet);

		}
		
		return true;
	}
	
	/********************
	 * 
	 * @param packet
	 * @param area
	 * @return
	 */
	private LCAreaInfo.AreaInfo areaInfo(Packet packet, LCAreaInfo.AreaInfo areaInfo) {
		LCAreaInfo.AreaInfo.Builder areaBuilder = LCAreaInfo.AreaInfo.newBuilder();
		areaBuilder.setAreaIdentify(areaInfo.getAreaIdentify());
		areaBuilder.setTypes(LCAreaType.AreaType.circle);
		areaBuilder.setCreateDate(System.currentTimeMillis() / 1000);
		LCAreaData.AreaData.Builder dataBuilder = LCAreaData.AreaData.newBuilder();
		dataBuilder.setDataSN(areaInfo.getDatas(0).getDataSN());
		dataBuilder.setLatitude(areaInfo.getDatas(0).getLatitude());
		dataBuilder.setLongitude(areaInfo.getDatas(0).getLongitude());
		dataBuilder.setRadiusLength(areaInfo.getDatas(0).getRadiusLength());
		areaBuilder.addDatas(dataBuilder.build());
		return areaBuilder.build();
	}

	/********************
	 * 创建区域对应的规则. .  区域信息播报
	 * messageBroadcast
	 * 
	 * @param packet
	 * @param area
	 * @return
	 */
	private List<LCRegularData.RegularData> ruleInfo(Packet packet, MessageBroadcastInArea messageBroadcastInArea) {
		List<LCRegularData.RegularData> list = new ArrayList<LCRegularData.RegularData>();
		// int areaProperty = area.getAreaProperty();
//		long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());

		// 服务站信息推送
		LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
		regularBuilder.setRegularCode(LCRegularCode.RegularCode.messageBroadcast);
		regularBuilder.setType(RegularType.common);//通用规则 
		regularBuilder.setLastModifyDate(System.currentTimeMillis() / 1000);
		
		
		MessageBroadcast.Builder messageBroadcastBuild = MessageBroadcast.newBuilder();
		messageBroadcastBuild.setAreaId(messageBroadcastInArea.getAreaInfo().getAreaIdentify());
		messageBroadcastBuild.setBroadcastContent(messageBroadcastInArea.getBroadcastContent());
		messageBroadcastBuild.setSigns(messageBroadcastInArea.getSigns());
		
		regularBuilder.setMessageBroadcast(messageBroadcastBuild.build());
		list.add(regularBuilder.build());

		return list;
	}
}
