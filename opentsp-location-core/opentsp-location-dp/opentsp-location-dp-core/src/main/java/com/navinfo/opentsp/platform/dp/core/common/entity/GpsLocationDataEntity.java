package com.navinfo.opentsp.platform.dp.core.common.entity;

import com.google.protobuf.ByteString;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.protocol.common.*;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCDrivingBehaviorAnalysis.DrivingBehaviorAnalysis;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.VehicleBreakdownAddition;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.VehicleStatusAddition;
import com.navinfo.opentsp.platform.location.protocol.common.LCShortLocationData.ShortLocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleBreakdown.VehicleBreakdown;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData.VehicleStatusData;
import com.navinfo.opentsp.platform.location.protocol.correct.LCDataEncryptRes.DataEncryptRes;
import com.navinfo.opentsp.platform.location.protocol.common.LCTireTemperatureAddition.TireTemperatureAddition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author
 * 	1、GpsLocationDataEntity 是转化位置数据 LocationData protobuf
 *
 * 针对大运项目，如下：
 * 	1、com.lc.dp.common.entity 目录下：
 * 		GpsOvertimeParkingAddition   <-->  parkingAddition	  区域滞留超时附加信息
 * 		GpsVehicleBreakdown          <-->  breakdownAddition	车辆故障附加信息
 * 		GpsVehicleStatusAddition     <--> statusAddition	车辆状态附加信息
 * 			GpsVehicleStatusData     <-->  VehicleStatusData 车辆状态数据
 * 	2、dp收到ta位置数据（3002协议）时，需要在GpsLocationDataEntity的构造方法中，转化数据，其中需要转化的附加信息主要是：车辆状态附加信息。（故障信息暂时没有，故还未做）
 */
