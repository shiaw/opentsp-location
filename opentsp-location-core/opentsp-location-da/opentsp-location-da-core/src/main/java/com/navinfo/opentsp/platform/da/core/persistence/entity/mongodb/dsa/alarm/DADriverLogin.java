package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 驶员登录统计信息
 * @author hws
 *
 */
public class DADriverLogin extends BaseAlarmEntity{

	private long terminalId;//终端标识号（索引）
	private String	driverName;//	驾驶员姓名
	private String	driverIdCode;//	驾驶证编号
	private String	driverCertificate;//	行车资格证编号
	private String	agencyName;//	发证机构名称
	private String	certificateValid;//	证件有效期
	private long	creditDate;//	刷卡时间
	private long	stubbsCard;//	拔卡时间
	private long	driverTime;//	登录时间
	private String	failedReason;//失败原因
	private int	beginLat;//	开始纬度
	private int	beginLng;//	开始经度
	private int	endLat;//	结束纬度
	private int	endLNG;//	结束经度
	public DADriverLogin(){

	}
	public DADriverLogin(DBObject dbObjectDetail){
		this.terminalId=(long) dbObjectDetail.get("AA");
		this.driverName=(String) dbObjectDetail.get("CB");
		this.driverIdCode=(String) dbObjectDetail.get("DB");
		this.driverCertificate=(String) dbObjectDetail.get("EB");
		this.agencyName=(String) dbObjectDetail.get("FB");
		this.certificateValid=(String) dbObjectDetail.get("GB");
		this.creditDate=(long) dbObjectDetail.get("HB");
		this.stubbsCard=(long) dbObjectDetail.get("IB");
		this.driverTime=(long) dbObjectDetail.get("JB");
		this.failedReason=(String) dbObjectDetail.get("KB");
		this.beginLat=(int) dbObjectDetail.get("WA");
		this.beginLng=(int) dbObjectDetail.get("XA");
		this.endLat=(int) dbObjectDetail.get("YA");
		this.endLNG=(int) dbObjectDetail.get("ZA");

	}
	public DBObject toDBObject() {
		DBObject alarm = new BasicDBObject();
		alarm.put("AA", this.terminalId);//终端标识号
		alarm.put("CB", this.driverName);
		alarm.put("DB", this.driverIdCode);
		alarm.put("EB", this.driverCertificate);
		alarm.put("FB", this.agencyName);
		alarm.put("GB", this.certificateValid);
		alarm.put("HB", this.creditDate);
		alarm.put("IB", this.stubbsCard);
		alarm.put("JB", this.driverTime);
		alarm.put("KB", this.failedReason);
		alarm.put("WA", this.beginLat);
		alarm.put("XA", this.beginLng);
		alarm.put("YA", this.endLat);
		alarm.put("ZA", this.endLNG);
		return alarm;
	}
	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverIdCode() {
		return driverIdCode;
	}
	public void setDriverIdCode(String driverIdCode) {
		this.driverIdCode = driverIdCode;
	}
	public String getDriverCertificate() {
		return driverCertificate;
	}
	public void setDriverCertificate(String driverCertificate) {
		this.driverCertificate = driverCertificate;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getCertificateValid() {
		return certificateValid;
	}
	public void setCertificateValid(String certificateValid) {
		this.certificateValid = certificateValid;
	}
	public long getCreditDate() {
		return creditDate;
	}
	public void setCreditDate(long creditDate) {
		this.creditDate = creditDate;
	}
	public long getStubbsCard() {
		return stubbsCard;
	}
	public void setStubbsCard(long stubbsCard) {
		this.stubbsCard = stubbsCard;
	}
	public long getDriverTime() {
		return driverTime;
	}
	public void setDriverTime(long driverTime) {
		this.driverTime = driverTime;
	}
	public String getFailedReason() {
		return failedReason;
	}
	public void setFailedReason(String failedReason) {
		this.failedReason = failedReason;
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
	public int getEndLNG() {
		return endLNG;
	}
	public void setEndLNG(int endLNG) {
		this.endLNG = endLNG;
	}
	@Override
	public void dbObjectToBean(DBObject dbObjectDetail) {
		this.terminalId=(long) dbObjectDetail.get("AA");
		this.driverName=(String) dbObjectDetail.get("CB");
		this.driverIdCode=(String) dbObjectDetail.get("DB");
		this.driverCertificate=(String) dbObjectDetail.get("EB");
		this.agencyName=(String) dbObjectDetail.get("FB");
		this.certificateValid=(String) dbObjectDetail.get("GB");
		this.creditDate=(long) dbObjectDetail.get("HB");
		this.stubbsCard=(long) dbObjectDetail.get("IB");
		this.driverTime=(long) dbObjectDetail.get("JB");
		this.failedReason=(String) dbObjectDetail.get("KB");
		this.beginLat=(int) dbObjectDetail.get("WA");
		this.beginLng=(int) dbObjectDetail.get("XA");
		this.endLat=(int) dbObjectDetail.get("YA");
		this.endLNG=(int) dbObjectDetail.get("ZA");

	}








}
