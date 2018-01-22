package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;


/*********************
 * 所有报警汇总统计
 *
 *
 * @author claus
 *
 */
public class AllAlarmData {

	private long terminalID;
	private int emergecy;
	private int powerSIW;
	private int electricPOff;
	private int	areaInOut;
	private int	areaOS;
	private int overSpd;
	private int routeIO;
	private int	offRLine;
	private int rLineOSpd;
	private int rLineOT;
	private int overTP;
	private int fatigue;
	
	
	public AllAlarmData(){}
	
	public AllAlarmData(long tid, 
			            int emergyCount, 
			            int powerSIWCount, 
			            int elePOffCount, 
			            int areaIOCount, 
			            int areaOSCount,
			            int ospdCount,
			            int routeIOCount,
			            int offRLCount,
			            int rLSCount,
			            int rLOTCount,
			            int otpCount,
			            int fatigeCount){
		terminalID = tid;
		emergecy = emergyCount;
		powerSIW = powerSIWCount;
		electricPOff = elePOffCount;
		areaInOut = areaIOCount;
		areaOS = areaOSCount;
		overSpd = ospdCount;
		routeIO = routeIOCount;
		offRLine = offRLCount;
		rLineOSpd = rLSCount;
		rLineOT = rLOTCount;
		overTP = otpCount;
		fatigue = fatigeCount;
	}
	public long getTerminalID() {
		return terminalID;
	}
	public void setTerminalID(long terminalID) {
		this.terminalID = terminalID;
	}
	public int getEmergecy() {
		return emergecy;
	}
	public void setEmergecy(int emergecy) {
		this.emergecy = emergecy;
	}
	public int getPowerSIW() {
		return powerSIW;
	}
	public void setPowerSIW(int powerSIW) {
		this.powerSIW = powerSIW;
	}
	public int getElectricPOff() {
		return electricPOff;
	}
	public void setElectricPOff(int electricPOff) {
		this.electricPOff = electricPOff;
	}
	public int getAreaInOut() {
		return areaInOut;
	}
	public void setAreaInOut(int areaInOut) {
		this.areaInOut = areaInOut;
	}
	public int getAreaOS() {
		return areaOS;
	}
	public void setAreaOS(int areaOS) {
		this.areaOS = areaOS;
	}
	public int getOverSpd() {
		return overSpd;
	}
	public void setOverSpd(int overSpd) {
		this.overSpd = overSpd;
	}
	public int getRouteIO() {
		return routeIO;
	}
	public void setRouteIO(int routeIO) {
		this.routeIO = routeIO;
	}
	public int getOffRLine() {
		return offRLine;
	}
	public void setOffRLine(int offRLine) {
		this.offRLine = offRLine;
	}
	public int getrLineOSpd() {
		return rLineOSpd;
	}
	public void setrLineOSpd(int rLineOSpd) {
		this.rLineOSpd = rLineOSpd;
	}
	public int getrLineOT() {
		return rLineOT;
	}
	public void setrLineOT(int rLineOT) {
		this.rLineOT = rLineOT;
	}
	public int getOverTP() {
		return overTP;
	}
	public void setOverTP(int overTP) {
		this.overTP = overTP;
	}
	public int getFatigue() {
		return fatigue;
	}
	public void setFatigue(int fatigue) {
		this.fatigue = fatigue;
	}	
}
