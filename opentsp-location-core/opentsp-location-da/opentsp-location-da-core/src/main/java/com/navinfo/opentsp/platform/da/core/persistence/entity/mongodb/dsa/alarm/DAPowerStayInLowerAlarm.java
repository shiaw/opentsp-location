package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 主电源欠压统计信息
 * @author hws
 *
 */
public class DAPowerStayInLowerAlarm  extends BaseAlarmEntity{
	private long terminalId;//终端标识号（索引）
	long	beginDate;//	报警开始时间
	long	endDate;//	报警结束时间
	int	continuousTime;//	报警持续时间长度
	int	beginLat;//	开始纬度
	int	beginLng;//	开始经度
	int	endLat;//	结束纬度
	int	endLng;//	结束经度
	public DAPowerStayInLowerAlarm(){

	}
	public DAPowerStayInLowerAlarm(DBObject dbObjectDetail){
		this.terminalId=(long) dbObjectDetail.get("AA");
		this.beginDate=(long) dbObjectDetail.get("HA");
		this.endDate=(long) dbObjectDetail.get("IA");
		this.continuousTime=(int) dbObjectDetail.get("KA");
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
		this.beginLat=(int) dbObjectDetail.get("WA");
		this.beginLng=(int) dbObjectDetail.get("XA");
		this.endLat=(int) dbObjectDetail.get("YA");
		this.endLng=(int) dbObjectDetail.get("ZA");

	}



}
