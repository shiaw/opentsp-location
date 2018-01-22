package com.navinfo.opentsp.platform.dp.core.rule.process.single;

import com.navinfo.opentsp.platform.dp.core.common.SpringContextUtil;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRuleAnno;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLimitSpeedSave;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCDispatchMessage;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTerminalStatusControl;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaDataEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRegularProcess;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.Rule_170100_Status;
import com.navinfo.opentsp.platform.dp.core.rule.tools.GeometricCalculation;
import com.navinfo.opentsp.platform.dp.core.rule.tools.Help.InOutMark;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**********************
 * 出区域限速
 * 规则编码：outRegionToLSpeed	170100	出区域限速   消息ID：0x2317
 * @author
 *
 */
@SingleRuleAnno(ruleId = RegularCode.outregionToLSpeed_VALUE)
public class Rule_170100_OutRegionToLSpeed extends SingleRegularProcess {


	@Resource
	private AreaCache areaCache;

	/*********************
	 * 处置过程
	 *
	 * @param
	 * dataEntity 终端上报的GPS数据实体，不包含报警状态数据
	 * RuleEntity 上层业务系统为该终端设定的规则实体<来自于数据Rule, 由DP节点启动时请求DA获得并保存于缓存中>
	 */
	@Override
	public GpsLocationDataEntity process(RuleEntity ruleEntity, GpsLocationDataEntity dataEntity) {
		try {
			if(ruleEntity != null){
				Rule_170100_Status status = Rule_170100_Status.getInstance();
				Map<String, Boolean> outRegionToLSpeedCache ;
				if(areaCache == null) {
					areaCache = (AreaCache)SpringContextUtil.getBean("areaCache");
				}
				Map<Long, AreaEntity> areaMap = areaCache.getAreaEntity(dataEntity.getTerminalId()); //获取区域
				AreaEntity areaEntity ;
				if(areaMap == null)
					return dataEntity;

				List<OutRegionToLSpeed> rules = ruleEntity.getOutregionToLSpeed(); //获取该终端的规则

				for(OutRegionToLSpeed rule: rules){
					areaEntity = areaMap.get(rule.getAreaId());
					if(areaEntity == null){
						continue;
					}

					String statusStr = dataEntity.getTerminalId()+"_" + rule.getAreaId()+"";//终端id+区域id
					//判断点是否在区域范围内
					InOutMark inOutMark = checkStatusInOrOut(areaEntity,dataEntity);
					if(inOutMark == InOutMark.Out){//点在区域范围外面
						//1、查看缓存记录
						if((status.getOutRegionToLSpeed(statusStr))!=null){
							//缓存中存在状态保存
//		        		outRegionToLSpeedCache = status.getOutRegionToLSpeed(statusStr);
							if(status.getOutRegionToLSpeed(statusStr)){
								//为true，代表之前的记录是在区域内，处理： 1、发送限速指令；2、发送限速播报；3、DA日志;4、删除缓存记录
								ToLimitSpeed(rule,dataEntity);
								sendDispatchMessage(rule,dataEntity);
								saveToDA(rule,areaEntity,dataEntity);
								status.delOutRegionToLSpeed(statusStr);
							}else{
								//为false，
							}
						}else{
							//缓存中没有记录，添加车在区域外的记录
							//车在区域外 ,不需要缓存，当车辆进入区域时，添加在区域内的缓存
//		        		status.addOutRegionToLSpeed(statusStr, false);
						}
					}else{//点在区域内
						//1、查看缓存记录
						if((status.getOutRegionToLSpeed(statusStr))==null){
							//缓存中没有记录，添加车在区域内的记录
							status.addOutRegionToLSpeed(statusStr, true);
						}
					}
				}
			}
		}catch (Exception e) {
			log.error("Rule_170100_OutRegionToLSpeed process error",e);
		}
		return dataEntity;
	}

