package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaProperty;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.*;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCRegularDataSave;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCSetRectangleAreaAlarm;
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
import com.navinfo.opentsp.platform.location.kit.LCConstant.LCMessageType;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

@RPAnno(id="2307")
public class RP_2307_SetRectangleAreaAlarm extends RPCommand {

	@Resource
	TerminalCache terminalCache;

	@Resource
	RuleCache ruleCache;

	@Resource
	AreaCache areaCache;

	//
	public static Logger log = LoggerFactory.getLogger(AreaCache.class);
	
	
	@Override
	public int processor(Packet packet) {
		try {
			LCSetRectangleAreaAlarm.SetRectangleAreaAlarm sraa =
					LCSetRectangleAreaAlarm.SetRectangleAreaAlarm.parseFrom(packet.getContent());
		    switch(sraa.getOperations().getNumber()) {
		    case 0:
		    	log.info("[DP: RectangleAreaAlarm] 矩形区域规则 Update操作.");
		    	updateRectangleArea(packet, sraa);
		    	break;
		    case 1:
		    	log.info("[DP: RectangleAreaAlarm] 矩形区域规则Add操作.");
		    	addRectangleArea(packet, sraa);
		    	break;
		    case 2:
		    	log.info("[DP: RectangleAreaAlarm] 矩形区域规则Modify操作.");
		    	modifyRectangleArea(packet, sraa);
		    	break;
		    }
		    
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
	
	
	/***********************************
	 * 以下是收到添加矩形报警数据后需要做的事.
	 * . 从消息体拿到终端ID，及待添加的区域List
	 * . For each area[] 
	 *     创建区域对象  AreaInfo
	 *     创建区域规则  List<ReguarrData>
	 *     缓存当前区域对象 AreaInfo (缓存List size<=16, 超出默认删除旧的，同时删除规则缓存中，与之对应的规则)
	 *     缓存区域规则到本地
	 * . 发送给DA做存储
	 * 
	 * @param packet
	 * @param sraa
	 */
	private void addRectangleArea(Packet packet, LCSetRectangleAreaAlarm.SetRectangleAreaAlarm sraa) {
		LCRegularDataSave.RegularDataSave.Builder builder = 
				LCRegularDataSave.RegularDataSave.newBuilder();
		long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
		for(int i = 0; i < sraa.getAreasCount(); i++) {
			log.info("[DP: RectangleAreaAlarm] 矩形区域规则 Add操作. 保存对应的矩形区域信息AreaEntity到缓存AreaCache.");
			
			//区域信息、规则信息 ,缓存管理(当区域大于指定数量,则删除最老的区域以及对应的规则数据)，返回被删除的区域对象
			LCAreaInfo.AreaInfo areaInfo = areaInfo(packet, sraa.getAreas(i));
	    	AreaEntity areaEntity = areaCache.addAreaEntity(new AreaEntity(areaInfo));
	    	if(areaEntity != null){
	    		//同样删除与区域相关的规则
				ruleCache.getRuleEntity(terminalId).removeRuleForArea(areaEntity.getOriginalAreaId(), AreaType.rectangle);
	    	}
	    	
	    	//保存新创建的规则到临时缓存中
	    	log.info("[DP: RectangleAreaAlarm] 矩形区域规则 Add操作. 保存对应的规则到缓存RuleCache.");
	    	List<RegularData> list = ruleInfo(packet, sraa.getAreas(i));
	    	for(RegularData rd : list) {
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
	        	if(rd.hasKeyPointFence()){
	        		rule.addKeyPointFenceRule(rd.getKeyPointFence());
	        	}
				ruleCache.addRuleEntity(rule);
	        	
	        	builder.addDatas(rd);
	        }
	    	
	    	builder.addInfos(areaInfo);
	        
	    }
		
		//发送RegularDataSave给DA做存储
		log.info("[DP: RectangleAreaAlarm] 矩形区域规则 Add操作. 转发给DA做添加区域信息和规则操作[0x0933].");
		Packet _out_packet = new Packet(true);
		_out_packet.setCommand(AllCommands.DataAccess.RegularDataSave_VALUE);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setContent(builder.build().toByteArray());
		super.writeToDataAccess(_out_packet);
	}
	
	
	/*********************************
	 * 以下是收到 “更新”矩形报警数据后需要做的事.
	 * . 删除与之对应的旧数据
	 *   删除缓存中与之对应的区域实体，及规则对象
	 *   DA删除数据库中对应的实体对象
	 *   
	 * . 重新做添加操作
	 * 
	 * @param packet
	 * @param spaa
	 */
	private void updateRectangleArea(Packet packet, LCSetRectangleAreaAlarm.SetRectangleAreaAlarm sraa) {
		long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
		
		//删除缓存中与该区域对应的规则实体，根据区域类型删除
		log.info("[DP: RectangleAreaAlarm] 矩形区域规则 Update操作. 删除当前区域对应的在RuleCache中的规则缓存. AreaCache中缓存的多边形区域信息. ");
		RuleEntity ruleEntity = ruleCache.getRuleEntity(terminalId);
		Set<Long> deleteAreaIds = ruleEntity.removeRuleForArea(AreaType.rectangle);
		//删除缓存中的区域对象，根据终端节点编号和区域类型
		areaCache.removeAreaEntity(Convert.uniqueMarkToLong(packet.getUniqueMark()), AreaType.rectangle);
		
		
		//向DA发送数据
		log.info("[DP: RectangleAreaAlarm] 矩形区域规则 Update操作. 转发给DA做Delete区域信息和规则操作[0x0931]. ");
		LCDeleteAreaInfo.DeleteAreaInfo.Builder builder = LCDeleteAreaInfo.DeleteAreaInfo.newBuilder();
		builder.setTerminalId(terminalId);
		builder.addAllAreaIdentify(deleteAreaIds);
		Packet _out_packet = new Packet(true);
		_out_packet.setCommand(AllCommands.DataAccess.DeleteAreaInfo_VALUE);
		_out_packet.setProtocol(LCMessageType.PLATFORM);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setContent(builder.build().toByteArray());
		super.writeToDataAccess(_out_packet);
		
		//添加最新区域
		addRectangleArea(packet, sraa);
	}
	
	
	/***************************
	 * 部分修改区域规则
	 * 
	 * @param packet
	 * @param spaa
	 */
	private void modifyRectangleArea(Packet packet, LCSetRectangleAreaAlarm.SetRectangleAreaAlarm sraa) {
		log.info("[DP: RectangleAreaAlarm] 矩形区域规则 Modify操作. 删除当前区域对应的在RuleCache中的规则缓存. AreaCache中缓存的矩形区域信息. ");
		
		long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
		//缓存管理
		RuleEntity ruleEntity = ruleCache.getRuleEntity(terminalId);
		for(int i = 0 , size = sraa.getAreasCount() ; i < size ; i++) {
			ruleEntity.removeRuleForArea(sraa.getAreas(i).getAreaId(), AreaType.rectangle);
			areaCache.removeAreaEntity(terminalId, sraa.getAreas(i).getAreaId(),
					LCAreaType.AreaType.rectangle);
		}
		
		addRectangleArea(packet, sraa);
	}
	
	
	/**************************************
	 * 创建区域ID与终端ID之间的对应关系类 areaInfo
	 * . 设置终端ID
	 * . 设置区域ID，由业务系统指定
	 * . 保存左上角，右下角点
	 * 
	 * @param packet
	 * @param area
	 * @return
	 */
	private LCAreaInfo.AreaInfo areaInfo(Packet packet, LCSetRectangleAreaAlarm.RectangleArea area) {
		LCAreaInfo.AreaInfo.Builder areaBuilder = LCAreaInfo.AreaInfo.newBuilder();
	    areaBuilder.setTerminalId(Convert.uniqueMarkToLong(packet.getUniqueMark()));
		areaBuilder.setAreaIdentify(area.getAreaId());
	    areaBuilder.setTypes(LCAreaType.AreaType.rectangle);
	    areaBuilder.setCreateDate(System.currentTimeMillis()/1000);
	    LCAreaData.AreaData.Builder dataBuilder1 = LCAreaData.AreaData.newBuilder();
	    LCAreaData.AreaData.Builder dataBuilder2 = LCAreaData.AreaData.newBuilder();
	    dataBuilder1.setDataSN(0);
	    dataBuilder1.setLatitude(area.getLeftTopLatitude());
	    dataBuilder1.setLongitude(area.getLeftToplongitude());
	    dataBuilder2.setDataSN(1);
	    dataBuilder2.setLatitude(area.getRightBottomLatitude());
	    dataBuilder2.setLongitude(area.getRightBottomLongitude());
	    areaBuilder.addDatas(dataBuilder1.build());
	    areaBuilder.addDatas(dataBuilder2.build());
	    
	    return areaBuilder.build();
	}
	
	
	/*******************************
	 * 创建区域对应的规则.
	 * . 超速报警 OverSpeed
	 * . 进出区域报警 InOutArea
	 * . 区域外开门报警 DoorOpenedOutsideArea
	 * 
	 * @param packet
	 * @param area
	 * @return
	 */
	private List<LCRegularData.RegularData> ruleInfo(Packet packet, LCSetRectangleAreaAlarm.RectangleArea area) {
		List<LCRegularData.RegularData> list = new ArrayList<LCRegularData.RegularData>();
		int areaProperty = area.getAreaProperty();
		long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
		//区域超速报警
	    if((areaProperty & LCAreaProperty.AreaProperty.speedLimit_VALUE) == LCAreaProperty.AreaProperty.speedLimit_VALUE){
	    	
	    	LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
	    	regularBuilder.setTerminalId(terminalId);
	    	regularBuilder.setType(RegularType.individual);//是否为通用规则
	    	regularBuilder.setRegularCode(LCRegularCode.RegularCode.speeding);
	    	regularBuilder.setLastModifyDate(System.currentTimeMillis()/1000);
	    	LCAreaSpeeding.AreaSpeeding.Builder areaSpeedBuild = 
	    			LCAreaSpeeding.AreaSpeeding.newBuilder();
	    	areaSpeedBuild.setAreaId(area.getAreaId());
	    	areaSpeedBuild.setTypes(LCAreaType.AreaType.rectangle);
	    	areaSpeedBuild.setMaxSpeed(area.getMaxSpeed());
	    	areaSpeedBuild.setContinuousTime(area.getSpeedingContinuousTime());
	    	if((areaProperty & LCAreaProperty.AreaProperty.accordingTime_VALUE) == LCAreaProperty.AreaProperty.accordingTime_VALUE) {
		    	areaSpeedBuild.setBasedTime(true);
		    	areaSpeedBuild.setStartDate(area.getBeginDate());
		    	areaSpeedBuild.setEndDate(area.getEndDate());
	    		
		    	//是否每天
		    	boolean isEveryday = (areaProperty  & LCAreaProperty.AreaProperty.everyDay_VALUE) == LCAreaProperty.AreaProperty.everyDay_VALUE ? true : false;
		    	if(isEveryday){
		    		Calendar start = DateUtils.calendar(area.getBeginDate());
		    		start.set(Calendar.YEAR, 1970);
		    		start.set(Calendar.MONTH, 1);
		    		start.set(Calendar.DAY_OF_MONTH, 1);
		    		
		    		Calendar end = DateUtils.calendar(area.getEndDate());
		    		end.set(Calendar.YEAR, 1970);
		    		end.set(Calendar.MONTH, 1);
		    		end.set(Calendar.DAY_OF_MONTH, 1);
		    		
		    		areaSpeedBuild.setStartDate(start.getTimeInMillis() / 1000);
			    	areaSpeedBuild.setEndDate(end.getTimeInMillis() / 1000);
		    	}else{
		    		areaSpeedBuild.setStartDate(area.getBeginDate());
			    	areaSpeedBuild.setEndDate(area.getEndDate());
		    	}
		    	
		    	areaSpeedBuild.setIsEveryDay(isEveryday);
		    	log.info("[DP: RouteAreaAlarm] 矩形区域规则 类型. [区域超速报警, 按照时间报警 True, 是否每天"+isEveryday+"]");
		    } else {
		    	areaSpeedBuild.setBasedTime(false);
		    	log.info("[DP: RouteAreaAlarm] 矩形区域规则 类型. [区域超速报警, 按照时间报警 False]");
		    }
	    	regularBuilder.setSpeeding(areaSpeedBuild.build());
	    	list.add(regularBuilder.build());
	    }
	   
	    
	    //进出区域报警
	    if((areaProperty & LCAreaProperty.AreaProperty.inAreaAlarmToDriver_VALUE) == LCAreaProperty.AreaProperty.inAreaAlarmToDriver_VALUE
	    		||(areaProperty & LCAreaProperty.AreaProperty.inAreaAlarmToPlatform_VALUE) == LCAreaProperty.AreaProperty.inAreaAlarmToPlatform_VALUE
	    		||(areaProperty & LCAreaProperty.AreaProperty.outAreaAlarmToDriver_VALUE) == LCAreaProperty.AreaProperty.outAreaAlarmToDriver_VALUE
	    		||(areaProperty & LCAreaProperty.AreaProperty.outAreaAlarmToPlatform_VALUE) == LCAreaProperty.AreaProperty.outAreaAlarmToPlatform_VALUE){
	    	LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
		    regularBuilder.setTerminalId(terminalId);
		    regularBuilder.setType(RegularType.individual);//是否为通用规则
		    regularBuilder.setRegularCode(LCRegularCode.RegularCode.inOutArea);
		    regularBuilder.setLastModifyDate(System.currentTimeMillis()/1000);
	    	LCInOutArea.InOutArea.Builder inOutArea = LCInOutArea.InOutArea.newBuilder();
	    	inOutArea.setAreaId(area.getAreaId());
	    	if((areaProperty & LCAreaProperty.AreaProperty.accordingTime_VALUE) == LCAreaProperty.AreaProperty.accordingTime_VALUE) {
	    		inOutArea.setBasedTime(true);
	    		inOutArea.setStartDate(area.getBeginDate());
		    	inOutArea.setEndDate(area.getEndDate());
	    		
		    	//是否每天
		    	boolean isEveryday = (areaProperty  & LCAreaProperty.AreaProperty.everyDay_VALUE) != 0 ? true : false;
		    	if(isEveryday){
		    		Calendar start = DateUtils.calendar(area.getBeginDate());
		    		start.set(Calendar.YEAR, 1970);
		    		start.set(Calendar.MONTH, 1);
		    		start.set(Calendar.DAY_OF_MONTH, 1);
		    		
		    		Calendar end = DateUtils.calendar(area.getEndDate());
		    		end.set(Calendar.YEAR, 1970);
		    		end.set(Calendar.MONTH, 1);
		    		end.set(Calendar.DAY_OF_MONTH, 1);
		    		
		    		inOutArea.setStartDate(start.getTimeInMillis() / 1000);
		    		inOutArea.setEndDate(end.getTimeInMillis() / 1000);
		    	}else{
		    		inOutArea.setStartDate(area.getBeginDate());
		    		inOutArea.setEndDate(area.getEndDate());
		    	}
		    	inOutArea.setIsEveryDay(isEveryday);
		    	log.info("[DP: RouteAreaAlarm] 矩形区域规则 类型. [进出区域报警, 按照时间报警 True, 是否每天"+isEveryday+"]");
	    	}else {
	    		inOutArea.setBasedTime(false);
	    		log.info("[DP: RouteAreaAlarm] 矩形区域规则 类型. [进出区域报警, 按照时间报警 False]");
	    	}
    		inOutArea.setInAreaAlarmToDriver((areaProperty & LCAreaProperty.AreaProperty.inAreaAlarmToDriver_VALUE) == LCAreaProperty.AreaProperty.inAreaAlarmToDriver_VALUE ? true : false);
    		inOutArea.setInAreaAlarmToPlatform((areaProperty & LCAreaProperty.AreaProperty.inAreaAlarmToPlatform_VALUE) == LCAreaProperty.AreaProperty.inAreaAlarmToPlatform_VALUE ? true : false);
    		inOutArea.setOutAreaAlarmToDriver((areaProperty & LCAreaProperty.AreaProperty.outAreaAlarmToDriver_VALUE) == LCAreaProperty.AreaProperty.outAreaAlarmToDriver_VALUE ? true : false);
    		inOutArea.setOutAreaAlarmToPlatform((areaProperty & LCAreaProperty.AreaProperty.outAreaAlarmToPlatform_VALUE) == LCAreaProperty.AreaProperty.outAreaAlarmToPlatform_VALUE ? true : false);
		    regularBuilder.setInOutArea(inOutArea.build());
		    list.add(regularBuilder.build());
	    }
	    
	    
	    //区域外开门报警	
	    if((areaProperty & LCAreaProperty.AreaProperty.openDoorOutArea_VALUE) == LCAreaProperty.AreaProperty.openDoorOutArea_VALUE){
	    	LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
		    regularBuilder.setTerminalId(terminalId);
		    regularBuilder.setType(RegularType.individual);//是否为通用规则
		    regularBuilder.setRegularCode(LCRegularCode.RegularCode.doorOpenOutArea);
		    regularBuilder.setLastModifyDate(System.currentTimeMillis()/1000);
		    LCDoorOpenOutArea.DoorOpenOutArea.Builder doorOpen = 
		    		LCDoorOpenOutArea.DoorOpenOutArea.newBuilder();
		    doorOpen.setAreaId(area.getAreaId());
		    if((areaProperty & LCAreaProperty.AreaProperty.accordingTime_VALUE) == LCAreaProperty.AreaProperty.accordingTime_VALUE) {
		    	doorOpen.setBasedTime(true);
		    	doorOpen.setStartDate(area.getBeginDate());
		    	doorOpen.setEndDate(area.getEndDate());
		    	
		    	//是否每天
		    	boolean isEveryday = (areaProperty  & LCAreaProperty.AreaProperty.everyDay_VALUE) == LCAreaProperty.AreaProperty.everyDay_VALUE ? true : false;
		    	if(isEveryday){
		    		Calendar start = DateUtils.calendar(area.getBeginDate());
		    		start.set(Calendar.YEAR, 1970);
		    		start.set(Calendar.MONTH, 1);
		    		start.set(Calendar.DAY_OF_MONTH, 1);
		    		
		    		Calendar end = DateUtils.calendar(area.getEndDate());
		    		end.set(Calendar.YEAR, 1970);
		    		end.set(Calendar.MONTH, 1);
		    		end.set(Calendar.DAY_OF_MONTH, 1);
		    		
		    		doorOpen.setStartDate(start.getTimeInMillis() / 1000);
		    		doorOpen.setEndDate(end.getTimeInMillis() / 1000);
		    	}else{
		    		doorOpen.setStartDate(area.getBeginDate());
		    		doorOpen.setEndDate(area.getEndDate());
		    	}
	    		doorOpen.setIsEveryDay(isEveryday);
	    		log.info("[DP: RouteAreaAlarm] 矩形区域规则 类型. [区域外开门报警, 按照时间报警 True, 是否每天"+isEveryday+"]");
	    		
		    }else {
		    	doorOpen.setBasedTime(false);
		    	log.info("[DP: RouteAreaAlarm] 矩形区域规则 类型. [区域外开门报警, 按照时间报警 False]");
		    }
		    regularBuilder.setDoorOpenOutArea(doorOpen.build());
		    list.add(regularBuilder.build());
	    }
	    //关键点围栏规则
	    if((areaProperty & LCAreaProperty.AreaProperty.accordingTime_VALUE) == LCAreaProperty.AreaProperty.accordingTime_VALUE){
	    	boolean notArriveFenceAlarmToDriver = ((areaProperty&LCAreaProperty.AreaProperty.notArriveFenceAlarmToDriver_VALUE) == LCAreaProperty.AreaProperty.notArriveFenceAlarmToDriver_VALUE);
	    	boolean notArriveFenceAlarmToPlatform = ((areaProperty&LCAreaProperty.AreaProperty.notArriveFenceAlarmToPlatform_VALUE) == LCAreaProperty.AreaProperty.notArriveFenceAlarmToPlatform_VALUE);
		    boolean notLeaveFenceAlarmToDriver = ((areaProperty&LCAreaProperty.AreaProperty.notLeaveFenceAlarmToDriver_VALUE) == LCAreaProperty.AreaProperty.notLeaveFenceAlarmToDriver_VALUE);
		    boolean notLeaveFenceAlarmToPlatform = ((areaProperty&LCAreaProperty.AreaProperty.notLeaveFenceAlarmToPlatform_VALUE) == LCAreaProperty.AreaProperty.notLeaveFenceAlarmToPlatform_VALUE);
		    if(notArriveFenceAlarmToDriver || notArriveFenceAlarmToPlatform || notLeaveFenceAlarmToDriver || notLeaveFenceAlarmToPlatform){
		    	if((notArriveFenceAlarmToDriver || notArriveFenceAlarmToPlatform) && (notLeaveFenceAlarmToDriver || notLeaveFenceAlarmToPlatform)){
		    		log.info("[DP: KeyPointFence] 关键点围栏（矩形）. [关键点围栏报警, 未按时到达和未按时离开同时设置 False]，规则错误！！");
		    	}else{
		    		LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
				    regularBuilder.setTerminalId(terminalId);
				    regularBuilder.setType(RegularType.individual);//是否为通用规则
				    regularBuilder.setRegularCode(LCRegularCode.RegularCode.keyPointFence);
				    regularBuilder.setLastModifyDate(System.currentTimeMillis()/1000);
				    LCKeyPointFence.KeyPointFence.Builder keyPointFence = LCKeyPointFence.KeyPointFence.newBuilder();
				    keyPointFence.setAreaId(area.getAreaId());
				    //未按时到达
				    keyPointFence.setNotArriveFenceAlarmToDriver(notArriveFenceAlarmToDriver);
			    	keyPointFence.setNotArriveFenceAlarmToPlatform(notArriveFenceAlarmToPlatform);
			    	//未按时离开
			    	keyPointFence.setNotLeaveFenceAlarmToDriver(notLeaveFenceAlarmToDriver);
			    	keyPointFence.setNotLeaveFenceAlarmToPlatform(notLeaveFenceAlarmToPlatform);
			    	keyPointFence.setIsEveryDay((areaProperty&LCAreaProperty.AreaProperty.everyDay_VALUE)==LCAreaProperty.AreaProperty.everyDay_VALUE?true:false);
			    	if(keyPointFence.getIsEveryDay()){
			    		Calendar start = DateUtils.calendar(area.getBeginDate());
			    		start.set(Calendar.YEAR, 1970);
			    		start.set(Calendar.MONTH, 1);
			    		start.set(Calendar.DAY_OF_MONTH, 1);
			    		
			    		Calendar end = DateUtils.calendar(area.getEndDate());
			    		end.set(Calendar.YEAR, 1970);
			    		end.set(Calendar.MONTH, 1);
			    		end.set(Calendar.DAY_OF_MONTH, 1);
			    		
			    		keyPointFence.setStartDate(start.getTimeInMillis() / 1000);
			    		keyPointFence.setEndDate(end.getTimeInMillis() / 1000);
			    	}else{
			    		keyPointFence.setStartDate(area.getBeginDate());
			    		keyPointFence.setEndDate(area.getEndDate());
			    	}
				    regularBuilder.setKeyPointFence(keyPointFence.build());
				    list.add(regularBuilder.build());
		    	}
		    	
		    }
	    }else{
	    	log.info("[DP: KeyPointFence] 关键点围栏（矩形）. [关键点围栏报警,区域属性未设置根据时间 False]，规则错误！！");
	    }
	    return list;
	}
}
