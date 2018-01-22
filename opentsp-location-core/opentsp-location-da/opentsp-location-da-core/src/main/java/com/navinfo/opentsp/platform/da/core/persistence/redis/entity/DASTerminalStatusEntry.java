package com.navinfo.opentsp.platform.da.core.persistence.redis.entity;

import java.io.Serializable;

public class DASTerminalStatusEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	/***
	 * 业务系统设计的报警类型
	 *
	 * @author jinshan
	 *
	 */

	//超速报警
	private long overspeed;
	//进出区域报警
	private long areaIOut;
	//区域超速报警
	private long areaOspd;
	//ACC状态
	private long accStatus;
	//驾驶员登录信息
	private long driverInfor;
	//电源断电
	private long elecPowerOff;
	//紧急报警
	private long emergncy;
	//疲劳驾驶
	private long fatigue;
	//超时停车
	private long otparking;
	//主电源欠压
	private long powerInLow;
	//路线超速报警
	private long rlOverspd;
	//路段超时报警
	private long rlOverTime;
	//进出线路报警
	private long routeIOut;
	//终端上下线状态
	private long terOnOffline;
	//流量统计
	private long wf;
	//里程统计
	private long mileage;
	public long getOverspeed() {
		return overspeed;
	}
	public void setOverspeed(long overspeed) {
		this.overspeed = overspeed;
	}
	public long getAreaIOut() {
		return areaIOut;
	}
	public void setAreaIOut(long areaIOut) {
		this.areaIOut = areaIOut;
	}
	public long getAreaOspd() {
		return areaOspd;
	}
	public void setAreaOspd(long areaOspd) {
		this.areaOspd = areaOspd;
	}
	public long getAccStatus() {
		return accStatus;
	}
	public void setAccStatus(long accStatus) {
		this.accStatus = accStatus;
	}
	public long getDriverInfor() {
		return driverInfor;
	}
	public void setDriverInfor(long driverInfor) {
		this.driverInfor = driverInfor;
	}
	public long getElecPowerOff() {
		return elecPowerOff;
	}
	public void setElecPowerOff(long elecPowerOff) {
		this.elecPowerOff = elecPowerOff;
	}
	public long getEmergncy() {
		return emergncy;
	}
	public void setEmergncy(long emergncy) {
		this.emergncy = emergncy;
	}
	public long getFatigue() {
		return fatigue;
	}
	public void setFatigue(long fatigue) {
		this.fatigue = fatigue;
	}
	public long getOtparking() {
		return otparking;
	}
	public void setOtparking(long otparking) {
		this.otparking = otparking;
	}
	public long getPowerInLow() {
		return powerInLow;
	}
	public void setPowerInLow(long powerInLow) {
		this.powerInLow = powerInLow;
	}
	public long getRlOverspd() {
		return rlOverspd;
	}
	public void setRlOverspd(long rlOverspd) {
		this.rlOverspd = rlOverspd;
	}
	public long getRlOverTime() {
		return rlOverTime;
	}
	public void setRlOverTime(long rlOverTime) {
		this.rlOverTime = rlOverTime;
	}
	public long getRouteIOut() {
		return routeIOut;
	}
	public void setRouteIOut(long routeIOut) {
		this.routeIOut = routeIOut;
	}
	public long getTerOnOffline() {
		return terOnOffline;
	}
	public void setTerOnOffline(long terOnOffline) {
		this.terOnOffline = terOnOffline;
	}
	public long getWf() {
		return wf;
	}
	public void setWf(long wf) {
		this.wf = wf;
	}
	public long getMileage() {
		return mileage;
	}
	public void setMileage(long mileage) {
		this.mileage = mileage;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}
