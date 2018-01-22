package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCRegularDataSave;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCOutRegionToLimitSpeed.OutRegionToLimitSpeed;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RPAnno(id="2317")
public class RP_2317_OutRegionToLimitSpeed extends RPCommand {

	@Resource
	AreaCache areaCache;

	@Resource
	RuleCache ruleCache;

	@Override
	public int processor(Packet packet) {
		try {
			OutRegionToLimitSpeed outRegionToLimitSpeed = OutRegionToLimitSpeed.parseFrom(packet.getContent());
			log.error("收到2317_OutRegionToLimitSpeed");
			log.error(outRegionToLimitSpeed.toString());
			Boolean res = regularDataSave(packet, outRegionToLimitSpeed);
			if (res) {
				this.commonResponses(4, packet.getFrom(),packet.getSerialNumber(), packet.getCommand(),PlatformResponseResult.success_VALUE);
			} else {
				this.commonResponses(4, packet.getFrom(),packet.getSerialNumber(), packet.getCommand(),PlatformResponseResult.failure_VALUE);
			}
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		this.commonResponses(4, packet.getFrom(), packet.getSerialNumber(),packet.getCommand(), PlatformResponseResult.success_VALUE);
		return 0;
	}

	/***********************
	 * @param packet
	 * @param scaa
	 */
	private Boolean regularDataSave(Packet packet, OutRegionToLimitSpeed outRegionToLimitSpeed) {
		LCRegularDataSave.RegularDataSave.Builder builder = LCRegularDataSave.RegularDataSave.newBuilder();
		// 1、区域数据保存缓存
		List<LCAreaInfo.AreaInfo> areaInfoList = outRegionToLimitSpeed.getAreaInfoList();
		for (LCAreaInfo.AreaInfo areaInfo : areaInfoList) {
//			RuleEntity rule = RuleCache.getInstance().getRuleEntity(areaInfo.getTerminalId());
			RuleEntity rule = ruleCache.getRuleEntity(areaInfo.getTerminalId());
			if (rule == null) {
				rule = new RuleEntity();
				rule.setTerminal(areaInfo.getTerminalId());
				OutRegionToLSpeed.Builder outRegionToLSpeed = OutRegionToLSpeed.newBuilder();
				outRegionToLSpeed.setAreaId(areaInfo.getAreaIdentify());
				outRegionToLSpeed.setControlType(outRegionToLimitSpeed.getControlType());
				outRegionToLSpeed.setLimitSpeed(outRegionToLimitSpeed.getLimitSpeed());
				outRegionToLSpeed.setGpsId(outRegionToLimitSpeed.getGpsId());
				outRegionToLSpeed.setBroadcastContent(outRegionToLimitSpeed.getBroadcastContent());
				outRegionToLSpeed.setSigns(outRegionToLimitSpeed.getSigns());
				rule.addOutRegionToLSpeed(outRegionToLSpeed.build());
//				RuleCache.getInstance().addRuleEntity(rule);
				ruleCache.addRuleEntity(rule);
			} else {
				OutRegionToLSpeed.Builder outRegionToLSpeed = OutRegionToLSpeed.newBuilder();
				outRegionToLSpeed.setAreaId(areaInfo.getAreaIdentify());
				outRegionToLSpeed.setControlType(outRegionToLimitSpeed.getControlType());
				outRegionToLSpeed.setLimitSpeed(outRegionToLimitSpeed.getLimitSpeed());
				outRegionToLSpeed.setGpsId(outRegionToLimitSpeed.getGpsId());
				outRegionToLSpeed.setBroadcastContent(outRegionToLimitSpeed.getBroadcastContent());
				outRegionToLSpeed.setSigns(outRegionToLimitSpeed.getSigns());
				rule.addOutRegionToLSpeed(outRegionToLSpeed.build());
			}
			log.error("2317,rule,OutRegionToLSpeed="+ Arrays.toString(rule.getOutregionToLSpeed().toArray()));
			/**
			 * 如果超过终端允许设置的最大区域数<br>
			 * 则删除最老的一个区域<br>
			 * 并返回删除的区域信息
			 */
//			AreaEntity areaEntity = AreaCache.getInstance().addAreaEntity(new AreaEntity(areaInfo));
			AreaEntity areaEntity = areaCache.addAreaEntity(new AreaEntity(areaInfo));
			if (areaEntity != null) {
				// RuleCache.getInstance().getRuleEntity(areaInfo.getTerminalId()).removeRuleForArea(areaEntity.getOriginalAreaId(),AreaType.circle);
//				RuleCache.getInstance().getRuleEntity(areaInfo.getTerminalId()).removeRuleForArea(areaEntity.getOriginalAreaId(),AreaType.valueOf(areaEntity.getAreaType()));
				ruleCache.getRuleEntity(areaInfo.getTerminalId()).removeRuleForArea(areaEntity.getOriginalAreaId(),AreaType.valueOf(areaEntity.getAreaType()));
			}
			LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
			regularBuilder.setTerminalId(areaInfo.getTerminalId());
			regularBuilder.setRegularCode(LCRegularCode.RegularCode.outregionToLSpeed);
			regularBuilder.setType(RegularType.individual);// 是否为通用规则
			regularBuilder.setLastModifyDate(System.currentTimeMillis() / 1000);

			LCOutRegionToLSpeed.OutRegionToLSpeed.Builder orlpBuilder = LCOutRegionToLSpeed.OutRegionToLSpeed.newBuilder();
			orlpBuilder.setAreaId(areaInfo.getAreaIdentify());
			orlpBuilder.setControlType(outRegionToLimitSpeed.getControlType());//
			orlpBuilder.setLimitSpeed(outRegionToLimitSpeed.getLimitSpeed());
			orlpBuilder.setGpsId(outRegionToLimitSpeed.getGpsId());//
			orlpBuilder.setBroadcastContent(outRegionToLimitSpeed.getBroadcastContent());
			orlpBuilder.setSigns(outRegionToLimitSpeed.getSigns());
			regularBuilder.setOutregionToLSpeed(orlpBuilder.build());
			builder.addInfos(areaInfo);
			builder.addDatas(regularBuilder.build());
		}

		Packet _out_packet = new Packet(true);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setCommand(AllCommands.DataAccess.RegularDataSave_VALUE);
		_out_packet.setContent(builder.build().toByteArray());
		super.writeToDataAccess(_out_packet);
		return true;
	}

	/***********************
	 * @param packet
	 * @param scaa
	 */

	//
	// private LCAreaInfo.AreaInfo areaInfo(Packet packet, OutRegionToLimitSpeed
	// otls) {
	// LCAreaInfo.AreaInfo.Builder areaBuilder =
	// LCAreaInfo.AreaInfo.newBuilder();
	// areaBuilder.setTerminalId(Convert.uniqueMarkToLong(packet.getUniqueMark()));
	// areaBuilder.setAreaIdentify(otls.getAreaInfo().getAreaIdentify());
	// areaBuilder.setTypes(LCAreaType.AreaType.circle);
	// areaBuilder.setCreateDate(System.currentTimeMillis()/1000);
	// LCAreaData.AreaData.Builder dataBuilder =
	// LCAreaData.AreaData.newBuilder();
	// dataBuilder.setDataSN(0);
	// dataBuilder.setLatitude(area.getCenterLatitude());
	// dataBuilder.setLongitude(area.getCenterLongitude());
	// dataBuilder.setRadiusLength(area.getRadius());
	// areaBuilder.addDatas(dataBuilder.build());
	// return areaBuilder.build();
	// }

	/********************
	 * 创建区域对应的规则. . 超速报警 OverSpeed . 进出区域报警 InOutArea . 区域外开门报警
	 * DoorOpenedOutsideArea
	 * 
	 * @param packet
	 * @param area
	 * @return
	 */
	private List<RegularData> ruleInfo(Packet packet, OutRegionToLimitSpeed otls) {
		List<LCRegularData.RegularData> list = new ArrayList<LCRegularData.RegularData>();
		long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());

		for (LCAreaInfo.AreaInfo areaInfo : otls.getAreaInfoList()) {
			LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
			regularBuilder.setTerminalId(terminalId);
			regularBuilder.setRegularCode(LCRegularCode.RegularCode.outregionToLSpeed);
			regularBuilder.setType(RegularType.individual);// 是否为通用规则
			regularBuilder.setLastModifyDate(System.currentTimeMillis() / 1000);

			LCOutRegionToLSpeed.OutRegionToLSpeed.Builder orlpBuilder = LCOutRegionToLSpeed.OutRegionToLSpeed.newBuilder();
			orlpBuilder.setAreaId(areaInfo.getAreaIdentify());
			orlpBuilder.setLimitSpeed(otls.getLimitSpeed());
			orlpBuilder.setBroadcastContent(otls.getBroadcastContent());
			orlpBuilder.setSigns(otls.getSigns());

			regularBuilder.setOutregionToLSpeed(orlpBuilder.build());
			regularBuilder.build();

			list.add(regularBuilder.build());
		}
		return list;
	}
}