public class GpsLocationDataEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private int serialNumber;
	private String uniqueMark;
	private long terminalId;
	private long alarm;
	private long status;
	private int longitude;
	private int latitude;
	private int originalLng;
	private int originalLat;
	private int height;
	private int speed;
	private int direction;
	private long gpsDate;
	private long mileage;
	private long receiveDate;
	private boolean isPatch;

	private float standardMileage;
	private  float standardFuelCon;



	private int oil;
	private int recorderSpeed;
	private int signalStatus;//扩展车辆信号状态
	private List<SpeedAddition> speedAdditions;
	private List<AreaAddition> areaAdditions;
	private List<RouteAddition> routeAdditions;
	private List<TemAddition> temAdditions;
	private List<KeyPointFenceAddition> defenceAdditions;
	private List<OvertimeParkingAddition> parkingAddition;
	private List<StaytimeParkingAddition> staytimeParkingAddition;
	private GpsVehicleStatusAddition	gpsVehicleStatusAddition;
	private GpsVehicleBreakdownAddition gpsVehicleBreakdownAddition;

	private TireTemperatureAddition tireTemperatureAddition;

	private int starStatus;
	private int starNumber;
	private LocationData locationData = null;//protobuf
	private ShortLocationData shortLocationData = null;//protobuf
	private long createTime = System.currentTimeMillis()/1000;
	private int commandId ;
	private byte[] additionAlarm;
	private DrivingBehaviorAnalysis analysisData;
	//电动车
	private int batteryPower;
	private List<ModuleVoltage> moduleVoltages = null;
	private long electricVehicle;
	private BatteryVehicleInfo batteryVehicleInfo = null;

	/**
	 * 过滤算法处理时间
	 */
	private long processFilterTime;

	/**
	 * 规则处理时间
	 */
	private long ruleProcessTime;

	private long ruleSignTime;

	private long encryptTime;

	private long forwardTime;

	public long getEcuTime() {
		return ecuTime;
	}

	public void setEcuTime(long ecuTime) {
		this.ecuTime = ecuTime;
	}

	/**
	 * 0705数据时间
	 */
	private long ecuTime;


	public long getRuleSignTime() {
		return ruleSignTime;
	}

	public void setRuleSignTime(long ruleSignTime) {
		this.ruleSignTime = ruleSignTime;
	}

	public long getEncryptTime() {
		return encryptTime;
	}

	public void setEncryptTime(long encryptTime) {
		this.encryptTime = encryptTime;
	}

	public long getForwardTime() {
		return forwardTime;
	}

	public void setForwardTime(long forwardTime) {
		this.forwardTime = forwardTime;
	}

	public long getRuleProcessTime() {
		return ruleProcessTime;
	}

	public void setRuleProcessTime(long ruleProcessTime) {
		this.ruleProcessTime = ruleProcessTime;
	}

	public long getProcessFilterTime() {
		return processFilterTime;
	}

	public void setProcessFilterTime(long processFilterTime) {
		this.processFilterTime = processFilterTime;
	}

	public float getStandardMileage() {
		return standardMileage;
	}

	public void setStandardMileage(float standardMileage) {
		this.standardMileage = standardMileage;
	}

	public float getStandardFuelCon() {
		return standardFuelCon;
	}

	public void setStandardFuelCon(float standardFuelCon) {
		this.standardFuelCon = standardFuelCon;
	}

	public TireTemperatureAddition getTireTemperatureAddition() {
		return tireTemperatureAddition;
	}

	public void setTireTemperatureAddition(TireTemperatureAddition tireTemperatureAddition) {
		this.tireTemperatureAddition = tireTemperatureAddition;
	}

	/**
	 * 每个规则链处理详细时间,<规则ID,处理时间ms>
	 */
	private Map<String,Long> detailMap;

	public Map<String, Long> getDetailMap() {
		if(detailMap == null) {
			detailMap = new HashMap<>();
		}
		return detailMap;
	}

	public void setDetailMap(Map<String, Long> detailMap) {
		this.detailMap = detailMap;
	}

	public GpsLocationDataEntity() {
		super();
	}

	/**
	 *
	 * @param locationData : 原始protobuf位置数据
	 * @param uniqueMark ： 唯一标示
	 * @param serialNumber ： 流水号
	 * @param commandId ： 协议号
	 *
	 * 说明： 在此构造方法中转化的附加信息是：statusAddition	车辆状态附加信息 。 （故障信息暂时没有，故还未做）
	 *
	 */
	public GpsLocationDataEntity(LocationData locationData, String uniqueMark, int serialNumber , int commandId) {
		this.commandId = commandId;
		this.setTerminalId(Convert.uniqueMarkToLong(uniqueMark));
		this.setUniqueMark(uniqueMark);
		this.setSerialNumber(serialNumber);
		this.setAlarm(locationData.getAlarm());
		this.setStatus(locationData.getStatus());
		this.setLongitude(locationData.getLongitude());
		this.setLatitude(locationData.getLatitude());
		this.setOriginalLat(locationData.getOriginalLat());
		this.setOriginalLng(locationData.getOriginalLng());
		this.setHeight(locationData.getHeight());
		this.setSpeed(locationData.getSpeed());
		this.setDirection(locationData.getDirection());
		this.setGpsDate(locationData.getGpsDate());
		this.setReceiveDate(locationData.getReceiveDate());
		this.setMileage(locationData.getMileage());
		this.setPatch(locationData.getIsPatch());
		this.setOil(locationData.getOil());
		this.setRecorderSpeed(locationData.getRecorderSpeed());
		this.setStarStatus(locationData.getStarStatus());
		this.setStarNumber(locationData.getStarNumber());
		this.setAdditionAlarm(locationData.getAdditionAlarm().toByteArray());
		this.setAnalysisData(locationData.getAnalysisData());
		this.setSignalStatus(locationData.getSignalStatus());
		this.setBatteryPower(locationData.getBatteryPower());
		this.setElectricVehicle(locationData.getElectricVehicle()); //充电状态
		this.setStandardFuelCon(locationData.getStandardFuelCon());
		this.setStandardMileage(locationData.getStandardMileage());
		this.setEcuTime(locationData.getEcuDate());
		this.setTireTemperatureAddition(locationData.getTemperatureAddition());

		//模块电压
		if(locationData.getModuleVoltagesList()!=null && locationData.getModuleVoltagesList().size()>0){
			List<com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.ModuleVoltage> list = locationData.getModuleVoltagesList();
			this.moduleVoltages = new ArrayList<ModuleVoltage>();
			ModuleVoltage moduleVoltage = null;
			for(int i=0;i<list.size();i++){
				com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.ModuleVoltage e = list.get(i);
				moduleVoltage = new ModuleVoltage();
				moduleVoltage.setModuleNum(e.getModuleNum());
				moduleVoltage.setNumber(e.getNumber());
				moduleVoltage.setVoltage(e.getVoltage());
				this.moduleVoltages.add(moduleVoltage);
			}
		}

		//电动车状态信息
		com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.BatteryVehicleInfo bVehicleInfo = locationData.getBatteryInfo();
		if(bVehicleInfo!=null){
			this.batteryVehicleInfo = new BatteryVehicleInfo();
			this.batteryVehicleInfo.setMotorTemperature(bVehicleInfo.getMotorTemperature());
			this.batteryVehicleInfo.setMotorControlTemp(bVehicleInfo.getMotorControlTemp());
			this.batteryVehicleInfo.setMcuFaultCode(bVehicleInfo.getMcuFaultCode());
			this.batteryVehicleInfo.setBmsStatus(bVehicleInfo.getBmsStatus());
			this.batteryVehicleInfo.setBatteryAveTem(bVehicleInfo.getBatteryAveTem());
			this.batteryVehicleInfo.setBatteryCurrent(bVehicleInfo.getBatteryCurrent());
			this.batteryVehicleInfo.setTotalBatteryV(bVehicleInfo.getTotalBatteryV());
			this.batteryVehicleInfo.setBatteryFaultCode(bVehicleInfo.getBatteryFaultCode());
			this.batteryVehicleInfo.setBatteryMaxTem(bVehicleInfo.getBatteryMaxTem());
			this.batteryVehicleInfo.setBatteryMinTem(bVehicleInfo.getBatteryMinTem());
			this.batteryVehicleInfo.setBatteryMinSoc(bVehicleInfo.getBatteryMinSoc());
			this.batteryVehicleInfo.setMaxChargingV(bVehicleInfo.getMaxChargingV());
			this.batteryVehicleInfo.setMaxChargingE(bVehicleInfo.getMaxChargingE());
			this.batteryVehicleInfo.setTotalStatus(bVehicleInfo.getTotalStatus());
			this.batteryVehicleInfo.setVehicleSpeed(bVehicleInfo.getVehicleSpeed());
			this.batteryVehicleInfo.setShiftStatus(bVehicleInfo.getShiftStatus());
			this.batteryVehicleInfo.setVcuFaultCode(bVehicleInfo.getVcuFaultCode());
			this.batteryVehicleInfo.setEnduranceMileage(bVehicleInfo.getEnduranceMileage());
			this.batteryVehicleInfo.setAcceleratorPedal(bVehicleInfo.getAcceleratorPedal());
			this.batteryVehicleInfo.setBrakePedal(bVehicleInfo.getBrakePedal());
			this.batteryVehicleInfo.setModelInfo(bVehicleInfo.getModelInfo());
			this.batteryVehicleInfo.setSwitchInfo(bVehicleInfo.getSwitchInfo());
			this.batteryVehicleInfo.setElectricAttachment(bVehicleInfo.getElectricAttachment());
			this.batteryVehicleInfo.setMotorRpm(bVehicleInfo.getMotorRpm());
		}


	    /**
	     * GpsVehicleStatusAddition  <--> statusAddition	车辆状态附加信息
	     * GpsVehicleStatusData  <--> VehicleStatusData 车辆状态数据
	     *
	     *  protobuf中对应关系：
	     *  	LocationData ： optional	  VehicleStatusAddition	 statusAddition
	     * 		VehicleStatusAddition ： repeated	  VehicleStatusData	status
	     *
	     *  保持和protobuf 一致对应，关系如下：
	     * 		GpsLocationDataEntity.set(GpsVehicleStatusAddition)
	     * 		GpsVehicleStatusAddition.set(List<GpsVehicleStatusData>)
	     */
		VehicleStatusAddition vehicleStatusAddition = locationData.getStatusAddition();//获取protobuf，车辆状态附加信息
		if(null != vehicleStatusAddition && vehicleStatusAddition.getStatusList().size() > 0){//存在附加信息时候，注：此附加信息是对大运项目的
			this.gpsVehicleStatusAddition = new GpsVehicleStatusAddition(new ArrayList<GpsVehicleStatusData>());
			GpsVehicleStatusData gpsVehicleStatusData = null;
			for(VehicleStatusData vehicleStatusData:vehicleStatusAddition.getStatusList()){
				gpsVehicleStatusData = new GpsVehicleStatusData();
				gpsVehicleStatusData.setStatusValue(vehicleStatusData.getStatusValue());
				gpsVehicleStatusData.setTypes(vehicleStatusData.getTypes());
				this.gpsVehicleStatusAddition.getStatus().add(gpsVehicleStatusData);
			}
			/*
			//定义List<GpsVehicleStatusData>，
			List<GpsVehicleStatusData> statusList = new ArrayList<GpsVehicleStatusData>();

			//从protobuf取出VehicleStatusData 数据，生成List<GpsVehicleStatusData>
			GpsVehicleStatusData gpsVehicleStatusData = null;
			for(VehicleStatusData vehicleStatusData:vehicleStatusAddition.getStatusList()){
				gpsVehicleStatusData = new GpsVehicleStatusData();
				gpsVehicleStatusData.setStatusValue(vehicleStatusData.getStatusValue());
				gpsVehicleStatusData.setTypes(vehicleStatusData.getTypes());
				statusList.add(gpsVehicleStatusData);
			}
			//给GpsVehicleStatusData gpsVehicleStatusAddition属性 赋值
			if(this.gpsVehicleStatusAddition==null){
				this.gpsVehicleStatusAddition = new GpsVehicleStatusAddition();
			}
			this.gpsVehicleStatusAddition.setStatus(statusList);
			statusList=null;//将statusList 置空，回收
			*/
		}

		/**
		 * 处理故障信息，处理逻辑同车辆状态附加信息
		 */
		VehicleBreakdownAddition breakdownAddition = locationData.getBreakdownAddition();
		if(null != breakdownAddition && breakdownAddition.getBreakdownList().size() > 0){
			this.gpsVehicleBreakdownAddition = new GpsVehicleBreakdownAddition(new ArrayList<GpsVehicleBreakdown>());
			GpsVehicleBreakdown gpsVehicleBreakdown = null;
			for(VehicleBreakdown breakdown:breakdownAddition.getBreakdownList()){
				gpsVehicleBreakdown = new GpsVehicleBreakdown();
				gpsVehicleBreakdown.setBreakdownSPNValue(breakdown.getBreakdownSPNValue());
				gpsVehicleBreakdown.setBreakdownFMIValue(breakdown.getBreakdownFMIValue());
				this.gpsVehicleBreakdownAddition.getBreakdowns().add(gpsVehicleBreakdown);
			}
			/*

			List<GpsVehicleBreakdown> breakdownList = new ArrayList<GpsVehicleBreakdown>();
			GpsVehicleBreakdown gpsVehicleBreakdown = null;
			for(VehicleBreakdown breakdown:breakdownAddition.getBreakdownList()){
				gpsVehicleBreakdown = new GpsVehicleBreakdown();
				gpsVehicleBreakdown.setType(breakdown.getTypes().ordinal());
				gpsVehicleBreakdown.setValue(breakdown.getBreakdownValue());
				breakdownList.add(gpsVehicleBreakdown);
			}
			if(this.gpsVehicleBreakdownAddition==null){
				this.gpsVehicleBreakdownAddition = new GpsVehicleBreakdownAddition();
			}
			this.gpsVehicleBreakdownAddition.setBreakdowns(breakdownList);
			breakdownList = null;
			*/
		}

	}

	public int getCommandId() {
		return commandId;
	}

	public void setCommandId(int commandId) {
		this.commandId = commandId;
	}

	public GpsLocationDataEntity(LocationData locationData,
								 DataEncryptRes dataEncryptRes, String uniqueMark, int serialNumber , int commandId) {
		this(locationData, uniqueMark, serialNumber,commandId);
		this.setLatitude(dataEncryptRes.getLatitude());
		this.setLongitude(dataEncryptRes.getLongitude());
		this.setTerminalId(Convert.uniqueMarkToLong(uniqueMark));
	}

	/**
	 * 转精简Gps数据对象
	 *
	 * @param isMustCreate
	 *            {@link Boolean} 是否需要创建对象
	 *
	 * @return {@link ShortLocationData}
	 */
	public ShortLocationData toShortLocationData(boolean isMustCreate) {
		if (isMustCreate) {
			return this.toShortLocationData();
		} else {
			if (this.shortLocationData == null) {
				this.toShortLocationData();
			}
			return this.shortLocationData;
		}
	}

	/**
	 * 创建精简Gps数据对象
	 *
	 * @return {@link ShortLocationData}
	 */
	private ShortLocationData toShortLocationData() {
		ShortLocationData.Builder builder = ShortLocationData.newBuilder();
		builder.setDirection(this.getDirection());
		builder.setGpsDate(this.getGpsDate());
		builder.setHeight(this.getHeight());
		builder.setIsPatch(this.isPatch());
		builder.setLatitude(this.getLatitude());
		builder.setLongitude(this.getLongitude());
		builder.setMileage(this.getMileage());
		builder.setOriginalLat(this.getOriginalLat());
		builder.setOriginalLng(this.getOriginalLng());
		builder.setReceiveDate(this.getReceiveDate());
		builder.setSpeed(this.getSpeed());
		this.shortLocationData = builder.build();
		return this.shortLocationData;
	}
	/**
	 * 转完整Gps数据对象
	 *
	 * @param isMustCreate
	 *            {@link Boolean} 是否需要创建对象
	 *
	 * @return {@link LocationData}
	 */
	public LocationData toLocationData(boolean isMustCreate) {
		if (isMustCreate) {
			return this.toLocationData();
		} else {
			if (this.locationData == null) {
				this.toLocationData();
			}
			return this.locationData;
		}
	}
	/**
	 * 创建完整Gps数据对象
	 *
	 * @return {@link LocationData}
	 */
	private LocationData toLocationData() {
		LocationData.Builder builder = LocationData.newBuilder();
		builder.setAlarm(this.getAlarm());
		builder.setStatus(this.getStatus());
		builder.setDirection(this.getDirection());
		builder.setGpsDate(this.getGpsDate());
		builder.setHeight(this.getHeight());
		builder.setIsPatch(this.isPatch());
		builder.setLatitude(this.getLatitude());
		builder.setLongitude(this.getLongitude());
		builder.setMileage(this.getMileage());
		builder.setOil(this.getOil());
		builder.setOriginalLat(this.getOriginalLat());
		builder.setOriginalLng(this.getOriginalLng());
		builder.setReceiveDate(this.getReceiveDate());
		builder.setRecorderSpeed(this.getRecorderSpeed());
		builder.setSpeed(this.getSpeed());
		builder.setStarNumber(this.getStarNumber());
		builder.setStarStatus(this.getStarStatus());
		builder.setAdditionAlarm(ByteString.copyFrom(this.getAdditionAlarm()));
		builder.setSignalStatus(this.getSignalStatus());
		builder.setBatteryPower(this.getBatteryPower());
		builder.setElectricVehicle(this.getElectricVehicle());
		builder.setStandardFuelCon(this.getStandardFuelCon());
		builder.setStandardMileage(this.getStandardMileage());
		builder.setEcuDate(this.getEcuTime());
		builder.setTemperatureAddition(this.getTireTemperatureAddition());
		//模块电压
		if(this.moduleVoltages!=null){  
			for(ModuleVoltage e : this.moduleVoltages){
				LCLocationData.ModuleVoltage.Builder mBuilder = LCLocationData.ModuleVoltage.newBuilder();
				mBuilder.setModuleNum(e.getModuleNum());
				mBuilder.setNumber(e.getNumber());
				mBuilder.setVoltage(e.getVoltage());
				builder.addModuleVoltages(mBuilder);
			}
		}
		//电动车状态信息
		if(this.batteryVehicleInfo!=null){
			LCLocationData.BatteryVehicleInfo.Builder bvBuilder = LCLocationData.BatteryVehicleInfo.newBuilder();
			bvBuilder.setMotorTemperature(this.batteryVehicleInfo.getMotorTemperature());
			bvBuilder.setMotorControlTemp(this.batteryVehicleInfo.getMotorControlTemp());
			bvBuilder.setMcuFaultCode(this.batteryVehicleInfo.getMcuFaultCode());
			bvBuilder.setBmsStatus(this.batteryVehicleInfo.getBmsStatus());
			bvBuilder.setBatteryAveTem(this.batteryVehicleInfo.getBatteryAveTem());
			bvBuilder.setBatteryCurrent(this.batteryVehicleInfo.getBatteryCurrent());
			bvBuilder.setTotalBatteryV(this.batteryVehicleInfo.getTotalBatteryV());
			bvBuilder.setBatteryFaultCode(this.batteryVehicleInfo.getBatteryFaultCode());
			bvBuilder.setBatteryMaxTem(this.batteryVehicleInfo.getBatteryMaxTem());
			bvBuilder.setBatteryMinTem(this.batteryVehicleInfo.getBatteryMinTem());
			bvBuilder.setBatteryMinSoc(this.batteryVehicleInfo.getBatteryMinSoc());
			bvBuilder.setMaxChargingV(this.batteryVehicleInfo.getMaxChargingV());
			bvBuilder.setMaxChargingE(this.batteryVehicleInfo.getMaxChargingE());
			bvBuilder.setTotalStatus(this.batteryVehicleInfo.getTotalStatus());
			bvBuilder.setVehicleSpeed(this.batteryVehicleInfo.getVehicleSpeed());
			bvBuilder.setShiftStatus(this.batteryVehicleInfo.getShiftStatus());
			bvBuilder.setVcuFaultCode(this.batteryVehicleInfo.getVcuFaultCode());
			bvBuilder.setEnduranceMileage(this.batteryVehicleInfo.getEnduranceMileage());
			bvBuilder.setAcceleratorPedal(this.batteryVehicleInfo.getAcceleratorPedal());
			bvBuilder.setBrakePedal(this.batteryVehicleInfo.getBrakePedal());
			bvBuilder.setModelInfo(this.batteryVehicleInfo.getModelInfo());
			bvBuilder.setSwitchInfo(this.batteryVehicleInfo.getSwitchInfo());
			bvBuilder.setElectricAttachment(this.batteryVehicleInfo.getElectricAttachment());
			bvBuilder.setMotorRpm(this.batteryVehicleInfo.getMotorRpm());
			builder.setBatteryInfo(bvBuilder);
		}
		if(analysisData != null) {
			builder.setAnalysisData(this.getAnalysisData());
		}
		
		if (this.speedAdditions != null) {
			for (SpeedAddition e : this.speedAdditions) {
				LCLocationData.SpeedAddition.Builder speedBuilder = LCLocationData.SpeedAddition
						.newBuilder();
				speedBuilder.setAreaId(e.getAeraId());
				speedBuilder.setTypes(AreaType.valueOf(e.getAreaType()));
				if(e.getAreaType() == AreaType.segment_VALUE)
					speedBuilder.setLineId(e.getLineId());
				builder.addSpeedAddition(speedBuilder);
			}
		}
		if (this.areaAdditions != null) {
			for (AreaAddition e : this.areaAdditions) {
				LCLocationData.AreaAddition.Builder areaBuilder = LCLocationData.AreaAddition
						.newBuilder();
				areaBuilder.setAreaId(e.getAreaId());
				areaBuilder.setAreaType(AreaType.valueOf(e.getAreaType()));
				areaBuilder.setDirection(e.isDirection());
				builder.addAreaAddition(areaBuilder);
			}
		}
		if (this.routeAdditions != null) {
			for (RouteAddition e : this.routeAdditions) {
				LCLocationData.RouteAddition.Builder routeBuilder = LCLocationData.RouteAddition
						.newBuilder();
				routeBuilder.setAreaId(e.getAreaId());
				routeBuilder.setRouteTime(e.getRouteTime());
				routeBuilder.setRouteResult(e.isRouteResult());
				routeBuilder.setLineId(e.getLineId());
				builder.addRouteAddition(routeBuilder);
			}
		}
		if (this.temAdditions != null) {
			for (TemAddition e : this.temAdditions) {
				LCLocationData.TemAddition.Builder tempBuilder = LCLocationData.TemAddition
						.newBuilder();
				tempBuilder.setChannelId(e.getChannelId());
				tempBuilder.setTemAlarm(e.isTemAlarm());
				tempBuilder.setTemperature(e.getTemperature());
			}
		}
		if(this.defenceAdditions != null){
			for(KeyPointFenceAddition e : this.defenceAdditions){
				LCLocationData.KeyPointFenceAddition.Builder fenceBuilder = LCLocationData.KeyPointFenceAddition.newBuilder();
				fenceBuilder.setAreaId(e.getAreaId());
				fenceBuilder.setAreaType(AreaType.valueOf(e.getAreaType()));
				fenceBuilder.setFenceAlarm(e.isFenceAlarm());
				builder.addDefenceAddition(fenceBuilder);
			}
		}
		if(this.parkingAddition != null){
			for(OvertimeParkingAddition e : this.parkingAddition){
				LCLocationData.OvertimeParkingAddition.Builder overBuilder = LCLocationData.OvertimeParkingAddition.newBuilder();
				overBuilder.setAreaId(e.getAreaId());
				overBuilder.setAreaType(AreaType.valueOf(e.getAreaType()));
				builder.addParkingAddition(overBuilder);
			}
		}
		if(this.staytimeParkingAddition != null){
			for(StaytimeParkingAddition e : this.staytimeParkingAddition){
				LCLocationData.StaytimeParkingAddition.Builder overBuilder = LCLocationData.StaytimeParkingAddition.newBuilder();
				overBuilder.setAreaId(e.getAreaId());
				overBuilder.setAreaType(AreaType.valueOf(e.getAreaType()));
				builder.addStaytimeParkingAddition(overBuilder);
			}
		}
		if(this.gpsVehicleStatusAddition!=null){
			LCLocationData.VehicleStatusAddition.Builder statusBuilder = LCLocationData.VehicleStatusAddition.newBuilder();
			List<GpsVehicleStatusData> list = this.gpsVehicleStatusAddition.getStatus();
			for(GpsVehicleStatusData gpsVehicleStatusData : list){
				LCVehicleStatusData.VehicleStatusData.Builder dataBuilder =  LCVehicleStatusData.VehicleStatusData.newBuilder();
				dataBuilder.setStatusValue(gpsVehicleStatusData.getStatusValue());
				dataBuilder.setTypes(gpsVehicleStatusData.getTypes());
				statusBuilder.addStatus(dataBuilder);
			}
			builder.setStatusAddition(statusBuilder.build());
			/*if(list !=null){
				for(GpsVehicleStatusData gpsVehicleStatusData : list){
					LCVehicleStatusData.VehicleStatusData.Builder dataBuilder =  LCVehicleStatusData.VehicleStatusData.newBuilder();
					dataBuilder.setStatusValue(gpsVehicleStatusData.getStatusValue());
					dataBuilder.setTypes(gpsVehicleStatusData.getTypes());
					statusBuilder.addStatus(dataBuilder);
				}
				builder.setStatusAddition(statusBuilder.build());
			}*/
		}
		
		if(this.gpsVehicleBreakdownAddition!=null){
			LCLocationData.VehicleBreakdownAddition.Builder breakdownBuilder = LCLocationData.VehicleBreakdownAddition.newBuilder();
			LCVehicleBreakdown.VehicleBreakdown.Builder vehicleBreakdown = null;
			List<GpsVehicleBreakdown> list = this.gpsVehicleBreakdownAddition.getBreakdowns();
			for(GpsVehicleBreakdown gpsVehicleBreakdown: list){
				vehicleBreakdown = LCVehicleBreakdown.VehicleBreakdown.newBuilder();
				vehicleBreakdown.setBreakdownSPNValue(gpsVehicleBreakdown.getBreakdownSPNValue());
				vehicleBreakdown.setBreakdownFMIValue(gpsVehicleBreakdown.getBreakdownFMIValue());
				breakdownBuilder.addBreakdown(vehicleBreakdown);
			}
			builder.setBreakdownAddition(breakdownBuilder);
		}
		this.locationData = builder.build();
		return this.locationData;
	}

	/**
	 * 更新报警
	 * 
	 * @param index
	 *            {@link Integer} 高低位,1：低3位,2：高3位
	 * @param alarm
	 * @return {@link GpsLocationDataEntity}
	 */
	public GpsLocationDataEntity updateAlarm(int index, long alarm) {
		if (index == 2) {
			long temp = alarm << 24;
			this.alarm = this.alarm | temp;
		} else if (index == 1) {
			this.alarm = this.alarm | alarm;
		}
		return this;
	}

	public static void main(String[] args) {
		GpsLocationDataEntity dataEntity = new GpsLocationDataEntity();
		dataEntity.updateAlarm(2, 256);
	}

	/**
	 * 超速报警
	 * 
	 * @param areaType
	 * @param aeraId
	 * @param lineId {@link Long} 路段ID,如果区域类型不为路线,值设置为0
	 */
	public void addSpeedAddition(int areaType, long aeraId , long lineId) {
		if (this.speedAdditions == null) {
			this.speedAdditions = new ArrayList<SpeedAddition>();
		}
		this.speedAdditions.add(new SpeedAddition(areaType, aeraId , lineId));
		this.alarm = this.alarm | LCAllAlarm.Alarm.speedingAlarm_VALUE;
	}

	/**
	 * 进出区域或者进出路线
	 *
	 * @param areaType
	 * @param areaId
	 * @param direction
	 */
	public void addAreaAddition(int areaType, long areaId, boolean direction) {
		if (this.areaAdditions == null)
			this.areaAdditions = new ArrayList<AreaAddition>();
		this.areaAdditions.add(new AreaAddition(areaType, areaId, direction));
		if(areaType == AreaType.route_VALUE){
			this.alarm = this.alarm | LCAllAlarm.Alarm.inOutRoute_VALUE;
		}else{
			this.alarm = this.alarm | LCAllAlarm.Alarm.inOutArea_VALUE;
		}
	}

	/**
	 * 偏离路线标识和附加信息处理
	 * @param areaType
	 * @param areaId
	 * @param flag true 添加附加信息  false 不添加附加信息
	 */
	public void addDeviateAddition(int areaType, long areaId, boolean flag) {
		this.alarm = this.alarm | LCAllAlarm.Alarm.routeDeviates_VALUE;
		if(flag){
			if (this.areaAdditions == null){
				this.areaAdditions = new ArrayList<AreaAddition>();
			}
			this.areaAdditions.add(new AreaAddition(areaType, areaId, false));
		}
	}

	public DrivingBehaviorAnalysis getAnalysisData() {
		return analysisData;
	}

	public void setAnalysisData(DrivingBehaviorAnalysis analysisData) {
		this.analysisData = analysisData;
	}

	/**
	 * 路段行驶时间不足/过长
	 *
	 * @param areaId
	 * @param routeTime
	 * @param routeResult
	 * @param lineId {@link Integer}路段ID
	 */
	public void addRouteAddition(long areaId, int routeTime, boolean routeResult , long lineId) {
		if (this.routeAdditions == null)
			this.routeAdditions = new ArrayList<RouteAddition>();
		this.routeAdditions.add(new RouteAddition(areaId, routeTime,
				routeResult , lineId));
		this.alarm = this.alarm | LCAllAlarm.Alarm.routeLackOrOverTime_VALUE;
	}

	public void addFenceAddition(long areaId, int areaType, boolean notArriveOrLeave){
		if(this.defenceAdditions == null){
			this.defenceAdditions = new ArrayList<KeyPointFenceAddition>();
		}
		this.defenceAdditions.add(new KeyPointFenceAddition(areaType, areaId, notArriveOrLeave));
		this.alarm = this.alarm | ((long)LCAllAlarm.AlarmAddition.keyPointFence_VALUE<<24);
	}

	public void addParkingAddition(long areaId, int areaType){
		if(this.parkingAddition == null){
			this.parkingAddition = new ArrayList<OvertimeParkingAddition>();
		}
		this.parkingAddition.add(new OvertimeParkingAddition(areaType, areaId));
		this.alarm = this.alarm | ((long)LCAllAlarm.AlarmAddition.overtimeParkingInArea_VALUE<<24);//
	}

	public void addStaytimeParkingAddition(long areaId, int areaType){
		if(this.staytimeParkingAddition == null){
			this.staytimeParkingAddition = new ArrayList<StaytimeParkingAddition>();
		}
		this.staytimeParkingAddition.add(new StaytimeParkingAddition(areaType, areaId));
		this.alarm = this.alarm | ((long)LCAllAlarm.AlarmAddition.staytimeParkingInArea_VALUE<<24);//
	}

	public void addAbnormalOilAlarm() {
		this.alarm = this.alarm | ((long)LCAllAlarm.AlarmAddition.abnormalOil_VALUE<<24);//
	}


	/**
	 * 温度报警
	 *
	 * @param channelId
	 * @param temAlarm
	 * @param temperature
	 */
	public void addTemAddition(int channelId, boolean temAlarm, int temperature) {
		if (this.temAdditions == null)
			this.temAdditions = new ArrayList<TemAddition>();
		this.temAdditions
				.add(new TemAddition(channelId, temAlarm, temperature));
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getUniqueMark() {
		return uniqueMark;
	}

	public void setUniqueMark(String uniqueMark) {
		this.uniqueMark = uniqueMark;
	}

	public long getAlarm() {
		return alarm;
	}

	public void setAlarm(long alarm) {
		this.alarm = alarm;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long status) {
		this.status = status;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public int getOriginalLng() {
		return originalLng;
	}

	public void setOriginalLng(int originalLng) {
		this.originalLng = originalLng;
	}

	public int getOriginalLat() {
		return originalLat;
	}

	public void setOriginalLat(int originalLat) {
		this.originalLat = originalLat;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public long getGpsDate() {
		return gpsDate;
	}

	public void setGpsDate(long gpsDate) {
		this.gpsDate = gpsDate;
	}

	public long getMileage() {
		return mileage;
	}

	public void setMileage(long mileage) {
		this.mileage = mileage;
	}

	public long getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(long receiveDate) {
		this.receiveDate = receiveDate;
	}

	public boolean isPatch() {
		return isPatch;
	}

	public void setPatch(boolean isPatch) {
		this.isPatch = isPatch;
	}

	public int getOil() {
		return oil;
	}

	public void setOil(int oil) {
		this.oil = oil;
	}

	public int getRecorderSpeed() {
		return recorderSpeed;
	}

	public void setRecorderSpeed(int recorderSpeed) {
		this.recorderSpeed = recorderSpeed;
	}

	public List<SpeedAddition> getSpeedAdditions() {
		return speedAdditions;
	}

	public void setSpeedAdditions(List<SpeedAddition> speedAdditions) {
		this.speedAdditions = speedAdditions;
	}

	public List<AreaAddition> getAreaAdditions() {
		return areaAdditions;
	}

	public void setAreaAdditions(List<AreaAddition> areaAdditions) {
		this.areaAdditions = areaAdditions;
	}

	public List<RouteAddition> getRouteAdditions() {
		return routeAdditions;
	}

	public int getSignalStatus() {
		return signalStatus;
	}

	public void setSignalStatus(int signalStatus) {
		this.signalStatus = signalStatus;
	}

	public void setRouteAdditions(List<RouteAddition> routeAdditions) {
		this.routeAdditions = routeAdditions;
	}

	public List<TemAddition> getTemAdditions() {
		return temAdditions;
	}

	public void setTemAdditions(List<TemAddition> temAdditions) {
		this.temAdditions = temAdditions;
	}
	
	public List<KeyPointFenceAddition> getDefenceAdditions() {
		return defenceAdditions;
	}

	public void setDefenceAdditions(List<KeyPointFenceAddition> defenceAdditions) {
		this.defenceAdditions = defenceAdditions;
	}
	public List<OvertimeParkingAddition> getParkingAddition() {
		return parkingAddition;
	}

	public void setParkingAddition(List<OvertimeParkingAddition> parkingAddition) {
		this.parkingAddition = parkingAddition;
	}
	public List<StaytimeParkingAddition> getStaytimeParkingAddition() {
		return staytimeParkingAddition;
	}

	public void setStaytimeParkingAddition(List<StaytimeParkingAddition> staytimeParkingAddition) {
		this.staytimeParkingAddition = staytimeParkingAddition;
	}

	public GpsVehicleStatusAddition getGpsVehicleStatusAddition() {
		return gpsVehicleStatusAddition;
	}

	public void setGpsVehicleStatusAddition(
			GpsVehicleStatusAddition gpsVehicleStatusAddition) {
		this.gpsVehicleStatusAddition = gpsVehicleStatusAddition;
	}

	public GpsVehicleBreakdownAddition getGpsVehicleBreakdownAddition() {
		return gpsVehicleBreakdownAddition;
	}

	public void setGpsVehicleBreakdownAddition(
			GpsVehicleBreakdownAddition gpsVehicleBreakdownAddition) {
		this.gpsVehicleBreakdownAddition = gpsVehicleBreakdownAddition;
	}

	public int getStarStatus() {
		return starStatus;
	}

	public void setStarStatus(int starStatus) {
		this.starStatus = starStatus;
	}

	public int getStarNumber() {
		return starNumber;
	}

	public void setStarNumber(int starNumber) {
		this.starNumber = starNumber;
	}

	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getBatteryPower() {
		return batteryPower;
	}

	public void setBatteryPower(int batteryPower) {
		this.batteryPower = batteryPower;
	}

	class SpeedAddition {
		int areaType;
		long aeraId;
		long lineId;

		public SpeedAddition(int areaType, long aeraId , long lineId) {
			this.areaType = areaType;
			this.aeraId = aeraId;
			this.lineId = lineId;
		}

		public long getLineId() {
			return lineId;
		}

		public int getAreaType() {
			return areaType;
		}

		public long getAeraId() {
			return aeraId;
		}

	}

	class AreaAddition {
		int areaType;
		long areaId;
		boolean direction;

		public AreaAddition(int areaType, long areaId, boolean direction) {
			super();
			this.areaType = areaType;
			this.areaId = areaId;
			this.direction = direction;
		}

		public int getAreaType() {
			return areaType;
		}

		public long getAreaId() {
			return areaId;
		}

		public boolean isDirection() {
			return direction;
		}

	}

	class RouteAddition {
		long areaId;
		int routeTime;
		boolean routeResult;
		long lineId;

		public RouteAddition(long areaId, int routeTime, boolean routeResult ,long lineId) {
			this.areaId = areaId;
			this.routeTime = routeTime;
			this.routeResult = routeResult;
			this.lineId = lineId;
		}

		public long getAreaId() {
			return areaId;
		}

		public long getLineId() {
			return lineId;
		}

		public int getRouteTime() {
			return routeTime;
		}

		public boolean isRouteResult() {
			return routeResult;
		}

	}

	class TemAddition {
		int channelId;
		boolean temAlarm;
		int temperature;

		public TemAddition(int channelId, boolean temAlarm, int temperature) {
			super();
			this.channelId = channelId;
			this.temAlarm = temAlarm;
			this.temperature = temperature;
		}

		public int getChannelId() {
			return channelId;
		}

		public boolean isTemAlarm() {
			return temAlarm;
		}

		public int getTemperature() {
			return temperature;
		}

	}
	
	class KeyPointFenceAddition{
		int areaType;
		long areaId;
		boolean fenceAlarm;
		public KeyPointFenceAddition(int areaType, long areaId,
				boolean fenceAlarm) {
			super();
			this.areaType = areaType;
			this.areaId = areaId;
			this.fenceAlarm = fenceAlarm;
		}
		public int getAreaType() {
			return areaType;
		}
		public void setAreaType(int areaType) {
			this.areaType = areaType;
		}
		public long getAreaId() {
			return areaId;
		}
		public void setAreaId(long areaId) {
			this.areaId = areaId;
		}
		public boolean isFenceAlarm() {
			return fenceAlarm;
		}
		public void setFenceAlarm(boolean fenceAlarm) {
			this.fenceAlarm = fenceAlarm;
		}
	}
	
	class OvertimeParkingAddition{
		int areaType;
		long areaId;
		public OvertimeParkingAddition(int areaType, long areaId) {
			super();
			this.areaType = areaType;
			this.areaId = areaId;
		}
		public int getAreaType() {
			return areaType;
		}
		public void setAreaType(int areaType) {
			this.areaType = areaType;
		}
		public long getAreaId() {
			return areaId;
		}
		public void setAreaId(long areaId) {
			this.areaId = areaId;
		}
	}

	class StaytimeParkingAddition{
		int areaType;
		long areaId;
		public StaytimeParkingAddition(int areaType, long areaId) {
			super();
			this.areaType = areaType;
			this.areaId = areaId;
		}
		public int getAreaType() {
			return areaType;
		}
		public void setAreaType(int areaType) {
			this.areaType = areaType;
		}
		public long getAreaId() {
			return areaId;
		}
		public void setAreaId(long areaId) {
			this.areaId = areaId;
		}
	}
	
	public byte[] getAdditionAlarm() {
		return additionAlarm;
	}

	public void setAdditionAlarm(byte[] additionAlarm) {
		this.additionAlarm = additionAlarm;
	}

	public List<ModuleVoltage> getModuleVoltages() {
		return moduleVoltages;
	}

	public void setModuleVoltages(List<ModuleVoltage> moduleVoltages) {
		this.moduleVoltages = moduleVoltages;
	}

	public long getElectricVehicle() {
		return electricVehicle;
	}

	public void setElectricVehicle(long electricVehicle) {
		this.electricVehicle = electricVehicle;
	}

	public BatteryVehicleInfo getBatteryVehicleInfo() {
		return batteryVehicleInfo;
	}

	public void setBatteryVehicleInfo(BatteryVehicleInfo batteryVehicleInfo) {
		this.batteryVehicleInfo = batteryVehicleInfo;
	}
	
}
