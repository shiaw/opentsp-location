package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


/**
 * 进出区域报警信息
 * @author
 *
 */
public class DAAreaINOUTAlarm extends BaseAlarmEntity{

	private long terminalId;//�ն˱�ʶ�ţ�������
	private long	areaId ;	//�����ʶ
	private int	type;	//0������1������
	private long	beginDate	;//������ʼʱ��
	private long	endDate  ;	//��������ʱ��
	private int	continuousTime;	//��������ʱ�䳤��
	private int	beginLat;	//��ʼγ��
	private int	beginLng;	//��ʼ����
	private int	endLat	;//����γ��
	private 	int	endLng;	//��������
	public DAAreaINOUTAlarm(){
		super();
	}
	public DAAreaINOUTAlarm(DBObject dbObjectDetail){
		this.setTerminalId((long ) dbObjectDetail.get("AA"));
		this.setAreaId( (long) dbObjectDetail.get("BA"));
		this.setType((int) dbObjectDetail.get("EA"));
		this.setBeginDate(  (long) dbObjectDetail.get("HA"));
		this.setEndDate( (long) dbObjectDetail.get("IA"));
		this.setContinuousTime((int) dbObjectDetail.get("KA"));
		this.setBeginLat( (int) dbObjectDetail.get("WA"));
		this.setBeginLng( (int) dbObjectDetail.get("XA"));
		this.setEndLat( (int) dbObjectDetail.get("YA"));
		this.setEndLng( (int) dbObjectDetail.get("ZA"));
	}
	public DBObject toDBObject() {
		DBObject alarm = new BasicDBObject();
		alarm.put("AA", this.terminalId);//�ն˱�ʶ��
		alarm.put("HA", this.beginDate);
		alarm.put("IA", this.endDate);
		alarm.put("KA", this.continuousTime);
		alarm.put("BA", this.areaId);
		alarm.put("EA", this.type);
		alarm.put("WA", this.beginLat);
		alarm.put("XA", this.beginLng);
		alarm.put("YA", this.endLat);
		alarm.put("ZA", this.endLng);
		return alarm;
	}
	
	public void toBeanObject(DBObject dbObjectDetail) {
		this.setTerminalId((long ) dbObjectDetail.get("AA"));
		this.setAreaId( (long) dbObjectDetail.get("HA"));
		this.setType(  (int) dbObjectDetail.get("IA"));
		this.setBeginDate(  (long) dbObjectDetail.get("KA"));
		this.setEndDate( (long) dbObjectDetail.get("BA"));
		this.setContinuousTime((int) dbObjectDetail.get("EA"));
		this.setBeginLat( (int) dbObjectDetail.get("WA"));
		this.setBeginLng( (int) dbObjectDetail.get("XA"));
		this.setEndLat( (int) dbObjectDetail.get("YA"));
		this.setEndLng( (int) dbObjectDetail.get("ZA"));
	}
	public long getTerminalId() {
		return terminalId;
	}

  public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
  }
	public long getAreaId() {
		return areaId;
	}
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
		this.setTerminalId((long ) dbObjectDetail.get("AA"));
		this.setAreaId( (long) dbObjectDetail.get("BA"));
		this.setType((int) dbObjectDetail.get("EA"));
		this.setBeginDate(  (long) dbObjectDetail.get("HA"));
		this.setEndDate( (long) dbObjectDetail.get("IA"));
		this.setContinuousTime((int) dbObjectDetail.get("KA"));
		this.setBeginLat( (int) dbObjectDetail.get("WA"));
		this.setBeginLng( (int) dbObjectDetail.get("XA"));
		this.setEndLat( (int) dbObjectDetail.get("YA"));
		this.setEndLng( (int) dbObjectDetail.get("ZA"));
		
	}
}
