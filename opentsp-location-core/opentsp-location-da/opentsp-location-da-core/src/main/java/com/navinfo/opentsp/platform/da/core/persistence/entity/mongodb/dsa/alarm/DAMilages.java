package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 里程统计信息
 *
 * @author hws
 *
 */
public class DAMilages extends BaseAlarmEntity {
	private long terminalId;
	private long beginMileage;// 开始里程
	private long endMileage;// 结束里程
	private long canMileage;// 里程数
	// private long beginDate;// 开始时间
	private long endDate;// 结束时间
	private int beginLat;// 开始纬度
	private int beginLng;// 开始经度
	private int endLat;// 结束纬度

	private int staticDate; // 统计时长, 单位秒
	private float oilConsumption; // 燃油消耗，%
	private float electricConsumption; // 电量消耗，%
	private long startDate; // 开始时间,UTC, 单位秒
	private int endLng; // 结束经度
	private long terminalMileage; //终端行驶里程(m)

	private long meterMileage; // 仪表行驶里程统计
	private float fuelOil;//积分总油耗，%
	private float oilValue;//仪表剩余油量，%

	private long beginGpsMileage;
	private long endGpsMileage;
	private long beginMeMileage;
	private long endMeMileage;

	// 辅助计算数据
	// private float preOil; // 上次燃油值，%
	// private float preElectric; // 上次电量值，%

	public DAMilages() {

	}

	public DAMilages(DBObject dbObjectDetail) {
		this.terminalId = (long) dbObjectDetail.get("tid");
		this.canMileage = Long.valueOf(String.valueOf(dbObjectDetail.get("mileage")));
		this.staticDate = (int) dbObjectDetail.get("sDate");
		this.oilConsumption = Float.parseFloat(dbObjectDetail.get("oil").toString());
		this.electricConsumption = Float.parseFloat(dbObjectDetail.get("ele").toString());
		this.beginMileage = (long) dbObjectDetail.get("bMileage");
		this.endMileage = (long) dbObjectDetail.get("eMileage");
		this.startDate = (long) dbObjectDetail.get("start");
		this.endDate = (long) dbObjectDetail.get("end");
		this.beginLat = (int) dbObjectDetail.get("bLat");
		this.beginLng = (int) dbObjectDetail.get("bLng");
		this.endLat = (int) dbObjectDetail.get("eLat");
		this.endLng = (int) dbObjectDetail.get("eLng");
		this.terminalMileage = (long) (dbObjectDetail.get("tm")==null?0L:dbObjectDetail.get("tm"));

		this.meterMileage = (long)(dbObjectDetail.get("meMileage")==null?0L:dbObjectDetail.get("meMileage")); //仪表里程统计值
		this.fuelOil = Float.parseFloat(dbObjectDetail.get("fOil") == null?"0":dbObjectDetail.get("fOil").toString());//积分总油耗
		this.oilValue = Float.parseFloat(dbObjectDetail.get("oilValue") == null?"0":dbObjectDetail.get("oilValue").toString());//仪表剩余油量统计
		this.beginGpsMileage = (long)(dbObjectDetail.get("bGpsMil")==null?0L:dbObjectDetail.get("bGpsMil"));
		this.endGpsMileage = (long)(dbObjectDetail.get("eGpsMil")==null?0L:dbObjectDetail.get("eGpsMil"));
		this.beginMeMileage = (long)(dbObjectDetail.get("bMeMil")==null?0L:dbObjectDetail.get("bMeMil"));
		this.endMeMileage = (long)(dbObjectDetail.get("eMeMil")==null?0L:dbObjectDetail.get("eMeMil"));

	}

