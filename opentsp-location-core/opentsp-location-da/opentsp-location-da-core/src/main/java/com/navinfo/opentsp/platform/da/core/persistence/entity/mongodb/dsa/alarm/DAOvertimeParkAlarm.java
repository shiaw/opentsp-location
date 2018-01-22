package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 滞留超时
 *
 * @author zyl
 *
 */
public class DAOvertimeParkAlarm extends BaseAlarmEntity {
	private String _id;// mongo主键
	private long terminalId;// 终端标识号（索引）
	private long areaId;// 区域ID
	private long beginDate;// 入区域时间
	private long endDate;// 出区域时间
	private int continuousTime;// 滞留超时时间长度

	private int limitParking;// 滞留阀值
	private int beginLat;// 开始纬度
	private int beginLng;// 开始经度
	private int endLat;// 结束纬度
	private int endLng;// 结束经度
	private int tailMerge; //0需要合并  ;   1统计完结
	public DAOvertimeParkAlarm() {
	}

	public DAOvertimeParkAlarm(DBObject dbObjectDetail) {
		this._id = (String) dbObjectDetail.get("_id");
		this.terminalId = (long) dbObjectDetail.get("AA");
		this.areaId = (long) dbObjectDetail.get("BA");
		this.beginDate = (long) dbObjectDetail.get("HA");
		this.endDate = (long) dbObjectDetail.get("IA");
		this.continuousTime = (int) dbObjectDetail.get("KA");
		this.limitParking = (int) dbObjectDetail.get("TB");
		this.beginLat = (int) dbObjectDetail.get("WA");
		this.beginLng = (int) dbObjectDetail.get("XA");
		this.endLat = (int) dbObjectDetail.get("YA");
		this.endLng = (int) dbObjectDetail.get("ZA");
		this.tailMerge = (int) (dbObjectDetail.get("tailMerge")==null?0:dbObjectDetail.get("tailMerge"));
	}

	public long getTerminalId() {
		return terminalId;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
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

	public int getTailMerge() {
		return tailMerge;
	}

	public void setTailMerge(int tailMerge) {
		this.tailMerge = tailMerge;
	}

	public DBObject toDBObject() {
		DBObject alarm = new BasicDBObject();
		alarm.put("AA", this.terminalId);// 终端标识号
		alarm.put("BA", this.areaId);
		alarm.put("HA", this.beginDate);
		alarm.put("IA", this.endDate);
		alarm.put("KA", this.continuousTime);
		alarm.put("TB", this.limitParking);
		alarm.put("WA", this.beginLat);
		alarm.put("XA", this.beginLng);
		alarm.put("YA", this.endLat);
		alarm.put("ZA", this.endLng);
		alarm.put("tailMerge",this.tailMerge);
		return alarm;
	}

	@Override
	public void dbObjectToBean(DBObject dbObjectDetail) {
		this._id = dbObjectDetail.get("_id").toString();
		this.terminalId = (long) dbObjectDetail.get("AA");
		this.areaId = (long) dbObjectDetail.get("BA");
		this.beginDate = (long) dbObjectDetail.get("HA");
		this.endDate = (long) dbObjectDetail.get("IA");
		this.continuousTime = (int) dbObjectDetail.get("KA");
		this.limitParking = (int) dbObjectDetail.get("TB");
		this.beginLat = (int) dbObjectDetail.get("WA");
		this.beginLng = (int) dbObjectDetail.get("XA");
		this.endLat = (int) dbObjectDetail.get("YA");
		this.endLng = (int) dbObjectDetail.get("ZA");
		this.tailMerge = (int) (dbObjectDetail.get("tailMerge")==null?0:dbObjectDetail.get("tailMerge"));
	}
}

