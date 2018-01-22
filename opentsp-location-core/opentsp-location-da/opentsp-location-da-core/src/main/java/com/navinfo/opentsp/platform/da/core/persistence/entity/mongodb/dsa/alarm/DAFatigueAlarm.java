package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 疲劳驾驶统计信息
 * @author hws
 *
 */
public class DAFatigueAlarm extends BaseAlarmEntity{

	private long terminalId;//终端标识号（索引）
	private long	beginDate;//	报警开始时间
	private long	endDate	;//报警结束时间
	private int	continuousTime;//	报警持续时间长度
	private int	limitDriving;//  	单次疲劳驾驶时间阀值
	private int	limitDayDriving;//	当天疲劳驾驶时间阀值
	private int	limitRest;//	疲劳驾驶休息时间阀值
	private int	beginLat;//	开始纬度
	private int	beginLng;//	开始经度
	private int	endLat;//结束纬度
	private int	endLng;//结束经度
	public DAFatigueAlarm(){

	}
	public DAFatigueAlarm(DBObject dbObjectDetail){
		this.terminalId=(long) dbObjectDetail.get("AA");
		this.beginDate=(long) dbObjectDetail.get("HA");
		this.endDate=(long) dbObjectDetail.get("IA");
		this.continuousTime=(int) dbObjectDetail.get("KA");
		this.limitDriving=(int) dbObjectDetail.get("QA");
		this.limitDayDriving=(int) dbObjectDetail.get("RA");
		this.limitRest=(int) dbObjectDetail.get("SA");
		this.beginLat=(int) dbObjectDetail.get("WA");
		this.beginLng=(int) dbObjectDetail.get("XA");
		this.endLat=(int) dbObjectDetail.get("YA");
		this.endLng=(int) dbObjectDetail.get("ZA");
	}
	public DBObject toDBObject() {
		DBObject alarm = new BasicDBObject();
		alarm.put("AA", this.terminalId);//终端标识号
		alarm.put("HA", this.beginDate);
		alarm.put("IA", this.endDate);
		alarm.put("KA", this.continuousTime);
		alarm.put("QA", this.limitDriving);
		alarm.put("RA", this.limitDayDriving);
		alarm.put("SA", this.limitRest);
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
	public int getLimitDriving() {
		return limitDriving;
	}
	public void setLimitDriving(int limitDriving) {
		this.limitDriving = limitDriving;
	}
	public int getLimitDayDriving() {
		return limitDayDriving;
	}
	public void setLimitDayDriving(int limitDayDriving) {
		this.limitDayDriving = limitDayDriving;
	}
	public int getLimitRest() {
		return limitRest;
	}
	public void setLimitRest(int limitRest) {
		this.limitRest = limitRest;
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
		this.beginDate=(long) dbObjectDetail.get("HA");
		this.endDate=(long) dbObjectDetail.get("IA");
		this.continuousTime=(int) dbObjectDetail.get("KA");
		this.limitDriving=(int) dbObjectDetail.get("QA");
		this.limitDayDriving=(int) dbObjectDetail.get("RA");
		this.limitRest=(int) dbObjectDetail.get("SA");
		this.beginLat=(int) dbObjectDetail.get("WA");
		this.beginLng=(int) dbObjectDetail.get("XA");
		this.endLat=(int) dbObjectDetail.get("YA");
		this.endLng=(int) dbObjectDetail.get("ZA");

	}



}
