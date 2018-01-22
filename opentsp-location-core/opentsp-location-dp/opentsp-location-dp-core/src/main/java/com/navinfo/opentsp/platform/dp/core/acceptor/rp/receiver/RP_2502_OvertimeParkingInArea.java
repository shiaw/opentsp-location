package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOverTimePark.OverTimePark;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCRegularDataSave;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCOvertimeParkingInArea;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCOvertimeParkingInArea.OvertimeParkingInArea;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCommonCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCommonCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RPAnno(id="2502")
public class RP_2502_OvertimeParkingInArea extends RPCommand {


	@Resource
	AreaCommonCache areaCommonCache;

	@Resource
	RuleCommonCache ruleCommonCache;

	@Override
	public int processor(Packet packet) {
		try {
			LCOvertimeParkingInArea.OvertimeParkingInArea overtimeParkingInArea = LCOvertimeParkingInArea.OvertimeParkingInArea.parseFrom(packet.getContent());
			log.error("收到OvertimeParkingInArea请求");
			log.error(overtimeParkingInArea.toString());
			boolean result = addCircleArea(packet, overtimeParkingInArea);
			
			//返回1100
			if(result){
				this.commonResponses(1,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);
			}else{
				this.commonResponses(1,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.failure_VALUE);
			}
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return 0;
	}
	
	
	/***********************
	 * 添加圆形区域规则
	 * 
	 * @param 
	 * @param overtimeParkingInArea 中保存 Area, parkingTime 
	 */
	private boolean addCircleArea(Packet packet, LCOvertimeParkingInArea.OvertimeParkingInArea overtimeParkingInArea) {
		LCRegularDataSave.RegularDataSave.Builder builder = LCRegularDataSave.RegularDataSave.newBuilder();
//		long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
		LCAreaInfo.AreaInfo areaInfo = areaInfo(packet, overtimeParkingInArea.getAreaInfo());
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
		List<RegularData> list = ruleInfo(packet, overtimeParkingInArea);
		for (RegularData rd : list) {
			RuleEntity ruleEntity = new RuleEntity();
			if(rd!=null){
				ruleEntity.setTerminal(rd.getTerminalId());
				ruleEntity.setAreaIdentify(areaIdentify);
				ruleEntity.setRegularCode(rd.getRegularCode());
				ruleEntity.setType(rd.getType());
				//ruleEntity.setMessageBroadcastRule(rd.getMessageBroadcast());
				ruleEntity.setOverTimeParkRule(rd.getOvertimePark());
			}
//			RuleCommonCache.getInstance().addRuleEntity(ruleEntity);
			ruleCommonCache.addRuleEntity(ruleEntity);
		}

		builder.addInfos(areaInfo);
		builder.addAllDatas(list);

		if(overtimeParkingInArea.getSaveSign()){//saveSign标示为true时，存储数据库
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
		dataBuilder.setDataSN(0);
		dataBuilder.setLatitude(areaInfo.getDatas(0).getLatitude());
		dataBuilder.setLongitude(areaInfo.getDatas(0).getLongitude());
		dataBuilder.setRadiusLength(areaInfo.getDatas(0).getRadiusLength());
		areaBuilder.addDatas(dataBuilder.build());
		return areaBuilder.build();
	}

	/********************
	 * 创建区域对应的规则. .  
	 * messageBroadcast
	 * 
	 * @param packet
	 * @param area
	 * @return
	 */
	private List<LCRegularData.RegularData> ruleInfo(Packet packet, OvertimeParkingInArea overtimeParkingInArea) {
		List<LCRegularData.RegularData> list = new ArrayList<LCRegularData.RegularData>();

		// 
		LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
		regularBuilder.setRegularCode(LCRegularCode.RegularCode.overtimePark);
		regularBuilder.setType(RegularType.common);//通用规则
		regularBuilder.setLastModifyDate(System.currentTimeMillis() / 1000);
		
		
		OverTimePark.Builder overparkBuild = OverTimePark.newBuilder();
		overparkBuild.setAreaId(overtimeParkingInArea.getAreaInfo().getAreaIdentify());
		overparkBuild.setOvertimeLimit(overtimeParkingInArea.getParkingTime());
		overparkBuild.setOvertimeNotifyLimit(overtimeParkingInArea.getParkingTimeForNotify());
		regularBuilder.setOvertimePark(overparkBuild.build());
		list.add(regularBuilder.build());

		return list;
	}
}
