package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;

import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCommonCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCommonCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.AreaValidate;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCRegularDataSave;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCInOrOutAreaNotifySet;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCInOrOutAreaNotifySetPara;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


@RPAnno(id="2507")
public class RP_2507_InOrOutAreaNotifySet extends RPCommand {

	@Autowired
	private RuleCommonCache ruleCommonCache;

	@Autowired
	private AreaCommonCache areaCommonCache;

	@Override
	public int processor(Packet packet) {
		try {
			LCInOrOutAreaNotifySet.InOrOutAreaNotifySet inOrOutAreaNotifySet = LCInOrOutAreaNotifySet.InOrOutAreaNotifySet.parseFrom(packet.getContent());
			log.error("收到RP_2507_InOrOutAreaNotifySet通知： status-->{},areaid:{},radio:{} ",inOrOutAreaNotifySet.getStatus(),inOrOutAreaNotifySet.getAreaInfo().getAreaIdentify(),inOrOutAreaNotifySet.getAreaInfo().getDatasList().get(0).getRadiusLength());
			Boolean result = regularDataSave(packet, inOrOutAreaNotifySet);
			//返回1100
			if(result){
				this.commonResponses(1,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), LCPlatformResponseResult.PlatformResponseResult.success_VALUE);
			}else{
				this.commonResponses(1,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), LCPlatformResponseResult.PlatformResponseResult.failure_VALUE);
			}
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public Boolean regularDataSave(Packet packet, LCInOrOutAreaNotifySet.InOrOutAreaNotifySet inOrOutAreaNotifySet){
		LCRegularDataSave.RegularDataSave.Builder builder = LCRegularDataSave.RegularDataSave.newBuilder();
		/*
		 * 服务站区域只有一个。暂时为圆 区域信息： AreaInfo
		 */
		LCAreaInfo.AreaInfo areaInfo = areaInfo(packet, inOrOutAreaNotifySet.getAreaInfo());
		AreaValidate areaValidate = new AreaValidate();
		boolean status = areaValidate.areaValidate(areaInfo);
		if(!status){
			return false;
		}
		long areaIdentify = areaInfo.getAreaIdentify();//区域标识（业务系统提供） 用作通用规则的缓存key

		//1、区域信息缓存
		areaCommonCache.addAreaEntity(new AreaEntity(areaInfo));
		//2、规则数据缓存
		List<LCRegularData.RegularData> list = ruleInfo(packet, inOrOutAreaNotifySet);
		
		for (LCRegularData.RegularData rd : list) {
			RuleEntity ruleEntity = new RuleEntity();
			if(rd!=null){
				ruleEntity.setAreaIdentify(areaIdentify);
				ruleEntity.setRegularCode(rd.getRegularCode());
				ruleEntity.setType(rd.getType());
				ruleEntity.setInOrOutAreaNotifySetParaRule(rd.getSetPara());
			}
			ruleCommonCache.addRuleEntity(ruleEntity);
		}

		/**
		 * 通用缓存修改,改为组播方式,RP通知DA存储
		 */
		builder.addInfos(areaInfo);
		builder.addAllDatas(list);
		// 发送RegularDataSave给DA做存储
		Packet _out_packet = new Packet(true);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setCommand(LCAllCommands.AllCommands.DataAccess.RegularDataSave_VALUE);
		_out_packet.setContent(builder.build().toByteArray());
		super.writeToDataAccess(_out_packet);
		return true;
	}
	
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
	
	private List<LCRegularData.RegularData> ruleInfo(Packet packet, LCInOrOutAreaNotifySet.InOrOutAreaNotifySet inOrOutAreaNotifySet) {
		List<LCRegularData.RegularData> list = new ArrayList<LCRegularData.RegularData>();
		
		LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
		regularBuilder.setRegularCode(LCRegularCode.RegularCode.inOrOutAreaNotifySetPara);
		regularBuilder.setType(LCRegularData.RegularType.common);//通用规则
		regularBuilder.setLastModifyDate(System.currentTimeMillis() / 1000);
		
		LCInOrOutAreaNotifySetPara.InOrOutAreaNotifySetPara.Builder inBuilder = LCInOrOutAreaNotifySetPara.InOrOutAreaNotifySetPara.newBuilder();
		inBuilder.setAreaIdentify(inOrOutAreaNotifySet.getAreaInfo().getAreaIdentify());
		inBuilder.setStatus(inOrOutAreaNotifySet.getStatus());
		
		regularBuilder.setSetPara(inBuilder.build());
		list.add(regularBuilder.build());

		return list;
	}
}
