package com.navinfo.opentsp.platform.dp.core.cache.entity;

import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCommonCache;
import com.navinfo.opentsp.platform.dp.core.common.SpringContextUtil;
import com.navinfo.opentsp.platform.dp.core.rule.tools.DateUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.*;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaSpeeding.AreaSpeeding;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDelayOvertimePark.DelayOvertimePark;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDoorOpenOutArea.DoorOpenOutArea;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDriverNotCard.DriverNotCard;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDrivingBan.DrivingBan;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCInAreaTriggerActivationOrLock.InAreaTriggerActivationOrLock;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCInOutArea.InOutArea;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCKeyPointFence.KeyPointFence;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCMessageBroadcast.MessageBroadcast;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOverTimePark.OverTimePark;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRouteDriverTime.RouteDriverTime;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTerminalMessageSwitch.TerminalMessageSwitch;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCAlarmCancelOrNot.AlarmCancelOrNot;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCInOrOutAreaNotifySetPara.InOrOutAreaNotifySetPara;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class RuleEntity extends Entity{

	private static final RuleCache ruleCache;

	private static final RuleCommonCache ruleCommonCache;

	private static final AreaCache areaCache;

	static {
		ruleCache = (RuleCache)SpringContextUtil.getBean("ruleCache");
		ruleCommonCache = (RuleCommonCache)SpringContextUtil.getBean("ruleCommonCache");
		areaCache = (AreaCache)SpringContextUtil.getBean("areaCache");
	}

	public static Logger log = LoggerFactory.getLogger(RuleEntity.class);
	private static final long serialVersionUID = 1L;

	//终端标识
	private long terminal;
	//区域标识
	private long areaIdentify;//通用规则 
	//是否为通用规则 true：是；false：否   ，默认false
	private RegularType	type;

	//规则编码
	private RegularCode	regularCode;
	//一个未刷卡报警 （公务车用，暂时未涉及）
	private DriverNotCard driverNotCardRule;
	//多个禁驾驶规则（不容许使用车辆的时间段）
	private List<DrivingBan> drivingBanRule;
	//多个区域限速规则
	private List<AreaSpeeding> areaSpeedingRule;
	//多个进出区域规则
	private List<InOutArea> inOutAreaRule;
	//多个区域外开门规则
	private List<DoorOpenOutArea> doorOpenOutAreaRule;
	//多个线路/区域超时规则
	private List<RouteDriverTime> routeDriverTimeRule;
	//多个关键点围栏（圆形，矩形，多边形）
	private List<KeyPointFence> keyPointFenceRule;

	//多个区域信息推送规则 通用规则（圆形，矩形，多边形）
	private List<MessageBroadcast> messageBroadcast;	//区域信息播报  通用规则
	//多个区域滞留超时 通用规则（圆形，矩形，多边形）
	private List<OverTimePark> overtimePark;	//区域滞留超时  通用规则
	//多个出区域限速
	private List<OutRegionToLSpeed> outregionToLSpeed;	//出区域限速

	//终端信息广播开关   ,默认false
	private TerminalMessageSwitch	terminalMessage ;
	//滞留超时延时
	private DelayOvertimePark	delayPark;

	//进区域激活/锁车通知
	private List<InAreaTriggerActivationOrLock> inAreaTriggerActivationOrLockRule;

	private List<Integer> _rule_array = new ArrayList<Integer>();

	//终端区域状态通知开关  默认为关
	private boolean areaStatusNotifySwitch = true;

	//报警撤销和恢复规则
	private AlarmCancelOrNot alarmCancelOrNot;

	//进出区域通知设置参数 通用规则
	private List<InOrOutAreaNotifySetPara> setPara;

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("终端[ "+terminal+" ]规则信息\n");
		buffer.append("未刷卡规则：");
		if(this.driverNotCardRule != null){
			buffer.append("\n未刷卡时间阀值[ "+this.driverNotCardRule.getNotCardTime()+" ]");
		}else{
			buffer.append("无\n");
		}
		buffer.append("\n禁驾规则：");
		if(this.drivingBanRule != null && this.drivingBanRule.size() > 0){
			for (DrivingBan ban : this.drivingBanRule) {
				buffer.append("\n禁驾标识[ "+ban.getBanIdentify()+" ] , 是否每天[ "+ban.getIsEveryDay()+" ] , 开始时间[ "+ DateUtils.format(ban.getStartDate(), DateUtils.DateFormat.YY_YY_MM_DD_HH_MM_SS)+" ] , 结束时间[ "+DateUtils.format(ban.getEndDate(), DateUtils.DateFormat.YY_YY_MM_DD_HH_MM_SS)+" ]");
			}
		}else{
			buffer.append("无");
		}
		buffer.append("\n区域限速规则：");
		if(this.areaSpeedingRule != null && this.areaSpeedingRule.size() > 0){
			for (AreaSpeeding speeding : this.areaSpeedingRule) {
				buffer.append("\n区域/路段标识[ "+speeding.getAreaId()+" ] , 区域类型[ "+speeding.getTypes().name()+" ] , 最大速度[ "+speeding.getMaxSpeed()+" ] , 是否依据时间[ "+speeding.getBasedTime()+" ]");
			}
		}else{
			buffer.append("无\n");
		}
		buffer.append("\n进出区域规则：");
		if(this.inOutAreaRule != null && this.inOutAreaRule.size() > 0){
			for (InOutArea inout : this.inOutAreaRule) {
				buffer.append("\n区域标识[ "+inout.getAreaId()+" ] , 是否依据时间[ "+inout.getBasedTime()+" ]");
			}
		}else{
			buffer.append("无\n");
		}
		buffer.append("\n行驶线路超时/不足规则：");
		if(this.routeDriverTimeRule != null && this.routeDriverTimeRule.size() > 0){
			for (RouteDriverTime route : this.routeDriverTimeRule) {
				buffer.append("\n路线标识[ "+route.getRouteId()+" ] , 路段标识[ "+route.getLineId()+" ] , 是否依据时间[ "+route.getBasedTime()+" ]");
			}
		}else{
			buffer.append("无\n");
		}
		buffer.append("\n关键点围栏规则：");
		if(this.keyPointFenceRule != null && this.keyPointFenceRule.size() > 0){
			for (KeyPointFence fence : this.keyPointFenceRule) {
				buffer.append("\n区域标识[ "+fence.getAreaId()+" ]");
			}
		}else{
			buffer.append("无\n");
		}

		buffer.append("\n区域信息播报规则：");
		if(this.messageBroadcast != null ){
			for(MessageBroadcast message : this.messageBroadcast){
				buffer.append("\n区域标识[ "+message.getAreaId()+" ]");
			}
		}else{
			buffer.append("无\n");
		}

		buffer.append("\n区域滞留超时规则：");
		if(this.overtimePark != null ){
			for(OverTimePark overTime : this.overtimePark){
				buffer.append("\n区域标识[ "+overTime.getAreaId()+" ]");
			}
		}else{
			buffer.append("无\n");
		}

		buffer.append("\n出区域限速规则：");
		if(this.outregionToLSpeed != null ){
			for(OutRegionToLSpeed outRegion : this.outregionToLSpeed){
				buffer.append("\n区域标识[ "+outRegion.getAreaId()+" ]");
			}
		}else{
			buffer.append("无\n");
		}

		buffer.append("\n终端信息广播开关：");
		if(this.terminalMessage != null ){
			buffer.append("\n开关[ "+terminalMessage+" ]");
		}else{
			buffer.append("无\n");
		}

		buffer.append("\n滞留超时延时：");
		if(this.delayPark != null ){
			for(OutRegionToLSpeed outRegion : this.outregionToLSpeed){
				buffer.append("\n滞留超时延时[ "+delayPark+" ]");
			}
		}else{
			buffer.append("无\n");
		}

		return buffer.toString();
	}


	/**
	 * @deprecated
	 * 添加区域时,判断是否超过指定区域数量
	 * @param areaType
	 * @return
	 */
	public int getAreaCount(AreaType areaType){
		int result = 0;
		Map<Long, AreaEntity> areas = areaCache.getAreaEntity(this.terminal);
		if(this.areaSpeedingRule != null){
			for (AreaSpeeding e : this.areaSpeedingRule) {
				if(e.getTypes().getNumber() == areaType.getNumber())
					result++;
			}
		}
		if(this.inOutAreaRule != null){
			for (InOutArea e : this.inOutAreaRule) {
				AreaEntity areaEntity = areas.get(e.getAreaId());
				if(areaEntity != null && areaEntity.getAreaType() == areaType.getNumber())
					result++;
			}
		}
		return result;
	}


	/**
	 * 获取此终端需要运算的规则编码
	 * @return
	 */
	public List<Integer> getRuleArray(){
		return this._rule_array;
	}


	/**
	 * 移除此区域相关的规则
	 * @param areaId {@link Long} 业务系统区域ID
	 */
	public void removeRuleForArea(long areaId , AreaType areaType){
		//区域、路段限速
		if(this.getAreaSpeedingRule() != null){
			Iterator<AreaSpeeding> iterator = this.getAreaSpeedingRule().iterator();
			while(iterator.hasNext()){
				AreaSpeeding areaSpeeding = iterator.next();
				if(areaId == areaSpeeding.getAreaId()){
					iterator.remove();
				}
			}
		}
		//进出区域
		if(this.getInOutAreaRule() != null){
			Iterator<InOutArea> iterator = this.getInOutAreaRule().iterator();
			while(iterator.hasNext()){
				InOutArea inOutArea = iterator.next();
				if(areaId == inOutArea.getAreaId()){
					iterator.remove();
				}
			}
		}
		//区域外开门
		if(this.getDoorOpenOutAreaRule() != null){
			Iterator<DoorOpenOutArea> iterator = this.getDoorOpenOutAreaRule().iterator();
			while(iterator.hasNext()){
				DoorOpenOutArea doorOpenOutArea = iterator.next();
				if(areaId == doorOpenOutArea.getAreaId()){
					iterator.remove();
				}
			}
		}
		//路段行驶时间过长或不足
		if(this.getRouteDriverTimeRule() != null){
			Iterator<RouteDriverTime> iterator = this.getRouteDriverTimeRule().iterator();
			while(iterator.hasNext()){
				RouteDriverTime routeDriverTime = iterator.next();
				if(areaId == routeDriverTime.getRouteId()){
					iterator.remove();
				}
			}
		}
		//关键点围栏
		if(this.keyPointFenceRule != null){
			Iterator<KeyPointFence> iterator = this.getKeyPointFenceRule().iterator();
			while(iterator.hasNext()){
				KeyPointFence keyPointFence = iterator.next();
				if(areaId == keyPointFence.getAreaId()){
					iterator.remove();
				}
			}
		}
		//this.updateCache();
		this.updateRuleArray();
	}


	/**
	 * 删除此类型区域所有相关规则
	 *
	 * @param areaType {@link AreaType} 区域类型
	 * @return {@link Set < Long >}[] 删除的区域ID列表
	 */
	public Set<Long> removeRuleForArea(AreaType areaType){
		Set<Long> result = new HashSet<Long>();
		//区域、路段限速
		if(this.getAreaSpeedingRule() != null){
			Iterator<AreaSpeeding> iterator = this.getAreaSpeedingRule().iterator();
			while(iterator.hasNext()){
				AreaSpeeding areaSpeeding = iterator.next();
				if(areaType.getNumber() == areaSpeeding.getTypes().getNumber()){
					result.add(areaSpeeding.getRouteId());
					iterator.remove();
				}
			}
		}
		//进出区域
		if(this.getInOutAreaRule() != null){
			Iterator<InOutArea> iterator = this.getInOutAreaRule().iterator();
			while(iterator.hasNext()){
				InOutArea inOutArea = iterator.next();
				AreaEntity areaEntity = areaCache.getAreaEntity(this.terminal, inOutArea.getAreaId());
				if(areaEntity != null && areaType.getNumber() == areaEntity.getAreaType()){
					result.add(inOutArea.getAreaId());
					iterator.remove();
				}
			}
		}
		//区域外开门
		if(this.getDoorOpenOutAreaRule() != null){
			Iterator<DoorOpenOutArea> iterator = this.getDoorOpenOutAreaRule().iterator();
			while(iterator.hasNext()){
				DoorOpenOutArea doorOpenOutArea = iterator.next();
				AreaEntity areaEntity = areaCache.getAreaEntity(this.terminal, doorOpenOutArea.getAreaId());
				if(areaEntity != null && areaType.getNumber() == areaEntity.getAreaType()){
					result.add(doorOpenOutArea.getAreaId());
					iterator.remove();
				}
			}
		}
		//路段行驶时间过长或不足
		if(areaType.getNumber() == AreaType.route_VALUE){
			if(this.getRouteDriverTimeRule() != null){
				for (RouteDriverTime r : this.getRouteDriverTimeRule()) {
					result.add(r.getRouteId());
				}
				this.routeDriverTimeRule = new ArrayList<LCRouteDriverTime.RouteDriverTime>();
			}
		}
		//关键点围栏
		if(this.getKeyPointFenceRule() != null){
			Iterator<KeyPointFence> iterator = this.getKeyPointFenceRule().iterator();
			while(iterator.hasNext()){
				KeyPointFence keyPointFence = iterator.next();
				AreaEntity areaEntity = areaCache.getAreaEntity(this.terminal, keyPointFence.getAreaId());
				if(areaEntity != null && areaType.getNumber() == areaEntity.getAreaType()){
					result.add(keyPointFence.getAreaId());
					iterator.remove();
				}
			}

		}
		//this.updateCache();
		this.updateRuleArray();
		return result;
	}

	public long getTerminal() {
		return terminal;
	}

	/**
	 * 添加区域外开门规则
	 * @param doorOpenOutArea
	 */
	public void addDoorOpenOutAreaRule(DoorOpenOutArea doorOpenOutArea){
		if(this.doorOpenOutAreaRule == null)
			this.doorOpenOutAreaRule = new ArrayList<LCDoorOpenOutArea.DoorOpenOutArea>();
		Iterator<DoorOpenOutArea> iterator = this.getDoorOpenOutAreaRule().iterator();
		while(iterator.hasNext()){
			DoorOpenOutArea area = iterator.next();
			if(area.getAreaId() == doorOpenOutArea.getAreaId()){
				iterator.remove();
			}
		}
		this.doorOpenOutAreaRule.add(doorOpenOutArea);
		//this.updateCache();
		this.updateRuleArray();
	}

	/**
	 * 添加出区域限速规则
	 * @param outregionToLSpeed
	 */
	public void addOutRegionToLSpeed(OutRegionToLSpeed outregionToLSpeed){
		if(this.outregionToLSpeed == null)
			this.outregionToLSpeed = new ArrayList<LCOutRegionToLSpeed.OutRegionToLSpeed>();
		Iterator<OutRegionToLSpeed> iterator = this.getOutRegionToLSpeedRule().iterator();
		while(iterator.hasNext()){
			OutRegionToLSpeed area = iterator.next();
			if(area.getAreaId() == outregionToLSpeed.getAreaId()){
				iterator.remove();
			}
		}
		this.outregionToLSpeed.add(outregionToLSpeed);
		//this.updateCache();
		this.updateRuleArray();
	}
	public void addInAreaTriggerActivationOrLock(InAreaTriggerActivationOrLock inAreaTriggerActivationOrLock){
		if(this.inAreaTriggerActivationOrLockRule == null)
			this.inAreaTriggerActivationOrLockRule = new ArrayList<LCInAreaTriggerActivationOrLock.InAreaTriggerActivationOrLock>();
		Iterator<InAreaTriggerActivationOrLock> iterator = this.getInAreaTriggerActivationOrLockRule().iterator();
		while(iterator.hasNext()){
			InAreaTriggerActivationOrLock area = iterator.next();
			if(area.getAreaId() == inAreaTriggerActivationOrLock.getAreaId()){
				iterator.remove();
			}
		}
		this.inAreaTriggerActivationOrLockRule.add(inAreaTriggerActivationOrLock);
//		this.updateCache();
		this.updateRuleArray();
	}

	/**
	 * 更新规则
	 */
	private void updateRuleArray(){
		List<Integer> _array = new ArrayList<Integer>();
		if(this.driverNotCardRule != null)
			_array.add(RegularCode.driverNotCard_VALUE);
		if(this.drivingBanRule != null && this.drivingBanRule.size() > 0)
			_array.add(RegularCode.drivingBan_VALUE);
		if(this.areaSpeedingRule != null && this.areaSpeedingRule.size() > 0)
			_array.add(RegularCode.speeding_VALUE);
		if(this.inOutAreaRule != null && this.inOutAreaRule.size() > 0)
			_array.add(RegularCode.inOutArea_VALUE);
		if(this.doorOpenOutAreaRule != null && this.doorOpenOutAreaRule.size() > 0)
			_array.add(RegularCode.doorOpenOutArea_VALUE);
		if(this.routeDriverTimeRule != null && this.routeDriverTimeRule.size() > 0)
			_array.add(RegularCode.routeDriverTime_VALUE);
		if(this.keyPointFenceRule != null && this.keyPointFenceRule.size() > 0)
			_array.add(RegularCode.keyPointFence_VALUE);
		if(this.messageBroadcast != null && this.messageBroadcast.size()>0)
			_array.add(RegularCode.messageBroadcast_VALUE);
		if(this.overtimePark != null && this.overtimePark.size()>0)
			_array.add(RegularCode.overtimePark_VALUE);
		if(this.outregionToLSpeed != null && this.outregionToLSpeed.size()>0)
			_array.add(RegularCode.outregionToLSpeed_VALUE);
		if(this.terminalMessage != null )
			_array.add(RegularCode.terminalBroadcastSwitch_VALUE);
		if(this.delayPark != null)
			_array.add(RegularCode.delayOvertimePark_VALUE);
		if(this.inAreaTriggerActivationOrLockRule != null)
			_array.add(RegularCode.inAreaTriggerActivationOrLockNotify_VALUE);

		//1214 add
		if(this.alarmCancelOrNot != null)
			_array.add(RegularCode.alarmCancelOrNot_VALUE);
		if(this.areaStatusNotifySwitch)
			_array.add(RegularCode.inOrOutAreaNotifySwitchPara_VALUE);
		if(this.setPara != null)
			_array.add(RegularCode.inOrOutAreaNotifySetPara_VALUE);

		this._rule_array = _array;
	}

	/**
	 * 添加路线行驶时间不足或者过长规则
	 * @param routeDriverTime
	 */
	public void addRouteDriverTimeRule(RouteDriverTime routeDriverTime){
		if(this.routeDriverTimeRule == null)
			this.routeDriverTimeRule = new ArrayList<LCRouteDriverTime.RouteDriverTime>();
		Iterator<RouteDriverTime> iterator = this.getRouteDriverTimeRule().iterator();
		while(iterator.hasNext()){
			if(iterator.next().getRouteId() == routeDriverTime.getRouteId())
				iterator.remove();
		}
		this.routeDriverTimeRule.add(routeDriverTime);
		//this.updateCache();
		this.updateRuleArray();
	}
	/**
	 * 添加进出区域规则
	 * @param inOutArea
	 */
	public void addInOutAreaRule(InOutArea inOutArea){
		if(this.inOutAreaRule == null)
			this.inOutAreaRule = new ArrayList<LCInOutArea.InOutArea>();
		Iterator<InOutArea> iterator = this.getInOutAreaRule().iterator();
		while(iterator.hasNext()){
			if(iterator.next().getAreaId() == inOutArea.getAreaId())
				iterator.remove();
		}
		this.inOutAreaRule.add(inOutArea);
		//this.updateCache();
		this.updateRuleArray();
	}
	/**
	 * 添加区域限速规则
	 * @param areaSpeeding
	 */
	public void addAreaSpeedingRule(AreaSpeeding areaSpeeding){
		if(this.areaSpeedingRule == null)
			this.areaSpeedingRule = new ArrayList<LCAreaSpeeding.AreaSpeeding>();
		Iterator<AreaSpeeding> iterator = this.getAreaSpeedingRule().iterator();
		while(iterator.hasNext()){
			if(iterator.next().getAreaId() == areaSpeeding.getAreaId())
				iterator.remove();
		}
		this.areaSpeedingRule.add(areaSpeeding);
		//this.updateCache();
		this.updateRuleArray();
	}

	/**
	 * 添加关键点围栏规则
	 * @param keyPointFence
	 */
	public void addKeyPointFenceRule(KeyPointFence keyPointFence){
		if(this.keyPointFenceRule == null)
			this.keyPointFenceRule = new ArrayList<LCKeyPointFence.KeyPointFence>();
		Iterator<KeyPointFence> iterator = this.keyPointFenceRule.iterator();
		while(iterator.hasNext()){
			if(iterator.next().getAreaId() == keyPointFence.getAreaId())
				iterator.remove();
		}
		this.keyPointFenceRule.add(keyPointFence);
		//this.updateCache();
		this.updateRuleArray();
	}


	public void setDriverNotCardRule(DriverNotCard driverNotCardRule) {
		this.driverNotCardRule = driverNotCardRule;
		//this.updateCache();
		this.updateRuleArray();
	}

	public void setMessageBroadcastRule(MessageBroadcast messageBroadcast) {
		if(this.messageBroadcast == null){
			this.messageBroadcast = new ArrayList<LCMessageBroadcast.MessageBroadcast>();
		}
		Iterator<MessageBroadcast> iterator = this.messageBroadcast.iterator();
		while(iterator.hasNext()){
			if(iterator.next().getAreaId() == messageBroadcast.getAreaId())
				iterator.remove();
		}
		this.messageBroadcast.add(messageBroadcast);
		//this.updateCache();
		this.updateRuleArray();
	}

	public void setOverTimeParkRule(OverTimePark overtimePark) {
		if(this.overtimePark == null){
			this.overtimePark = new ArrayList<LCOverTimePark.OverTimePark>();
		}
		Iterator<OverTimePark> iterator = this.overtimePark.iterator();
		while(iterator.hasNext()){
			if(iterator.next().getAreaId() == overtimePark.getAreaId())
				iterator.remove();
		}
		this.overtimePark.add(overtimePark);
		//this.updateCache();
		this.updateRuleArray();
	}

	public void setOutRegionToLSpeedRule(OutRegionToLSpeed outregionToLSpeed) {
		if(this.outregionToLSpeed == null){
			this.outregionToLSpeed = new ArrayList<LCOutRegionToLSpeed.OutRegionToLSpeed>();
		}
		Iterator<OutRegionToLSpeed> iterator = this.outregionToLSpeed.iterator();
		while(iterator.hasNext()){
			if(iterator.next().getAreaId() == outregionToLSpeed.getAreaId())
				iterator.remove();
		}
		this.outregionToLSpeed.add(outregionToLSpeed);
		//this.updateCache();
		this.updateRuleArray();
	}



	/***********
	 * 添加禁驾规则
	 *
	 * @param drivingBanRule
	 */
	public void setDrivingBanRule(DrivingBan drivingBanRule) {
		if(this.drivingBanRule == null)
			this.drivingBanRule = new ArrayList<DrivingBan>();
		this.drivingBanRule.add(drivingBanRule);
		//this.updateCache();
		this.updateRuleArray();
	}

	/***************
	 * 删除禁驾规则
	 *
	 * @param allRuleIds
	 */
    public void removeDrivingBanRule(boolean isAllRemoved, List<Long> allRuleIds) {
    	if(isAllRemoved){
    		 this.drivingBanRule.clear();
    	} else {
    		Iterator<DrivingBan> iterator = this.drivingBanRule.iterator();
    		while(iterator.hasNext()){
    			DrivingBan ban = iterator.next();
    			for(long drivingID : allRuleIds){
    				if(ban.getBanIdentify() == drivingID){
        				iterator.remove();
        			}
    			}
    		}
    	}
		//this.updateCache();
    	this.updateRuleArray();
	}


	public void setTerminal(long terminal) {
		this.terminal = terminal;
	}

	public DriverNotCard getDriverNotCardRule() {
		return driverNotCardRule;
	}
	public List<DrivingBan> getDrivingBanRule() {
		return drivingBanRule;
	}
	public List<AreaSpeeding> getAreaSpeedingRule() {
		return areaSpeedingRule;
	}
	public List<InOutArea> getInOutAreaRule() {
		return inOutAreaRule;
	}
	public List<RouteDriverTime> getRouteDriverTimeRule() {
		return routeDriverTimeRule;
	}
	public List<DoorOpenOutArea> getDoorOpenOutAreaRule() {
		return doorOpenOutAreaRule;
	}

	public List<OutRegionToLSpeed> getOutRegionToLSpeedRule() {
		return outregionToLSpeed;
	}

	public List<KeyPointFence> getKeyPointFenceRule() {
		return keyPointFenceRule;
	}
	public void setKeyPointFenceRule(List<KeyPointFence> keyPointFenceRule) {
		this.keyPointFenceRule = keyPointFenceRule;
	}
	public List<MessageBroadcast> getMessageBroadcast() {
		return messageBroadcast;
	}
	public void setMessageBroadcast(List<MessageBroadcast> messageBroadcast) {
		this.messageBroadcast = messageBroadcast;
	}
	public long getAreaIdentify() {
		return areaIdentify;
	}
	public void setAreaIdentify(long areaIdentify) {
		this.areaIdentify = areaIdentify;
	}
	public List<OverTimePark> getOvertimePark() {
		return overtimePark;
	}
	public void setOvertimePark(List<OverTimePark> overtimePark) {
		this.overtimePark = overtimePark;
	}
	public List<OutRegionToLSpeed> getOutregionToLSpeed() {
		return outregionToLSpeed;
	}
	public void setOutregionToLSpeed(List<OutRegionToLSpeed> outregionToLSpeed) {
		this.outregionToLSpeed = outregionToLSpeed;
	}
	public RegularCode getRegularCode() {
		return regularCode;
	}
	public void setRegularCode(RegularCode regularCode) {
		this.regularCode = regularCode;
	}
	public RegularType getType() {
		return type;
	}
	public void setType(RegularType type) {
		this.type = type;
	}
	public TerminalMessageSwitch getTerminalMessage() {
		return terminalMessage;
	}
	public void setTerminalMessage(TerminalMessageSwitch terminalMessage) {
		this.terminalMessage = terminalMessage;
	}
	public DelayOvertimePark getDelayPark() {
		return delayPark;
	}
	public void setDelayPark(DelayOvertimePark delayPark) {
		this.delayPark = delayPark;
	}
	public List<InAreaTriggerActivationOrLock> getInAreaTriggerActivationOrLockRule() {
		return inAreaTriggerActivationOrLockRule;
	}

