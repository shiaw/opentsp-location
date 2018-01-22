package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 汇总统计信息
 * @author hws
 *
 */
public class DATotalAlarmInfo  extends BaseAlarmEntity{
	private int	type;//	用协议定义的报警类型复制
	private int	continuousTime;//	报警持续时间长度
	private int	counts;//	报警次数
	@Override
	public void dbObjectToBean(DBObject dbObjectDetail) {

		this.type=(int) dbObjectDetail.get("EA");
		this.continuousTime=(int) dbObjectDetail.get("KA");
		this.counts=(int) dbObjectDetail.get("QB");
	}
	public DATotalAlarmInfo(){

	}
	public DATotalAlarmInfo(DBObject dbObjectDetail){
		this.type=(int) dbObjectDetail.get("EA");
		this.continuousTime=(int) dbObjectDetail.get("KA");
		this.counts=(int) dbObjectDetail.get("QB");

	}
	public DBObject toDBObject() {
		DBObject alarm = new BasicDBObject();
		alarm.put("EA", this.type);
		alarm.put("QB", this.counts);
		alarm.put("KA", this.continuousTime);

		return alarm;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getContinuousTime() {
		return continuousTime;
	}
	public void setContinuousTime(int continuousTime) {
		this.continuousTime = continuousTime;
	}
	public int getCounts() {
		return counts;
	}
	public void setCounts(int counts) {
		this.counts = counts;
	}



}
