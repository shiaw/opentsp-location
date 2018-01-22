package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 进出线路报警信息
 * @author hws
 *
 */
public class DARouteINOUTAlarm extends BaseAlarmEntity{
	private long terminalId;//终端标识号（索引）
	private long	areaId;	//路线标识
	private int	type;//0进区域；1出区域
	private long	beginDate;	//报警开始时间
	private long	endDate;	//用户下发处理报警结束时间
	private int	triggerDate;	//偏离路线报警，维持到报警条件解除结束时间
	private int	continuousTime;	//报警持续时间长度
	private int	triggerContinuousTime;	//触发报警处置时间长度
	private int	beginLat;	//开始纬度
	private int	beginLng;	//开始经度
	private int	endLat;	//结束纬度
	private int	endLng;	//结束经度
	private int	triggerLat;	//触发结束报警纬度
	private int	triggerLng;	//触发结束报警经度
	public DARouteINOUTAlarm(DBObject dbObjectDetail){
		this.terminalId=(long) dbObjectDetail.get("");
		this.areaId=(long) dbObjectDetail.get("");
		this.type=(int) dbObjectDetail.get("");
		this.beginDate=(long) dbObjectDetail.get("");
		this.endDate=(long) dbObjectDetail.get("");
		this.triggerDate=(int) dbObjectDetail.get("");
		this.continuousTime=(int) dbObjectDetail.get("");
		this.triggerContinuousTime=(int) dbObjectDetail.get("");
		this.beginLat=(int) dbObjectDetail.get("");
		this.beginLng=(int) dbObjectDetail.get("");
		this.endLat=(int) dbObjectDetail.get("");
		this.endLng=(int) dbObjectDetail.get("");
		this.triggerLat=(int) dbObjectDetail.get("");
		this.triggerLng=(int) dbObjectDetail.get("");
	}
	public DARouteINOUTAlarm() {
	}
	public DBObject toDBObject() {
		DBObject alarm = new BasicDBObject();
		alarm.put("AA", this.terminalId);//终端标识号
		alarm.put("BA", this.areaId);
		alarm.put("EA", this.type);
		alarm.put("HA", this.beginDate);
		alarm.put("IA", this.endDate);
		alarm.put("KA", this.continuousTime);
		alarm.put("MA", this.triggerContinuousTime);
		alarm.put("JA", this.triggerDate);
		alarm.put("WA", this.beginLat);
		alarm.put("XA", this.beginLng);
		alarm.put("YA", this.endLat);
		alarm.put("ZA", this.endLng);
		alarm.put("AB", this.triggerLat);
		alarm.put("BB", this.triggerLng);
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
	public int getTriggerDate() {
		return triggerDate;
	}
	public void setTriggerDate(int triggerDate) {
		this.triggerDate = triggerDate;
	}
	public int getContinuousTime() {
		return continuousTime;
	}
	public void setContinuousTime(int continuousTime) {
		this.continuousTime = continuousTime;
	}
	public int getTriggerContinuousTime() {
		return triggerContinuousTime;
	}
	public void setTriggerContinuousTime(int triggerContinuousTime) {
		this.triggerContinuousTime = triggerContinuousTime;
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
	public int getTriggerLat() {
		return triggerLat;
	}
	public void setTriggerLat(int triggerLat) {
		this.triggerLat = triggerLat;
	}
	public int getTriggerLng() {
		return triggerLng;
	}
	public void setTriggerLng(int triggerLng) {
		this.triggerLng = triggerLng;
	}
	@Override
	public void dbObjectToBean(DBObject dbObjectDetail) {
		this.terminalId=(long) dbObjectDetail.get("AA");
		this.areaId=(long) dbObjectDetail.get("BA");
		this.type=(int) dbObjectDetail.get("EA");
		this.beginDate=(long) dbObjectDetail.get("HA");
		this.endDate=(long) dbObjectDetail.get("IA");
		this.triggerDate=(int) dbObjectDetail.get("KA");
		this.continuousTime=(int) dbObjectDetail.get("MA");
		this.triggerContinuousTime=(int) dbObjectDetail.get("JA");
		this.beginLat=(int) dbObjectDetail.get("WA");
		this.beginLng=(int) dbObjectDetail.get("XA");
		this.endLat=(int) dbObjectDetail.get("YA");
		this.endLng=(int) dbObjectDetail.get("ZA");
		this.triggerLat=(int) dbObjectDetail.get("AB");
		this.triggerLng=(int) dbObjectDetail.get("BB");
	}



}