//	/**
//	 * 更新缓存
//	 */
//	private void updateCache()
//	{
//		// 如果是通用的，则更新通用的缓存
//		if (ruleCommonCache.isExists(this))
//		{
//			ruleCommonCache.addRuleEntity(this);
//		}
//		// 如果是专用的，则更新专用缓存
//		if (ruleCache.isExists(this))
//		{
//			ruleCache.addRuleEntity(this);
//		}
//	}

	public AlarmCancelOrNot getAlarmCancelOrNot() {
		return alarmCancelOrNot;
	}
	public void setAlarmCancelOrNot(AlarmCancelOrNot alarmCancelOrNot) {
		this.alarmCancelOrNot = alarmCancelOrNot;
		this.updateRuleArray();
	}
	public List<InOrOutAreaNotifySetPara> getSetPara() {
		return setPara;
	}
	public void setSetPara(List<InOrOutAreaNotifySetPara> setPara) {
		this.setPara = setPara;
	}
	public boolean isAreaStatusNotifySwitch() {
		return areaStatusNotifySwitch;
	}
	public void setAreaStatusNotifySwitch(boolean areaStatusNotifySwitch) {
		this.areaStatusNotifySwitch = areaStatusNotifySwitch;
	}

	public void setInOrOutAreaNotifySetParaRule(InOrOutAreaNotifySetPara setPara){
		if(this.setPara == null){
			this.setPara = new ArrayList<InOrOutAreaNotifySetPara>();
		}
		Iterator<InOrOutAreaNotifySetPara> iterator = this.setPara.iterator();
		while(iterator.hasNext()){
			if(iterator.next().getAreaIdentify() == setPara.getAreaIdentify())
				iterator.remove();
		}
		this.setPara.add(setPara);
		//this.updateCache();
		this.updateRuleArray();
	}

	/**
	 * 删除停滞超时报警撤销与恢复规则
	 */
	public void removeAlarmCancelOrNot(){
		this.alarmCancelOrNot = null;
		this.updateRuleArray();
	}
}
