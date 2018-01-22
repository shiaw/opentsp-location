package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DASTerminalStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	//终端ID
	private long terminalId;
	//超速报警1
	private long overspeed;
	//进出区域报警
	private long areaIOut;
	//区域超速报警
	private long areaOspd;
	//路段超速报警
	private long rlOverspd;
	//路段超时报警
	private long rlOverTime;
	//进出线路报警
	private long routeIOut;
	//偏离路线
	private long routeDeviate;
	//ACC状态
	private long accStatus;
	//驾驶员登录信息
	private long driverInfor;
	//电源断电
	private long elecPowerOff;
	//主电源欠压
	private long powerInLow;
	//紧急报警
	private long emergncy;
	//疲劳驾驶
	private long fatigue;
	//终端上下线状态
	private long terOnOffline;
	//超时停车
	private long overtimeParking;
	//流量统计 
	private long wf;
	//滞留超时
	private long overtimePark;
	//里程统计 
	private long mileage;
	//统计系统当前时间
	private int computerDate;
	//统计结果状态
	private int status;
	//时间序列
	private List<Long> times = new ArrayList<Long>();
	
	public long getMintime(){
		long min = Long.MAX_VALUE;
		for(int i=0; i<times.size(); i++){
			if(min > times.get(i)) {
				min = times.get(i);
			}
		}
		return min;
	}
	
	public long getPowerInLow() {
		return powerInLow;
	}
	
	public long getRouteDeviate() {
		return routeDeviate;
	}

	public void setRouteDeviate(long routeDeviate) {
		this.routeDeviate = routeDeviate;
	}

	
    public void setPowerInLow(long powerInLow) {
    	times.add(powerInLow);
		this.powerInLow = powerInLow;
	}
	public long getTerOnOffline() {
		return terOnOffline;
	}
	public void setTerOnOffline(long terOnOffline) {
		times.add(terOnOffline);
		this.terOnOffline = terOnOffline;
	}
	public long getWf() {
		return wf;
	}
	public void setWf(long wf) {
		times.add(wf);
		this.wf = wf;
	}
	public long getMileage() {
		return mileage;
	}
	public void setMileage(long mileage) {
		times.add(mileage);
		this.mileage = mileage;
	}
	
	public long getAccStatus() {
		return accStatus;
	}
	public void setAccStatus(long accStatus) {
		times.add(accStatus);
		this.accStatus = accStatus;
	}
	public long getDriverInfor() {
		return driverInfor;
	}
	public void setDriverInfor(long driverInfor) {
		times.add(driverInfor);
		this.driverInfor = driverInfor;
	}
	public long getElecPowerOff() {
		return elecPowerOff;
	}
	public void setElecPowerOff(long elecPowerOff) {
		times.add(elecPowerOff);
		this.elecPowerOff = elecPowerOff;
	}
	public long getEmergncy() {
		return emergncy;
	}
	public void setEmergncy(long emergncy) {
		times.add(emergncy);
		this.emergncy = emergncy;
	}
	public long getFatigue() {
		return fatigue;
	}
	public void setFatigue(long fatigue) {
		times.add(fatigue);
		this.fatigue = fatigue;
	}
	public long getOvertimePark() {
		return overtimePark;
	}
	public void setOvertimePark(long overtimePark) {
		this.overtimePark = overtimePark;
	}
	public int getComputerDate() {
		return computerDate;
	}
	public void setComputerDate(int computerDate) {
		this.computerDate = computerDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}
	public long getOvertimeParking() {
		return overtimeParking;
	}
	public void setOvertimeParking(long overtimeParking) {
		times.add(overtimeParking);
		this.overtimeParking = overtimeParking;
	}
	public long getOverspeed() {
		return overspeed;
	}
	public void setOverspeed(long overspeed) {
		times.add(overspeed);
		this.overspeed = overspeed;
	}
	public long getAreaIOut() {
		return areaIOut;
	}
	public void setAreaIOut(long areaIOut) {
		times.add(areaIOut);
		this.areaIOut = areaIOut;
	}
	public long getAreaOspd() {
		return areaOspd;
	}
	public void setAreaOspd(long areaOspd) {
		times.add(areaOspd);
		this.areaOspd = areaOspd;
	}
	public long getRlOverspd() {
		return rlOverspd;
	}

	public void setRlOverspd(long rlOverspd) {
		times.add(rlOverspd);
		this.rlOverspd = rlOverspd;
	}

	public long getRlOverTime() {
		return rlOverTime;
	}

	public void setRlOverTime(long rlOverTime) {
		times.add(rlOverTime);
		this.rlOverTime = rlOverTime;
	}

	public long getRouteIOut() {
		return routeIOut;
	}

	public void setRouteIOut(long routeIOut) {
		times.add(routeIOut);
		this.routeIOut = routeIOut;
	}
}
