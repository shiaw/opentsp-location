package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import java.io.Serializable;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
/**
 * 流量统计信息
 * @author hws
 *
 */
public class DAWFlow  extends BaseAlarmEntity implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private long terminalId;//终端标识号（索引）
	private int	flowDate;//	（小时数1~24）
	private float	upFlow;//	上行流量
	private float	downFlow;//	下行流量
	public DAWFlow(){

	}

	public DAWFlow(DBObject dbObjectDetail){
		this.flowDate=(int) dbObjectDetail.get("MB");
		this.upFlow=(float) dbObjectDetail.get("NB");
		this.downFlow=(float) dbObjectDetail.get("OB");

	}

	public DBObject toDBObject() {
		DBObject alarm = new BasicDBObject();
		alarm.put("MB", this.flowDate);
		alarm.put("NB", this.upFlow);
		alarm.put("OB", this.downFlow);
		alarm.put("AA", this.terminalId);
		return alarm;
	}
	public int getFlowDate() {
		return flowDate;
	}
	public void setFlowDate(int flowDate) {
		this.flowDate = flowDate;
	}
	public float getUpFlow() {
		return upFlow;
	}
	public void setUpFlow(float upFlow) {
		this.upFlow = upFlow;
	}
	public float getDownFlow() {
		return downFlow;
	}
	public void setDownFlow(float downFlow) {
		this.downFlow = downFlow;
	}

	@Override
	public void dbObjectToBean(DBObject dbObjectDetail) {
		this.flowDate=(int) dbObjectDetail.get("MB");
		this.upFlow=Float.parseFloat( dbObjectDetail.get("NB")+"");
		this.downFlow=Float.parseFloat(dbObjectDetail.get("OB")+"");
		this.terminalId=(long) dbObjectDetail.get("AA");
	}

	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}



}
