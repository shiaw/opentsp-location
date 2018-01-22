package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 终端在线状态统计信息
 * @author hws
 *
 */
public class DATerminalOnOffLineStatus extends BaseAlarmEntity{
	private long terminalId;//终端标识号（索引）
	private int	onlineStatus;//	0：离线；1：在线
	private long	beginDate;//	报警开始时间
	private long	endDate;//	报警结束时间
	private int	continuousTime;//	报警持续时间长度
	public DATerminalOnOffLineStatus(){

	}
	public DATerminalOnOffLineStatus(DBObject dbObjectDetail){
		this.terminalId=(long) dbObjectDetail.get("AA");
		this.onlineStatus=(int) dbObjectDetail.get("LB");
		this.beginDate=(long) dbObjectDetail.get("HA");
		this.endDate=(long) dbObjectDetail.get("IA");
		this.continuousTime=(int) dbObjectDetail.get("KA");
	}
	public DBObject toDBObject() {
		DBObject alarm = new BasicDBObject();
		alarm.put("AA", this.terminalId);//终端标识号
		alarm.put("HA", this.beginDate);
		alarm.put("IA", this.endDate);
		alarm.put("KA", this.continuousTime);
		alarm.put("LB", this.onlineStatus);
		return alarm;
	}
	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}
	public int getOnlineStatus() {
		return onlineStatus;
	}
	public void setOnlineStatus(int onlineStatus) {
		this.onlineStatus = onlineStatus;
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
	@Override
	public void dbObjectToBean(DBObject dbObjectDetail) {
		this.terminalId=(long) dbObjectDetail.get("AA");
		this.onlineStatus=(int) dbObjectDetail.get("LB");
		this.beginDate=(long) dbObjectDetail.get("HA");
		this.endDate=(long) dbObjectDetail.get("IA");
		this.continuousTime=(int) dbObjectDetail.get("KA");

	}



}