package com.navinfo.opentsp.platform.dp.core.rule.process.common;

import com.navinfo.opentsp.platform.dp.core.rule.handler.common.CommonRegularProcess;
import com.navinfo.opentsp.platform.dp.core.rule.handler.common.CommonRuleAnno;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.dp.core.cache.DictCache;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsVehicleStatusData;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDictType;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCDispatchMessage;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.DictEntity;
import com.navinfo.opentsp.platform.dp.core.rule.entity.OilStatus;
import com.navinfo.opentsp.platform.dp.core.rule.status.common.Rule_170120_Status;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.List;

/**
 * 
 * 根据油量计算是否漏油规则，暂时屏蔽——hk
 * 规则编码：abnormalOilAlarm	170120	油量异常报警（规则内容来自字典）
 */
@CommonRuleAnno(ruleId = RegularCode.abnormalOilAlarm_VALUE)
public class Rule_170120_AbnormalOilAlarm extends CommonRegularProcess {

	@Resource
	private DictCache dictCache;

	@Resource
	private Rule_170120_Status rule_170120_Status;

	@Value("${rule.170120.process:false}")
	private boolean flag;

	@Override
	public GpsLocationDataEntity process(double distance, AreaEntity areaEntity, GpsLocationDataEntity dataEntity) {
		if(flag) {
			DictEntity dictEntity = dictCache.getDictForDataCode(LCDictType.DictType.commonRuleCode, 210002);
			if(dictEntity!=null){
				Double oilSlope = Double.parseDouble(dictEntity.getDictValue());//偷油漏油阀值（斜率）
				if(rule_170120_Status != null){
					Long terminalId = dataEntity.getTerminalId();
					if(dataEntity.getGpsVehicleStatusAddition()==null){
						return dataEntity;
					}
					List<GpsVehicleStatusData> gpsVehicleStatusDataList = dataEntity.getGpsVehicleStatusAddition().getStatus();
					if(gpsVehicleStatusDataList !=null){
						double currentOil = 0;
						for(GpsVehicleStatusData gpsVehicleStatusData:gpsVehicleStatusDataList){
							if(gpsVehicleStatusData.getTypes().equals(LCStatusType.StatusType.oilValue)){//油量
								currentOil = gpsVehicleStatusData.getStatusValue()/100;//注意，油量是实际值乘以100
								break;
							}
						}
						OilStatus oilStatus = rule_170120_Status.getOilStatus(terminalId);
						if(oilStatus != null && (oilStatus.getOilvalue()-currentOil)/(dataEntity.getGpsDate()-oilStatus.getGpstime()) >= oilSlope*100){
							dataEntity.addAbnormalOilAlarm();
							sendDispatchMessage("您的车辆出现漏油，请检查油箱! ",dataEntity);
						}
						oilStatus = new OilStatus();
						oilStatus.setGpstime(dataEntity.getGpsDate());
						oilStatus.setOilvalue(currentOil);
						rule_170120_Status.addOilStatus(terminalId, oilStatus);
					}
				}
			}
		}
		return dataEntity;
	}
	
	/***************************
	 * 发送终端播报
	 * 
	 * @param messageContent
	 * @param dataEntity
	 */
	private void sendDispatchMessage(String messageContent, GpsLocationDataEntity dataEntity){
//		List<AreaDataEntity> areaDataEntities = areaEntity.getDatas();
		LCDispatchMessage.DispatchMessage.Builder  dispatchMessBuilder = LCDispatchMessage.DispatchMessage.newBuilder();
		LCMessageSign.MessageSign.Builder messageSignBuilder = LCMessageSign.MessageSign.newBuilder();
		messageSignBuilder.setIsBroadcast(true);
		messageSignBuilder.setIsUrgent(false);
		messageSignBuilder.setIsDisplay(false);
		messageSignBuilder.setIsAdvertiseScreen(false);
		messageSignBuilder.setInfoType(false);
		dispatchMessBuilder.setMessageContent(messageContent);
		dispatchMessBuilder.setSigns(messageSignBuilder.build());//
		
		Packet _packet = new Packet(true);
		_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_packet.setTo(dataEntity.getTerminalId());
		_packet.setFrom(NodeHelper.getNodeCode());
		_packet.setUniqueMark(dataEntity.getUniqueMark());
		_packet.setCommand(AllCommands.Terminal.DispatchMessage_VALUE);
		_packet.setContent(dispatchMessBuilder.build().toByteArray());
		// TODO: 16/8/24
//		RPCommand rpc= new RP_2151_DispatchMessage();
//		rpc.processor(_packet);
		super.writeToTermianl.processor(_packet);
	}

}
