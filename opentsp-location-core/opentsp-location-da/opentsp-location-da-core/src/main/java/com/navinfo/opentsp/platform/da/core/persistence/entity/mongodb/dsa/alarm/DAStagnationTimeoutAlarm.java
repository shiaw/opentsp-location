package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DAStagnationTimeoutAlarm extends BaseAlarmEntity {
	private String _id; //mongo自增主键
	private long terminalId; //终端id
	private long beginDate; // 停滞报警开始时间
	private long endDate; //停滞报警结束时间
	private int continuousTime; //停滞超时时间长度
	private int limitParking; //停滞阀值
	private int beginLat; //停滞报警开始纬度
	private int beginLng; // 停滞报警开始经度
	private boolean status; // true：正常；false：撤销
	private int tailMerge;//数据合并尾标识 0:需要合并;1:统计完结

	@Override
	public void dbObjectToBean(DBObject dbObjectDetail) {
		this._id = dbObjectDetail.get("_id").toString();
		this.terminalId = (long) dbObjectDetail.get("tId");
		this.beginDate = (long) dbObjectDetail.get("begin");
		this.endDate = (long) dbObjectDetail.get("end");
		this.continuousTime = (int) dbObjectDetail.get("con");
		this.limitParking = (int) dbObjectDetail.get("lp");
		this.beginLat = (int) dbObjectDetail.get("bLat");
		this.beginLng = (int) dbObjectDetail.get("bLng");
		this.status = (int) dbObjectDetail.get("st") == 1 ? true : false;
	}

	public DBObject toDBObject() {
		DBObject alarm = new BasicDBObject();
		alarm.put("tId", this.terminalId);// 终端标识号
		alarm.put("begin", this.beginDate);
		alarm.put("end", this.endDate);
		alarm.put("con", this.continuousTime);
		alarm.put("lp", this.limitParking);
		alarm.put("bLat", this.beginLat);
		alarm.put("bLng", this.beginLng);
		alarm.put("st", this.status == true ? 1 : 0);
		alarm.put("tail", this.tailMerge);
		return alarm;
	}



	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
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
	public int getLimitParking() {
		return limitParking;
	}
	public void setLimitParking(int limitParking) {
		this.limitParking = limitParking;
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
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getTailMerge() {
		return tailMerge;
	}

	public void setTailMerge(int tailMerge) {
		this.tailMerge = tailMerge;
	}

}
