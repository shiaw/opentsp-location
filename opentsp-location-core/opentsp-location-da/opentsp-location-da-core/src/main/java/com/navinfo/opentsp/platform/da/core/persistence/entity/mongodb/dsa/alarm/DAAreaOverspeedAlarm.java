package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 区域限速报警信息
 * @author hws
 *
 */
public class DAAreaOverspeedAlarm  extends BaseAlarmEntity{

	private long terminalId;//�ն˱�ʶ�ţ�������
    private	long	areaId;	//�����ʶ
    private int	type;	//0������1������
    private long	beginDate;	//������ʼʱ��
    private long	endDate;	//��������ʱ��
    private int	continuousTime;	//��������ʱ�䳤��
    private int	limitSpeed;	//���ٷ�ֵ
    private int	maxSpeed;	//����ٶ�
    private int	minSpeed;	//��С�ٶ�
    private int	beginLat;	//��ʼγ��
    private int	beginLng;	//��ʼ����
    private int	endLat;	//����γ��
    private int	endLng;	//��������
    public DAAreaOverspeedAlarm(DBObject dbObjectDetail){
    	this.terminalId=(long) dbObjectDetail.get("AA");
    	this.type=(int) dbObjectDetail.get("BA");
    	this.areaId=(long) dbObjectDetail.get("EA");
    	this.beginDate=(long) dbObjectDetail.get("HA");
    	this.endDate=(long) dbObjectDetail.get("IA");
    	this.continuousTime=(int) dbObjectDetail.get("KA");
    	this.limitSpeed=(int) dbObjectDetail.get("NA");
    	this.maxSpeed=(int) dbObjectDetail.get("OA");
    	this.minSpeed=(int) dbObjectDetail.get("PA");
    	this.beginLat=(int) dbObjectDetail.get("WA");
    	this.beginLng=(int) dbObjectDetail.get("XA");
    	this.endLat=(int) dbObjectDetail.get("YA");
    	this.endLng=(int) dbObjectDetail.get("ZA");
    		}
    public DAAreaOverspeedAlarm() {
		// TODO Auto-generated constructor stub
	}
	public DBObject toDBObject() {
		DBObject alarm = new BasicDBObject();
		alarm.put("AA", this.terminalId);//�ն˱�ʶ��
		alarm.put("BA", this.areaId);
		alarm.put("EA", this.type);
		alarm.put("HA", this.beginDate);
		alarm.put("IA", this.endDate);
		alarm.put("KA", this.continuousTime);
		alarm.put("NA", this.limitSpeed);
		alarm.put("OA", this.maxSpeed);
		alarm.put("PA", this.minSpeed);
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
	public int getLimitSpeed() {
		return limitSpeed;
	}
	public void setLimitSpeed(int limitSpeed) {
		this.limitSpeed = limitSpeed;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	public int getMinSpeed() {
		return minSpeed;
	}
	public void setMinSpeed(int minSpeed) {
		this.minSpeed = minSpeed;
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
    	this.type=(int) dbObjectDetail.get("EA");
    	this.areaId=(long) dbObjectDetail.get("BA");
    	this.beginDate=(long) dbObjectDetail.get("HA");
    	this.endDate=(long) dbObjectDetail.get("IA");
    	this.continuousTime=(int) dbObjectDetail.get("KA");
    	this.limitSpeed=(int) dbObjectDetail.get("NA");
    	this.maxSpeed=(int) dbObjectDetail.get("OA");
    	this.minSpeed=(int) dbObjectDetail.get("PA");
    	this.beginLat=(int) dbObjectDetail.get("WA");
    	this.beginLng=(int) dbObjectDetail.get("XA"); 
    	this.endLat=(int) dbObjectDetail.get("YA"); 
    	this.endLng=(int) dbObjectDetail.get("ZA"); 
		
	}

}
