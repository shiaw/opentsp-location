package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 路段超速报警信息
 * @author hws
 *
 */
public class DARLineOverspeedAlarm extends BaseAlarmEntity{
	private long terminalId;//终端标识号（索引）
	private long	areaId;	//路线标识
	private long	segmentId;	//路段标识
	private long	beginDate;	//报警开始时间
	private long	endDate;	//报警结束时间
	private int	continuousTime;	//报警持续时间长度
	private int	limitSpeed;  	//超速阀值
	private int	maxSpeed;	//最大速度
	private int	minSpeed;	//最小速度
	private int	beginLat;	//开始纬度
	private int	beginLng;	//开始经度
	private int	endLat;	//结束纬度
	private int	endLng;	//结束经度
	public DARLineOverspeedAlarm(){

	}
	public DARLineOverspeedAlarm(DBObject dbObjectDetail){
		this.terminalId=(long) dbObjectDetail.get("AA");
		this.areaId=(long) dbObjectDetail.get("BA");
		this.segmentId=(long) dbObjectDetail.get("CA");
		this.beginDate=(long) dbObjectDetail.get("HA");
		this.endDate=(long) dbObjectDetail.get("IA");
		this.continuousTime=(int) dbObjectDetail.get("KA");
		this.limitSpeed=(int) dbObjectDetail.get("NA");
		this.maxSpeed=(int) dbObjectDetail.get("OA");
		this.minSpeed=(int) dbObjectDetail.get("PA");
		this.beginLat=(int) dbObjectDetail.get("WA");
		this.beginLng=(int) dbObjectDetail.get("XA");
		this.endLat=(int) dbObjectDetail.get("YA");
		this.endLng=(int) dbObjectDetail.get("ZA");
	}

	public DBObject toDBObject() {
		DBObject alarm = new BasicDBObject();
		alarm.put("AA", this.terminalId);//终端标识号
		alarm.put("BA", this.areaId);
		alarm.put("CA", this.segmentId);
		alarm.put("HA", this.beginDate);
		alarm.put("IA", this.endDate);
		alarm.put("KA", this.continuousTime);
		alarm.put("NA", this.limitSpeed);
		alarm.put("OA", this.maxSpeed);
		alarm.put("PA", this.minSpeed);
		alarm.put("WA", this.beginLat);
		alarm.put("XA", this.beginLng);
		alarm.put("YA", this.endLat);
		alarm.put("ZA", this.endLng);
		return alarm;
	}

	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}
	public long getAreaId() {
		return areaId;
	}
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	public long getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(long segmentId) {
		this.segmentId = segmentId;
	}
	public long getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(long beginDate) {
		this.beginDate = beginDate;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	public int getContinuousTime() {
		return continuousTime;
	}
	public void setContinuousTime(int continuousTime) {
		this.continuousTime = continuousTime;
	}
	public int getLimitSpeed() {
		return limitSpeed;
	}
	public void setLimitSpeed(int limitSpeed) {
		this.limitSpeed = limitSpeed;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public int getMinSpeed() {
		return minSpeed;
	}
	public void setMinSpeed(int minSpeed) {
		this.minSpeed = minSpeed;
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
	@Override
	public void dbObjectToBean(DBObject dbObjectDetail) {
		this.terminalId=(long) dbObjectDetail.get("AA");
		this.areaId=(long) dbObjectDetail.get("BA");
		this.segmentId=(long) dbObjectDetail.get("CA");
		this.beginDate=(long) dbObjectDetail.get("HA");
		this.endDate=(long) dbObjectDetail.get("IA");
		this.continuousTime=(int) dbObjectDetail.get("KA");
		this.limitSpeed=(int) dbObjectDetail.get("NA");
		this.maxSpeed=(int) dbObjectDetail.get("OA");
		this.minSpeed=(int) dbObjectDetail.get("PA");
		this.beginLat=(int) dbObjectDetail.get("WA");
		this.beginLng=(int) dbObjectDetail.get("XA");
		this.endLat=(int) dbObjectDetail.get("YA");
		this.endLng=(int) dbObjectDetail.get("ZA");

	}


}

