package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;


/**
 * 里程能耗计算
 * @author hk
 *
 */
public class Mileages implements Serializable{
	private static final long serialVersionUID = 1L;
	private long terminalID;	//终端标识
	private long canMileage;	//行驶里程数，单位米
	private int staticDate;	//统计时长, 单位秒
	private float oilConsumption;	//燃油消耗，%
	private float electricConsumption;	//电量消耗，%
	private long beginMileage;	//开始can里程，单位米
	private long endMileage;	//结束can里程，单位米
	private long startDate;	//开始时间,UTC, 单位秒
	private long endDate;	//结束时间,UTC, 单位秒
	private int beginLat;	//开始纬度
	private int beginLng;	//开始经度
	private int endLat;	//结束纬度
	private int endLng;	//结束经度

	private long beginGpsMileage;//开始gps里程
	private long endGpsMileage;//结束gps里程
	private long gpsMileage;//终端gps计算行驶里程

	//辅助计算数据,不参与存储
	private float preOil;	//上次燃油值 L
	private float preElectric;	//上次电量值，%
	//private long preGpsMileages; //单位米
	private long preGpsDate;//上次终端里程时间

	private long beginMeMileage;//开始仪表里程
	private long endMeMileage;//结束仪表里程
	//private long preMeMileage; //上次仪表里程
	private long meterMileage; // 仪表行驶里程统计
	private long preMeDate;//上次仪表里程结束时间

	private float preFuelOil;//上次积分总油耗，%
	private float fuelOil;//积分总油耗，%
	private float preOilValue;//上次剩余油量，%
	private float oilValue;//仪表剩余油量，%

	public float getPreOil() {
		return preOil;
	}
	public void setPreOil(float preOil) {
		this.preOil = preOil;
	}
	public float getPreElectric() {
		return preElectric;
	}
	public void setPreElectric(float preElectric) {
		this.preElectric = preElectric;
	}
	public long getTerminalID() {
		return terminalID;
	}
	public void setTerminalID(long terminalID) {
		this.terminalID = terminalID;
	}
	public long getCanMileage() {
		return canMileage;
	}
	public void setCanMileage(long canMileage) {
		this.canMileage = canMileage;
	}
	public long getGpsMileage() {
		return gpsMileage;
	}
	public void setGpsMileage(long gpsMileage) {
		this.gpsMileage = gpsMileage;
	}
	public int getStaticDate() {
		return staticDate;
	}
	public void setStaticDate(int staticDate) {
		this.staticDate = staticDate;
	}
	public float getOilConsumption() {
		return oilConsumption;
	}
	public void setOilConsumption(float oilConsumption) {
		this.oilConsumption = oilConsumption;
	}
	public float getElectricConsumption() {
		return electricConsumption;
	}
	public void setElectricConsumption(float electricConsumption) {
		this.electricConsumption = electricConsumption;
	}
	public long getBeginMileage() {
		return beginMileage;
	}
	public void setBeginMileage(long beginMileage) {
		this.beginMileage = beginMileage;
	}
	public long getEndMileage() {
		return endMileage;
	}
	public void setEndMileage(long endMileage) {
		this.endMileage = endMileage;
	}
	public long getStartDate() {
		return startDate;
	}
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	public int getBeginLat() {
		return beginLat;
	}
	public void setBeginLat(int beginLat) {
		this.beginLat = beginLat;
	}
	public int getBeginLng() {
		return beginLng;
	}
	public void setBeginLng(int beginLng) {
		this.beginLng = beginLng;
	}
	public int getEndLat() {
		return endLat;
	}
	public void setEndLat(int endLat) {
		this.endLat = endLat;
	}
	public int getEndLng() {
		return endLng;
	}
	public void setEndLng(int endLng) {
		this.endLng = endLng;
	}
	public long getPreGpsDate() {
		return preGpsDate;
	}
	public void setPreGpsDate(long preGpsDate) {
		this.preGpsDate = preGpsDate;
	}
	public long getMeterMileage() {
		return meterMileage;
	}
	public void setMeterMileage(long meterMileage) {
		this.meterMileage = meterMileage;
	}
	public long getPreMeDate() {
		return preMeDate;
	}
	public void setPreMeDate(long preMeDate) {
		this.preMeDate = preMeDate;
	}
	public float getPreFuelOil() {
		return preFuelOil;
	}
	public void setPreFuelOil(float preFuelOil) {
		this.preFuelOil = preFuelOil;
	}
	public float getFuelOil() {
		return fuelOil;
	}
	public void setFuelOil(float fuelOil) {
		this.fuelOil = fuelOil;
	}
	public float getPreOilValue() {
		return preOilValue;
	}
	public void setPreOilValue(float preOilValue) {
		this.preOilValue = preOilValue;
	}
	public float getOilValue() {
		return oilValue;
	}
	public void setOilValue(float oilValue) {
		this.oilValue = oilValue;
	}

	public long getBeginGpsMileage() {
		return beginGpsMileage;
	}

	public void setBeginGpsMileage(long beginGpsMileage) {
		this.beginGpsMileage = beginGpsMileage;
	}

	public long getEndGpsMileage() {
		return endGpsMileage;
	}

	public void setEndGpsMileage(long endGpsMileage) {
		this.endGpsMileage = endGpsMileage;
	}

	public long getBeginMeMileage() {
		return beginMeMileage;
	}

	public void setBeginMeMileage(long beginMeMileage) {
		this.beginMeMileage = beginMeMileage;
	}

	public long getEndMeMileage() {
		return endMeMileage;
	}

	public void setEndMeMileage(long endMeMileage) {
		this.endMeMileage = endMeMileage;
	}
}