	/**
	 * 判断点是否在区域（圆形，矩形，多边形，路线）内
	 * @param areaEntity
	 * @param dataEntity
	 * @return 点在区域内的状态枚举
	 */
	private InOutMark checkStatusInOrOut(AreaEntity areaEntity, GpsLocationDataEntity dataEntity){
		boolean isInArea = false;
		List<AreaDataEntity> areaDataEntities = areaEntity.getDatas();
		switch (areaEntity.getAreaType()) {
		    case AreaType.circle_VALUE:
		    	isInArea = GeometricCalculation.insideCircle(dataEntity.getLongitude(),dataEntity.getLatitude(),areaEntity.getDatas().get(0),areaEntity.getDatas().get(0).getRadiusLength());
			break;
		    case AreaType.rectangle_VALUE:
		    	isInArea = GeometricCalculation.insideRectangle(dataEntity.getLongitude(), dataEntity.getLatitude(), areaDataEntities.get(0), areaDataEntities.get(1));
			break;
		    case AreaType.polygon_VALUE:
		    	isInArea = GeometricCalculation.insidePolygon(dataEntity.getLongitude(), dataEntity.getLatitude(), areaDataEntities);
			break;
		    case AreaType.route_VALUE:
		    	isInArea = GeometricCalculation.insideRoute(dataEntity.getLongitude(), dataEntity.getLatitude(), areaDataEntities);
			break;
		}
		InOutMark inoutMark = isInArea ? InOutMark.In : InOutMark.Out;
		return inoutMark;
	}

	/********
	 * 终端限速-限制转速
	 * @param rules
	 * @param dataEntity
	 */
	private void ToLimitSpeed(OutRegionToLSpeed rules, GpsLocationDataEntity dataEntity){
		LCTerminalStatusControl.TerminalStatusControl.Builder statusControlBuilder = LCTerminalStatusControl.TerminalStatusControl.newBuilder();
//		statusControlBuilder.setControls(LCTerminalStatusControl.PeripheralControl.limitSpeed);
		statusControlBuilder.setControls(LCTerminalStatusControl.PeripheralControl.lockVehicle);//controls
		statusControlBuilder.addAllParas(Arrays.asList(1,rules.getLimitSpeed(), Integer.parseInt(rules.getGpsId())));//paras
		statusControlBuilder.setControlType(rules.getControlType());//controlType

		Packet _packet = new Packet(true);
		_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_packet.setTo(dataEntity.getTerminalId());
		_packet.setFrom(NodeHelper.getNodeCode());
		_packet.setUniqueMark(dataEntity.getUniqueMark());
		_packet.setCommand(AllCommands.Terminal.TerminalStatusControl_VALUE);
		_packet.setContent(statusControlBuilder.build().toByteArray());
		// TODO: 16/8/24
		//new RP_2170_TerminalStatusControl().processor(_packet);
		log.error("终端限速-限制转速：packet{},tid:{}",_packet.toString(),dataEntity.getTerminalId());
		writeToTermianl2170.processor(_packet);
	}

	/********
	 * TTS播报
	 * @param rule
	 * @param dataEntity
	 */
	private void sendDispatchMessage(OutRegionToLSpeed rule, GpsLocationDataEntity dataEntity){
		LCDispatchMessage.DispatchMessage.Builder  dispatchMessBuilder = LCDispatchMessage.DispatchMessage.newBuilder();
		dispatchMessBuilder.setMessageContent(rule.getBroadcastContent());
		dispatchMessBuilder.setSigns(rule.getSigns());

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
		writeToTermianl2151.processor(_packet);
	}

	private void saveToDA(OutRegionToLSpeed rule, AreaEntity areaEntity, GpsLocationDataEntity dataEntity){
		LCOutRegionToLimitSpeedSave.OutRegionToLimitSpeedSave.Builder limitSpeedSaveBuilder = LCOutRegionToLimitSpeedSave.OutRegionToLimitSpeedSave.newBuilder();
		limitSpeedSaveBuilder.setTerminalId(dataEntity.getTerminalId());
		limitSpeedSaveBuilder.setAreaIdentify(areaEntity.getAreaId());
		limitSpeedSaveBuilder.setLimitDate(System.currentTimeMillis()/1000);
		limitSpeedSaveBuilder.setLimitSpeed(rule.getLimitSpeed());
		limitSpeedSaveBuilder.setSigns(true);

		Packet _packet = new Packet(true);
		_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_packet.setTo(dataEntity.getTerminalId());
		_packet.setFrom(NodeHelper.getNodeCode());
		_packet.setUniqueMark(dataEntity.getUniqueMark());
		_packet.setCommand(AllCommands.DataAccess.OutRegionToLimitSpeedSave_VALUE);
		_packet.setContent(limitSpeedSaveBuilder.build().toByteArray());

		// TODO: 16/8/24
		super.writeToDataAccess(_packet);
	}
}



















