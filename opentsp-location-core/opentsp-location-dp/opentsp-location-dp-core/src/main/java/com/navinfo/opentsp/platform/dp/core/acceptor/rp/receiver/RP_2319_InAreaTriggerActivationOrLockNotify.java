package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCInAreaTriggerActivationOrLock.InAreaTriggerActivationOrLock;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCRegularDataSave;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCInAreaTriggerActivationOrLockNotify.InAreaTriggerActivationOrLockNotify;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@RPAnno(id="2319")
public class RP_2319_InAreaTriggerActivationOrLockNotify extends RPCommand {


	@Resource
	RuleCache ruleCache;

	@Resource
	AreaCache areaCache;

	@Override
	public int processor(Packet packet) {
		try {
			InAreaTriggerActivationOrLockNotify inAreaTriggerActivationOrLockNotify = InAreaTriggerActivationOrLockNotify.parseFrom(packet.getContent());
			log.error("收到_RP_2319_InAreaTriggerActivationOrLockNotify");
			log.error(inAreaTriggerActivationOrLockNotify.toString());
			Boolean res = regularDataSave(packet, inAreaTriggerActivationOrLockNotify);
			if (res) {
				this.commonResponses(4, packet.getFrom(),packet.getSerialNumber(), packet.getCommand(),PlatformResponseResult.success_VALUE);
			} else {
				this.commonResponses(4, packet.getFrom(),packet.getSerialNumber(), packet.getCommand(),PlatformResponseResult.failure_VALUE);
			}
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		//this.commonResponses(4, packet.getFrom(), packet.getSerialNumber(),packet.getCommand(), PlatformResponseResult.success_VALUE);
		return 0;
	}

	/***********************
	 * @param packet
	 * @param scaa
	 */
	private Boolean regularDataSave(Packet packet, InAreaTriggerActivationOrLockNotify entity) {
		LCRegularDataSave.RegularDataSave.Builder builder = LCRegularDataSave.RegularDataSave.newBuilder();
		// 1、区域数据
		List<LCAreaInfo.AreaInfo> areaInfoList = entity.getAreaInfoList();
		for (LCAreaInfo.AreaInfo areaInfo : areaInfoList) {
//			RuleEntity rule = RuleCache.getInstance().getRuleEntity(areaInfo.getTerminalId());
			RuleEntity rule = ruleCache.getRuleEntity(areaInfo.getTerminalId());
			if (rule == null) {
				rule = new RuleEntity();
				rule.setTerminal(areaInfo.getTerminalId());
				InAreaTriggerActivationOrLock.Builder inAreaTriggerActivationOrLock = InAreaTriggerActivationOrLock.newBuilder();
				inAreaTriggerActivationOrLock.setAreaId(areaInfo.getAreaIdentify());
				inAreaTriggerActivationOrLock.setStatus(entity.getStatus());
				rule.addInAreaTriggerActivationOrLock(inAreaTriggerActivationOrLock.build());
//				RuleCache.getInstance().addRuleEntity(rule);
				ruleCache.addRuleEntity(rule);
			} else {
				InAreaTriggerActivationOrLock.Builder inAreaTriggerActivationOrLock = InAreaTriggerActivationOrLock.newBuilder();
				inAreaTriggerActivationOrLock.setAreaId(areaInfo.getAreaIdentify());
				inAreaTriggerActivationOrLock.setStatus(entity.getStatus());
				rule.addInAreaTriggerActivationOrLock(inAreaTriggerActivationOrLock.build());
			}
			log.error("2319,rule,InAreaTriggerActivationOrLock="+ Arrays.toString(rule.getInAreaTriggerActivationOrLockRule().toArray()));
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
			regularBuilder.setRegularCode(LCRegularCode.RegularCode.inAreaTriggerActivationOrLockNotify);
			regularBuilder.setType(RegularType.individual);// 是否为通用规则
			regularBuilder.setLastModifyDate(System.currentTimeMillis() / 1000);

			InAreaTriggerActivationOrLock.Builder inAreaTriggerActivationOrLock = InAreaTriggerActivationOrLock.newBuilder();
			inAreaTriggerActivationOrLock.setStatus(entity.getStatus());
			inAreaTriggerActivationOrLock.setAreaId(areaInfo.getAreaIdentify());
			regularBuilder.setInAreaTriggerActivationOrLock(inAreaTriggerActivationOrLock.build());
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
}