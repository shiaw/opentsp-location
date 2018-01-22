package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;

/**
 * 流量统计信息
 * @author hws
 *
 */
public class TotalAlarmInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private int	type;//	用协议定义的报警类型复制
	private int	continuousTime;//	报警持续时间长度
	private int	counts;//	报警次数
	public TotalAlarmInfo(){
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
	@Override
	public String toString() {
		return "TotalAlarmInfo [type=" + type + ", continuousTime="
				+ continuousTime + ", counts=" + counts + "]";
	}

	
}