	public DBObject toDBObject() {
		DBObject alarm = new BasicDBObject();
		alarm.put("tid", this.terminalId);
		alarm.put("mileage", this.canMileage);
		alarm.put("sDate", this.staticDate);
		alarm.put("oil", this.oilConsumption);
		alarm.put("ele", this.electricConsumption);
		alarm.put("bMileage", this.beginMileage);
		alarm.put("eMileage", this.endMileage);
		alarm.put("start", this.startDate);
		alarm.put("end", this.endDate);
		alarm.put("bLat", this.beginLat);
		alarm.put("bLng", this.beginLng);
		alarm.put("eLat", this.endLat);
		alarm.put("eLng", this.endLng);
		alarm.put("tm", this.terminalMileage);
		alarm.put("meMileage",this.meterMileage);
		alarm.put("fOil",this.fuelOil);
		alarm.put("oilValue",this.oilValue);
		alarm.put("bMeMil",this.beginMeMileage);
		alarm.put("eMeMil",this.endMeMileage);
		alarm.put("bGpsMil",this.beginGpsMileage);
		alarm.put("eGpsMil",this.endGpsMileage);
		return alarm;
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



	// public long getBeginDate() {
	// return beginDate;
	// }
	//
	// public void setBeginDate(long beginDate) {
	// this.beginDate = beginDate;
	// }

	public long getCanMileage() {
		return canMileage;
	}

	public void setCanMileage(long canMileage) {
		this.canMileage = canMileage;
	}

	public long getTerminalMileage() {
		return terminalMileage;
	}

	public void setTerminalMileage(long terminalMileage) {
		this.terminalMileage = terminalMileage;
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

	@Override
	public void dbObjectToBean(DBObject dbObjectDetail) {
		this.terminalId = (long) dbObjectDetail.get("tid");
		this.canMileage = Long.parseLong(String.valueOf(dbObjectDetail.get("mileage")));
		this.staticDate = (int) dbObjectDetail.get("sDate");
		this.oilConsumption = (float) dbObjectDetail.get("oil");
		this.electricConsumption = (float) dbObjectDetail.get("ele");
		this.beginMileage = (long) dbObjectDetail.get("bMileage");
		this.endMileage = (long) dbObjectDetail.get("eMileage");
		this.startDate = (long) dbObjectDetail.get("start");
		this.endDate = (long) dbObjectDetail.get("end");
		this.beginLat = (int) dbObjectDetail.get("bLat");
		this.beginLng = (int) dbObjectDetail.get("bLng");
		this.endLat = (int) dbObjectDetail.get("eLat");
		this.endLng = (int) dbObjectDetail.get("eLng");
		this.terminalMileage = (long) (dbObjectDetail.get("tm")==null?0:dbObjectDetail.get("tm"));
		this.meterMileage = (long)(dbObjectDetail.get("meMileage")==null?0L:dbObjectDetail.get("meMileage")); //仪表里程统计值
		this.fuelOil = Float.parseFloat(dbObjectDetail.get("fOil") == null?"0":dbObjectDetail.get("fOil").toString());//积分总油耗
		this.oilValue = Float.parseFloat(dbObjectDetail.get("oilValue") == null?"0":dbObjectDetail.get("oilValue").toString());//剩余油量统计
		this.beginGpsMileage = (long)(dbObjectDetail.get("bGpsMil")==null?0L:dbObjectDetail.get("bGpsMil"));
		this.endGpsMileage = (long)(dbObjectDetail.get("eGpsMil")==null?0L:dbObjectDetail.get("eGpsMil"));
		this.beginMeMileage = (long)(dbObjectDetail.get("bMeMil")==null?0L:dbObjectDetail.get("bMeMil"));
		this.endMeMileage = (long)(dbObjectDetail.get("eMeMil")==null?0L:dbObjectDetail.get("eMeMil"));
	}

	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
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

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public int getEndLng() {
		return endLng;
	}

	public void setEndLng(int endLng) {
		this.endLng = endLng;
	}

	public long getMeterMileage() {
		return meterMileage;
	}

	public void setMeterMileage(long meterMileage) {
		this.meterMileage = meterMileage;
	}

	public float getFuelOil() {
		return fuelOil;
	}

	public void setFuelOil(float fuelOil) {
		this.fuelOil = fuelOil;
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