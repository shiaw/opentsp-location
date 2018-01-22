package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;


/**
 * 停滞超时（部标超时停车）报警
 * @author hk
 *
 */
public class StagnationTimeoutEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String _id;//数据标识
	private long terminalId;//终端标识号（索引）
	private long	beginDate;//	报警开始时间
	private long	endDate	;//报警结束时间
	private int	continuousTime;//	报警持续时间长度
	private int	limitParking ;// 	停车时间阀值
	private int	beginLat;//	开始纬度
	private int	beginLng;//	开始经度
	private int status;//状态，1：正常；0：撤销
	private int headMerge;//数据合并首标识 0:需要合并;1:统计完结
	private int tailMerge;//数据合并尾标识 0:需要合并;1:统计完结
	
	public StagnationTimeoutEntity(){
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getHeadMerge() {
		return headMerge;
	}

	public void setHeadMerge(int headMerge) {
		this.headMerge = headMerge;
	}

	public int getTailMerge() {
		return tailMerge;
	}

	public void setTailMerge(int tailMerge) {
		this.tailMerge = tailMerge;
	}

	@Override
	public String toString() {
		return "StagnationTimeoutEntity [_id=" + _id + ", terminalId=" + terminalId + ", beginDate=" + beginDate
				+ ", endDate=" + endDate + ", continuousTime=" + continuousTime + ", limitParking=" + limitParking
				+ ", beginLat=" + beginLat + ", beginLng=" + beginLng + ", status=" + status + ", headMerge="
				+ headMerge + ", tailMerge=" + tailMerge + "]";
	}
}
