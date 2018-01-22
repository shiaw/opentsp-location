package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;

/**
 * 驶员登录统计信息
 * @author hws
 *
 */
public class DriverLogin implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
    
	public DriverLogin(){
		
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
	
	
	
	
	
	
	

}
