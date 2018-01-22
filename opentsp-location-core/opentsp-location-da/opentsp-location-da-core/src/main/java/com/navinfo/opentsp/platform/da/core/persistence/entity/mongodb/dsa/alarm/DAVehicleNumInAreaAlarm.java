package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DAVehicleNumInAreaAlarm extends BaseAlarmEntity{
	private long terminalId;
	private long mileage;
	private long continuousTime;
	private boolean isFault;
	private int times =1;
	public DAVehicleNumInAreaAlarm(){
			
	}
	public DAVehicleNumInAreaAlarm(DBObject dbObjectDetail){
		this.terminalId=(long) dbObjectDetail.get("id");
		this.mileage=(long) dbObjectDetail.get("m");
		this.continuousTime=(long) dbObjectDetail.get("t");
		this.isFault=(boolean) dbObjectDetail.get("f"); 
	}
	public DBObject toDBObject() {
		DBObject alarm = new BasicDBObject();
		alarm.put("id", this.terminalId);
		alarm.put("m", this.mileage);
		alarm.put("t", this.continuousTime);
		alarm.put("f", this.isFault);
		return alarm;
	}
	@Override
	public void dbObjectToBean(DBObject dbObjectDetail) {
		this.terminalId=(long) dbObjectDetail.get("id");
		this.mileage=(long) dbObjectDetail.get("m");
		this.continuousTime=(long) dbObjectDetail.get("t");
		this.isFault=(boolean) dbObjectDetail.get("f");
	}
	public long getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}
	public boolean isFault() {
		return isFault;
	}
	public void setFault(boolean isFault) {
		this.isFault = isFault;
	}
	public long getMileage() {
		return mileage;
	}
	public void setMileage(long mileage) {
		this.mileage = mileage;
	}
	public long getContinuousTime() {
		return continuousTime;
	}
	public void setContinuousTime(long continuousTime) {
		this.continuousTime = continuousTime;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
}
