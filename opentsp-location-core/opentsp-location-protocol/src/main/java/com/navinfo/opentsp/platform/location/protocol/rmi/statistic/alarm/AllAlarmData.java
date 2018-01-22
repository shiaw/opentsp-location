package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;


/*********************
 * 所有报警汇总统计 
 * 
 * 
 * @author claus
 *
 */
public class AllAlarmData implements Serializable{
	private static final long serialVersionUID = 1L;
	private long terminalID;
	private int emergecy;                    //1009
	private int powerSIW;                    //1010
	private int electricPOff;                //1011
	private int	areaInOut;                   //1002
	private int	areaOS;                      //1003
	private int overSpd;                     //1001
	private int routeIO;                     //1004
	private int	offRLine;                    //1104
	private int rLineOSpd;                   //1005
	private int rLineOT;                     //1006
	private int overTP;                      //1007
	private int fatigue;                     //1008
	
	public AllAlarmData(){}
	
	public AllAlarmData(long tid, 
			            int emergyCount, 
			            int powerSIWCount, 
			            int elePOffCount, 
			            int areaIOCount, 
			            int areaOSCount,
			            int ospdCount,
			            int routeIOCount,
			            int routeDeviation,
			            int offRLCount,
			            int rLSCount,
			            int rLOTCount,
			            int otpCount,
			            int fatigeCount,
			            int driverCount,
			            int wflowCount,
			            int mileageCount){
		terminalID = tid;
		emergecy = emergyCount;
		powerSIW = powerSIWCount;
		electricPOff = elePOffCount;
		areaInOut = areaIOCount;
		areaOS = areaOSCount;
		overSpd = ospdCount;
		routeIO = routeIOCount;
		routeDeviation = routeDeviation;
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
	
	@Override
	public String toString() {
		return "AllAlarmData [terminalID=" + terminalID + ", emergecy="
				+ emergecy + ", powerSIW=" + powerSIW + ", electricPOff="
				+ electricPOff + ", areaInOut=" + areaInOut + ", areaOS="
				+ areaOS + ", overSpd=" + overSpd + ", routeIO=" + routeIO
				+ ", offRLine=" + offRLine + ", rLineOSpd=" + rLineOSpd
				+ ", rLineOT=" + rLineOT + ", overTP=" + overTP + ", fatigue="
				+ fatigue + ", dirverLoginout=" +"]";
	}
}
