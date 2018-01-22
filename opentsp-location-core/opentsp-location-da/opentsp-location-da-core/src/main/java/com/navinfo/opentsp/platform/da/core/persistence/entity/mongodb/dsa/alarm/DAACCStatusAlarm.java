package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
/**
 * ACC状态统计信息
 * @author hws
 *
 */
public class DAACCStatusAlarm extends BaseAlarmEntity{
	private long terminalId;//�ն˱�ʶ�ţ�������
	private int	accStatus;//	0��OFF��1��ON
	private long	beginDate;//	������ʼʱ��
	private long	endDate	;//��������ʱ��
	private int	continuousTime;//	��������ʱ�䳤��
	private int	beginLat	;//��ʼγ��
	private int	beginLng	;//��ʼ����
	private int	endLat	;//����γ��
	private int	endLng	;//��������
	public DAACCStatusAlarm(){

	}
	public DAACCStatusAlarm(DBObject dbObjectDetail){
		this.terminalId=(long) dbObjectDetail.get("AA");
		this.accStatus=(int) dbObjectDetail.get("FA");
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
		alarm.put("AA", this.terminalId);//�ն˱�ʶ��
		alarm.put("FA", this.accStatus);
		alarm.put("HA", this.beginDate);
		alarm.put("IA", this.endDate);
		alarm.put("KA", this.continuousTime);
		alarm.put("WA", this.beginLat);
		alarm.put("XA", this.beginLng);
		alarm.put("YA", this.endLat);
		alarm.put("ZA", this.endLng);
		return alarm;
}
	
	public void toBeanObject(DBObject dbObjectDetail) {
		this.setTerminalId((long ) dbObjectDetail.get("AA"));
		this.setAccStatus((int) dbObjectDetail.get("FA"));
		this.setBeginDate( (long) dbObjectDetail.get("HA"));
		this.setEndDate( (long) dbObjectDetail.get("IA"));
		this.setContinuousTime( (int) dbObjectDetail.get("KA"));
		this.setBeginLat((int) dbObjectDetail.get("WA"));
		this.setBeginLng( (int) dbObjectDetail.get("XA"));
		this.setEndLat((int) dbObjectDetail.get("YA"));
		this.setEndLng((int) dbObjectDetail.get("ZA"));
	}
	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}
	public int getAccStatus() {
		return accStatus;
	}
	public void setAccStatus(int accStatus) {
		this.accStatus = accStatus;
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
		this.accStatus=(int) dbObjectDetail.get("FA");
		this.beginDate=(long) dbObjectDetail.get("HA");
		this.endDate=(long) dbObjectDetail.get("IA");
		this.continuousTime=(int) dbObjectDetail.get("KA");
		this.beginLat=(int) dbObjectDetail.get("WA");
		this.beginLng=(int) dbObjectDetail.get("XA");
		this.endLat=(int) dbObjectDetail.get("YA");
		this.endLng=(int) dbObjectDetail.get("ZA");
		
	}
	
	

}
