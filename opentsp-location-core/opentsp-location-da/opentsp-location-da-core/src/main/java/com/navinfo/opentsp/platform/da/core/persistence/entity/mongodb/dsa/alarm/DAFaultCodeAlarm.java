package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 故障码
 * @author zyl
 */
public class DAFaultCodeAlarm extends BaseAlarmEntity {
	private String _id;// mongo主键
	private long terminalId;// 终端标识号（索引）
	private int spn;
	private int fmi;
	private long beginDate;
	private long endDate;
	private int continuousTime;
	private int beginLat;// 开始纬度
	private int beginLng;// 开始经度
	private int endLat;// 结束纬度
	private int endLng;// 结束经度

	public DAFaultCodeAlarm() {
	}

	public DBObject toDBObject() {
		DBObject alarm = new BasicDBObject();
		alarm.put("tId", this.terminalId);// 终端标识号
		alarm.put("spn", this.spn);
		alarm.put("fmi", this.fmi);
		alarm.put("begin", this.beginDate);
		alarm.put("end", this.endDate);
		alarm.put("con", this.continuousTime);
		alarm.put("bLat", this.beginLat);
		alarm.put("bLng", this.beginLng);
		alarm.put("eLat", this.endLat);
		alarm.put("eLng", this.endLng);
		return alarm;
	}

	@Override
	public void dbObjectToBean(DBObject dbObjectDetail) {
		this._id = dbObjectDetail.get("_id").toString();
		this.terminalId = (long) dbObjectDetail.get("tId");
		this.spn = (int) dbObjectDetail.get("spn");
		this.fmi = (int) dbObjectDetail.get("fmi");
		this.beginDate = (long) dbObjectDetail.get("begin");
		if(dbObjectDetail.get("end")==null){
			this.endDate = 0l;
		}else{
			this.endDate = Long.valueOf(String.valueOf(dbObjectDetail.get("end")));
		}
		this.continuousTime = (int) dbObjectDetail.get("con");
		this.beginLat = (int) dbObjectDetail.get("bLat");
		this.beginLng = (int) dbObjectDetail.get("bLng");
		this.endLat = (int) dbObjectDetail.get("eLat");
		this.endLng = (int) dbObjectDetail.get("eLng");
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

	public int getSpn() {
		return spn;
	}

	public void setSpn(int spn) {
		this.spn = spn;
	}

	public int getFmi() {
		return fmi;
	}

	public void setFmi(int fmi) {
		this.fmi = fmi;
	}

	public long getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(long beginDate) {
		this.beginDate = beginDate;
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

	public long getEndDate() {
		return endDate;
	}

	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}

}
