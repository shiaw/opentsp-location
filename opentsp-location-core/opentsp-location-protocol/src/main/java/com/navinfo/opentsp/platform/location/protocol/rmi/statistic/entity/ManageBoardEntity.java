package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity;

import java.io.Serializable;

public class ManageBoardEntity implements Serializable{
	private static final long serialVersionUID = 7544010545899890885L;
	private long _id;
	private long terminal_id;
	private int accTime;//acc时长
	private int mileage;//总里程
	private int overSpeedTimes;//超速次数
	private int fatigueTimes;//疲劳驾驶次数
	private int oilConsumption;//油耗
	public long getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(long terminal_id) {
		this.terminal_id = terminal_id;
	}
	public int getAccTime() {
		return accTime;
	}
	public void setAccTime(int accTime) {
		this.accTime = accTime;
	}
	public int getMileage() {
		return mileage;
	}
	public void setMileage(int mileage) {
		this.mileage = mileage;
	}
	public int getOverSpeedTimes() {
		return overSpeedTimes;
	}
	public void setOverSpeedTimes(int overSpeedTimes) {
		this.overSpeedTimes = overSpeedTimes;
	}
	public int getFatigueTimes() {
		return fatigueTimes;
	}
	public int getOilConsumption() {
		return oilConsumption;
	}
	public void setOilConsumption(int oilConsumption) {
		this.oilConsumption = oilConsumption;
	}
	public void setFatigueTimes(int fatigueTimes) {
		this.fatigueTimes = fatigueTimes;
	}
	@Override
	public String toString() {
		return "ManageBoardEntity [_id=" + _id + ", terminal_id=" + terminal_id
				+ ", accTime=" + accTime + ", mileage=" + mileage
				+ ", overSpeedTimes=" + overSpeedTimes + ", fatigueTimes="
				+ fatigueTimes + ", oilConsumption=" + oilConsumption + "]";
	}
	
}
