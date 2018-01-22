package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaProperty;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCLineProperty.LineProperty;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.common.LCRouteProperty;
import com.navinfo.opentsp.platform.location.protocol.common.LCRouteProperty.RouteProperty;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.*;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRouteDriverTime.RouteDriverTime;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCRegularDataSave;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCSetRouteAlarm;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCSetRouteAlarm.TurningPoint;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.cache.TerminalCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.TerminalEntity;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;


/*******************************
 * 设置路线报警
 * 
 * @author claus
 *
 */

@RPAnno(id="2311")
public class RP_2311_SetRouteAlarm extends RPCommand {
	//
	public static Logger log = LoggerFactory.getLogger(AreaCache.class);


	@Resource
	AreaCache areaCache;

	@Resource
	RuleCache ruleCache;

	@Resource
	TerminalCache terminalCache;

	
	@Override
	public int processor(Packet packet) {
		try {
			LCSetRouteAlarm.SetRouteAlarm sra = LCSetRouteAlarm.SetRouteAlarm.parseFrom(packet.getContent());
			switch(sra.getOperations().getNumber()) {
		    case 0:
		    	log.info("[DP: RouteAreaAlarm] 路线区域规则 Update操作.");
		    	updateRoute(packet, sra);
		    	break;
		    case 1:
		    	log.info("[DP: RouteAreaAlarm] 路线区域规则 Add操作.");
		    	addRoute(packet, sra);
		    	break;
		    case 2:
		    	log.info("[DP: RouteAreaAlarm] 路线区域规则 Modify操作.");
		    	modifyRoute(packet, sra);
		    	break;
		    }		
			
//		    TerminalEntity entity = TerminalCache.getInstance().getTerminal(packet.getFrom());
		    TerminalEntity entity = terminalCache.getTerminal(packet.getFrom());
		    if( entity != null ){
		    	if( entity.isRegularInTerminal() ){
				    packet.setTo(packet.getFrom());
				    super.writeToTermianl(packet);
		    	}
		    }
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
        super.commonResponses(4,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);
		return 0;
	}
	
	
	/******************************
	 * 添加路线区域对象和规则
	 * 
	 * @param packet
	 * @param sra
	 */
	private void addRoute(Packet packet, LCSetRouteAlarm.SetRouteAlarm sra) {
		LCRegularDataSave.RegularDataSave.Builder builder = 
				LCRegularDataSave.RegularDataSave.newBuilder();
		long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
		for(int i = 0; i < sra.getRoutesCount(); i++) {
			log.info("[DP: RouteAreaAlarm] 路线区域规则 ADD 操作. 保存对应的线路区域信息AreaEntity到缓存AreaCache. ");
			
			//区域信息、规则信息 ， 缓存管理(当区域大于指定数量,则删除最老的区域以及对应的规则数据)
			LCAreaInfo.AreaInfo areaInfo = routeInfo(packet, sra.getRoutes(i));
//	    	AreaEntity areaEntity = AreaCache.getInstance().addAreaEntity(new AreaEntity(areaInfo));
	    	AreaEntity areaEntity = areaCache.addAreaEntity(new AreaEntity(areaInfo));
	    	if(areaEntity != null){
//	    		RuleCache.getInstance().getRuleEntity(terminalId).removeRuleForArea(areaEntity.getOriginalAreaId(), AreaType.route);
	    		ruleCache.getRuleEntity(terminalId).removeRuleForArea(areaEntity.getOriginalAreaId(), AreaType.route);
	    	}
	    	
	    	
	    	//保存新创建的规则到临时缓存中
	    	log.info("[DP: RouteAreaAlarm] 路线区域规则 ADD 操作. 保存对应的规则到缓存RuleCache. ");
	    	List<RegularData> list = ruleInfo(packet, sra.getRoutes(i));
	        for(RegularData rd : list) {
//	        	RuleEntity rule = RuleCache.getInstance().getRuleEntity(terminalId);
	        	RuleEntity rule = ruleCache.getRuleEntity(terminalId);
	        	if(rule == null){
	        		rule = new RuleEntity();
	        		rule.setTerminal(terminalId);
	        	}
	        	if(rd.hasDoorOpenOutArea())
	        		rule.addDoorOpenOutAreaRule(rd.getDoorOpenOutArea());
	        	if(rd.hasInOutArea())
	        		rule.addInOutAreaRule(rd.getInOutArea());
	        	if(rd.hasSpeeding())
	        		rule.addAreaSpeedingRule(rd.getSpeeding());
	        	if(rd.hasDriverTime())
	        		rule.addRouteDriverTimeRule(rd.getDriverTime());
//	        	RuleCache.getInstance().addRuleEntity(rule);
	        	ruleCache.addRuleEntity(rule);

	        	builder.addDatas(rd);
	        }
	    	builder.addInfos(areaInfo);
	    }
		
		
		//发送RegularDataSave给DA做存储
		log.info("[DP: RouteAreaAlarm] 路线区域规则 ADD 操作. 转发给DA做添加区域信息和规则操作[0x0933]. ");
		Packet _out_packet = new Packet(true);
		_out_packet.setCommand(AllCommands.DataAccess.RegularDataSave_VALUE);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setContent(builder.build().toByteArray());
		
		
		try {
			LCRegularDataSave.RegularDataSave lcr  = LCRegularDataSave.RegularDataSave.parseFrom(_out_packet.getContent());
			System.out.println();
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		super.writeToDataAccess(_out_packet);
	}
	
	
	/*************************
	 * 线路规则更新 
	 * 
	 * @param packet
	 * @param sra
	 */
	private void updateRoute(Packet packet, LCSetRouteAlarm.SetRouteAlarm sra) {
		long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
		
		//删除缓存中与该区域对应的规则实体，根据区域类型删除
		log.info("[DP: RouteAreaAlarm] 路线区域规则 Update操作. 删除当前区域对应的在RuleCache中的规则缓存. AreaCache中缓存的路线区域信息. ");
//		RuleEntity ruleEntity = RuleCache.getInstance().getRuleEntity(terminalId);
		RuleEntity ruleEntity = ruleCache.getRuleEntity(terminalId);
		Set<Long> deleteAreaIds = ruleEntity.removeRuleForArea(AreaType.route);
//		AreaCache.getInstance().removeAreaEntity(Convert.uniqueMarkToLong(packet.getUniqueMark()) , AreaType.route);
		areaCache.removeAreaEntity(Convert.uniqueMarkToLong(packet.getUniqueMark()) , AreaType.route);

		//构建DA数据
		log.info("[DP: RouteAreaAlarm] 路线区域规则 Update操作. 转发给DA做Delete区域信息和规则操作[0x0931]. ");
		LCDeleteAreaInfo.DeleteAreaInfo.Builder builder = LCDeleteAreaInfo.DeleteAreaInfo.newBuilder();
		builder.setTerminalId(terminalId);
		builder.addAllAreaIdentify(deleteAreaIds);
		
		Packet _out_packet = new Packet(true);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setCommand(AllCommands.DataAccess.DeleteAreaInfo_VALUE);
		_out_packet.setContent(builder.build().toByteArray());
		super.writeToDataAccess(_out_packet);
		
		//添加最新区域
		addRoute(packet,sra);
		
	}
	
	
	
	/*************************
	 * 线路规则修改
	 * 
	 * @param packet
	 * @param sra
	 */
	private void modifyRoute(Packet packet, LCSetRouteAlarm.SetRouteAlarm sra) {
		log.info("[DP: RouteAreaAlarm] 路线区域规则 Modify操作. 删除当前区域对应的在RuleCache中的规则缓存. AreaCache中缓存的路线区域信息. ");
		long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
		
		//删除缓存中旧的区域信息和规则信息
		for(int i = 0; i < sra.getRoutesCount(); i++) {
//			RuleEntity ruleEntity = RuleCache.getInstance().getRuleEntity(terminalId);
			RuleEntity ruleEntity = ruleCache.getRuleEntity(terminalId);
			if(ruleEntity != null)
				ruleEntity.removeRuleForArea(sra.getRoutes(i).getRouteIdentify(), 
						LCAreaType.AreaType.route);
//			AreaCache.getInstance().removeAreaEntity(terminalId, sra.getRoutes(i).getRouteIdentify(),
//					LCAreaType.AreaType.route);
			areaCache.removeAreaEntity(terminalId, sra.getRoutes(i).getRouteIdentify(),
					LCAreaType.AreaType.route);
		}
		
		//添加最新区域
		addRoute(packet, sra);
	}
	
	
	
	private LCAreaInfo.AreaInfo routeInfo(Packet packet, LCSetRouteAlarm.Route route) {
		LCAreaInfo.AreaInfo.Builder areaBuilder = LCAreaInfo.AreaInfo.newBuilder();
	    areaBuilder.setTerminalId(Convert.uniqueMarkToLong(packet.getUniqueMark()));
		areaBuilder.setAreaIdentify(route.getRouteIdentify());
	    areaBuilder.setTypes(LCAreaType.AreaType.route);
	    areaBuilder.setCreateDate(System.currentTimeMillis()/1000);
	    for(int j = 0; j < route.getTurningPointsCount(); j++) {
	    	LCSetRouteAlarm.TurningPoint turingPoint = route.getTurningPoints(j);
	    	LCAreaData.AreaData.Builder dataBuilder = LCAreaData.AreaData.newBuilder();
	    	dataBuilder.setDataSN(j);
	    	dataBuilder.setDataStatus(turingPoint.getLineIdentify());
	    	dataBuilder.setLatitude(turingPoint.getTurningLatitude());
	    	dataBuilder.setLongitude(turingPoint.getTurningLongitude());
	    	dataBuilder.setRadiusLength(turingPoint.getLineWidth());
	    	areaBuilder.addDatas(dataBuilder.build());
	    }
	    
	    return areaBuilder.build();
	}

	/*****************
	 * 创建区域对应的规则.
	 * . 超速报警 OverSpeed
	 * . 进出区域报警 InOutArea
	 * . 区域外开门报警 DoorOpenedOutsideArea
	 * 
	 * @param packet
	 * @param route
	 * @return
	 */
	private List<LCRegularData.RegularData> ruleInfo(Packet packet, LCSetRouteAlarm.Route route) {
		List<LCRegularData.RegularData> list = new ArrayList<LCRegularData.RegularData>();
		int routeProperty = route.getRouteProperty();
		long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
		
		//路段限速、路段行驶时间不足或者过长规则需要循环各个拐点项
    	List<TurningPoint> turnings = route.getTurningPointsList();
    	for (int i = 0 , size =turnings.size() - 1 ; i < size; i++) {
	    	
    		//此路段限速
	    	if((turnings.get(i).getLineProperty() & LineProperty.speedLimit_VALUE) == LineProperty.speedLimit_VALUE){
	    		log.info("[DP: RouteAreaAlarm] 路线区域规则 类型. [拐点号: "+turnings.get(i).getLineIdentify()+", 限速报警]");
	    		
	    		LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
			    regularBuilder.setTerminalId(terminalId);
			    regularBuilder.setType(RegularType.individual);//是否为通用规则
			    regularBuilder.setRegularCode(LCRegularCode.RegularCode.speeding);
			    regularBuilder.setLastModifyDate(System.currentTimeMillis()/1000);
		    	LCAreaSpeeding.AreaSpeeding.Builder areaSpeedBuild = 
			    		 LCAreaSpeeding.AreaSpeeding.newBuilder();
		    	areaSpeedBuild.setAreaId(route.getRouteIdentify());
		    	areaSpeedBuild.setTypes(AreaType.segment);
		    	areaSpeedBuild.setRouteId(turnings.get(i).getLineIdentify());
		    	areaSpeedBuild.setMaxSpeed(turnings.get(i).getMaxSpeed());
		    	areaSpeedBuild.setContinuousTime(turnings.get(i).getSpeedingContinuousTime());
		    	//是否据时间
		    	boolean basedTime = (routeProperty & RouteProperty.accordingTime_VALUE) == RouteProperty.accordingTime_VALUE ? true : false;
		    	areaSpeedBuild.setBasedTime(basedTime);
		    	if(basedTime){
			    	if((routeProperty & RouteProperty.accordingTime_VALUE) == RouteProperty.accordingTime_VALUE){
			    		areaSpeedBuild.setIsEveryDay(true);
			    	}else{
			    		areaSpeedBuild.setIsEveryDay(false);
			    	}
		    		areaSpeedBuild.setStartDate(route.getBeginDate());
		    		areaSpeedBuild.setEndDate(route.getEndDate());
		    	}
			    regularBuilder.setSpeeding(areaSpeedBuild.build());
			    list.add(regularBuilder.build());
	    	}
	    	
	    	//此路段是否有路段行驶时间不足或者过长规则
	    	if((turnings.get(i).getLineProperty() & LineProperty.drivingTime_VALUE) == LineProperty.drivingTime_VALUE){
	    		log.info("[DP: RouteAreaAlarm] 路线区域规则 类型. [拐点号: "+turnings.get(i).getLineIdentify()+", 超时报警]");
	    		
	    		LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
			    regularBuilder.setTerminalId(terminalId);
			    regularBuilder.setType(RegularType.individual);//是否为通用规则
			    regularBuilder.setRegularCode(LCRegularCode.RegularCode.routeDriverTime);
			    regularBuilder.setLastModifyDate(System.currentTimeMillis()/1000);
			    RouteDriverTime.Builder routeDriverTimebuilder = RouteDriverTime.newBuilder();
			    routeDriverTimebuilder.setRouteId(route.getRouteIdentify());
			    routeDriverTimebuilder.setLineId(turnings.get(i).getLineIdentify());
			    routeDriverTimebuilder.setMinTime(turnings.get(i).getDrivingLackOfTime());
			    routeDriverTimebuilder.setMaxTime(turnings.get(i).getDrivingOutTime());
		    	//是否据时间
			    boolean basedTime = (routeProperty & RouteProperty.accordingTime_VALUE) == RouteProperty.accordingTime_VALUE ? true : false;
			    routeDriverTimebuilder.setBasedTime(basedTime);
			    if(basedTime){
			    	boolean isEveryday = (routeProperty & RouteProperty.everyDay_VALUE) == RouteProperty.everyDay_VALUE ? true : false;
			    	 routeDriverTimebuilder.setIsEveryDay(isEveryday);
			    	if(isEveryday){
			    		Calendar start = DateUtils.calendar(route.getBeginDate());
			    		start.set(Calendar.YEAR, 1970);
			    		start.set(Calendar.MONTH, 1);
			    		start.set(Calendar.DAY_OF_MONTH, 1);
			    		
			    		Calendar end = DateUtils.calendar(route.getEndDate());
			    		end.set(Calendar.YEAR, 1970);
			    		end.set(Calendar.MONTH, 1);
			    		end.set(Calendar.DAY_OF_MONTH, 1);
			    		
			    		routeDriverTimebuilder.setStartDate(start.getTimeInMillis() / 1000);
			    		routeDriverTimebuilder.setEndDate(end.getTimeInMillis() / 1000);
			    		routeDriverTimebuilder.setIsEveryDay(true);
			    	}else{
			    		routeDriverTimebuilder.setStartDate(route.getBeginDate());
			    		routeDriverTimebuilder.setEndDate(route.getEndDate());
			    		routeDriverTimebuilder.setIsEveryDay(false);
			    	}	
			    }
			   
			    regularBuilder.setDriverTime(routeDriverTimebuilder);
			    list.add(regularBuilder.build());
	    	}

	    }
    	
    	
    	//进出路线报警
	    if((routeProperty & LCRouteProperty.RouteProperty.inRouteAlarmToDriver_VALUE) == LCRouteProperty.RouteProperty.inRouteAlarmToDriver_VALUE
	    		||(routeProperty & LCRouteProperty.RouteProperty.inRouteAlarmToPlatform_VALUE) == LCRouteProperty.RouteProperty.inRouteAlarmToPlatform_VALUE
	    		||(routeProperty & LCRouteProperty.RouteProperty.outRouteAlarmToDriver_VALUE) == LCRouteProperty.RouteProperty.outRouteAlarmToDriver_VALUE
	    		||(routeProperty & LCRouteProperty.RouteProperty.outRouteAlarmToPlatform_VALUE) == LCRouteProperty.RouteProperty.outRouteAlarmToPlatform_VALUE) {
	    	
	    	LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
		    regularBuilder.setTerminalId(terminalId);
		    regularBuilder.setRegularCode(LCRegularCode.RegularCode.inOutArea);
		    regularBuilder.setLastModifyDate(System.currentTimeMillis()/1000);
		    regularBuilder.setType(RegularType.individual);//是否为通用规则
		    LCInOutArea.InOutArea.Builder inOutArea = LCInOutArea.InOutArea.newBuilder();
	    	inOutArea.setAreaId(route.getRouteIdentify());
	    	if((routeProperty & LCAreaProperty.AreaProperty.accordingTime_VALUE) == LCAreaProperty.AreaProperty.accordingTime_VALUE) {
	    		inOutArea.setBasedTime(true);
	    		inOutArea.setStartDate(route.getBeginDate());
		    	inOutArea.setEndDate(route.getEndDate());
		    	boolean isEveryday = (routeProperty & RouteProperty.everyDay_VALUE) == RouteProperty.everyDay_VALUE ? true : false;
		    	if(isEveryday){
		    		Calendar start = DateUtils.calendar(route.getBeginDate());
		    		start.set(Calendar.YEAR, 1970);
		    		start.set(Calendar.MONTH, 1);
		    		start.set(Calendar.DAY_OF_MONTH, 1);
		    		
		    		Calendar end = DateUtils.calendar(route.getEndDate());
		    		end.set(Calendar.YEAR, 1970);
		    		end.set(Calendar.MONTH, 1);
		    		end.set(Calendar.DAY_OF_MONTH, 1);
		    		
		    		inOutArea.setStartDate(start.getTimeInMillis() / 1000);
		    		inOutArea.setEndDate(end.getTimeInMillis() / 1000);
		    		inOutArea.setIsEveryDay(true);
		    		
		    	}else{
		    		inOutArea.setStartDate(route.getBeginDate());
		    		inOutArea.setEndDate(route.getEndDate());
		    		inOutArea.setIsEveryDay(false);
		    	}
	    		inOutArea.setIsEveryDay(isEveryday);
	    		log.info("[DP: RouteAreaAlarm] 路线区域规则 类型. [进去区域报警, 按照时间报警True, 是否每天="+ isEveryday +"]");
	    	}else {
	    		inOutArea.setBasedTime(false);
	    		log.info("[DP: RouteAreaAlarm] 路线区域规则 类型. [进去区域报警, 按照时间报警 False]");
	    	}
    		inOutArea.setInAreaAlarmToDriver((routeProperty & LCAreaProperty.AreaProperty.inAreaAlarmToDriver_VALUE) == LCAreaProperty.AreaProperty.inAreaAlarmToDriver_VALUE ? true : false);
    		inOutArea.setInAreaAlarmToPlatform((routeProperty & LCAreaProperty.AreaProperty.inAreaAlarmToPlatform_VALUE) == LCAreaProperty.AreaProperty.inAreaAlarmToPlatform_VALUE ? true : false);
    		inOutArea.setOutAreaAlarmToDriver((routeProperty & LCAreaProperty.AreaProperty.outAreaAlarmToDriver_VALUE) == LCAreaProperty.AreaProperty.outAreaAlarmToDriver_VALUE ? true : false);
    		inOutArea.setOutAreaAlarmToPlatform((routeProperty & LCAreaProperty.AreaProperty.outAreaAlarmToPlatform_VALUE) == LCAreaProperty.AreaProperty.outAreaAlarmToPlatform_VALUE ? true : false);
	    
		    regularBuilder.setInOutArea(inOutArea.build());
		    list.add(regularBuilder.build());
	    }
	    
	    //区域外开门报警	
	    if((routeProperty & LCAreaProperty.AreaProperty.openDoorOutArea_VALUE) == LCAreaProperty.AreaProperty.openDoorOutArea_VALUE){
	    	LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
		    regularBuilder.setTerminalId(terminalId);
		    regularBuilder.setType(RegularType.individual);//是否为通用规则
		    regularBuilder.setRegularCode(LCRegularCode.RegularCode.doorOpenOutArea);
		    regularBuilder.setLastModifyDate(System.currentTimeMillis()/1000);
		    LCDoorOpenOutArea.DoorOpenOutArea.Builder doorOpen = 
		    		LCDoorOpenOutArea.DoorOpenOutArea.newBuilder();
		    doorOpen.setAreaId(route.getRouteIdentify());
		    if((routeProperty & LCAreaProperty.AreaProperty.accordingTime_VALUE) != 0) {
		    	doorOpen.setBasedTime(true);
		    	doorOpen.setStartDate(route.getBeginDate());
		    	doorOpen.setEndDate(route.getEndDate());
		    	
		    	boolean isEveryday = (routeProperty & RouteProperty.everyDay_VALUE) == RouteProperty.accordingTime_VALUE ? true : false;
		    	if(isEveryday){
		    		Calendar start = DateUtils.calendar(route.getBeginDate());
		    		start.set(Calendar.YEAR, 1970);
		    		start.set(Calendar.MONTH, 1);
		    		start.set(Calendar.DAY_OF_MONTH, 1);
		    		
		    		Calendar end = DateUtils.calendar(route.getEndDate());
		    		end.set(Calendar.YEAR, 1970);
		    		end.set(Calendar.MONTH, 1);
		    		end.set(Calendar.DAY_OF_MONTH, 1);
		    		
		    		doorOpen.setStartDate(start.getTimeInMillis() / 1000);
		    		doorOpen.setEndDate(end.getTimeInMillis() / 1000);
		    		doorOpen.setIsEveryDay(true);
		    	}else{
		    		doorOpen.setStartDate(route.getBeginDate());
		    		doorOpen.setEndDate(route.getEndDate());
		    		doorOpen.setIsEveryDay(false);
		    	}
	    		doorOpen.setIsEveryDay(isEveryday);
	    		log.info("[DP: RouteAreaAlarm] 路线区域规则 类型. [区域外开门报警, 按照时间报警 True,是否每天="+ isEveryday +"]");
		    }else {
		    	doorOpen.setBasedTime(false);
		    	log.info("[DP: RouteAreaAlarm] 路线区域规则 类型. [区域外开门报警, 按照时间报警 False]");
		    }
		    regularBuilder.setDoorOpenOutArea(doorOpen.build());
		    list.add(regularBuilder.build());
	    }
	    
	    
	    return list;
	}
}
