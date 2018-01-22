package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity;

public class OnlinePercentageEntity {
	
	private	int	terminalId;
	
	private	int	tatisticDay;
	
	private	int	 onlineDay;
	
	private	float	onlinePercentage;

	public int getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(int terminalId) {
		this.terminalId = terminalId;
	}

	public int getTatisticDay() {
		return tatisticDay;
	}

	public void setTatisticDay(int tatisticDay) {
		this.tatisticDay = tatisticDay;
	}

	public int getOnlineDay() {
		return onlineDay;
	}

	public void setOnlineDay(int onlineDay) {
		this.onlineDay = onlineDay;
	}

	public float getOnlinePercentage() {
		return onlinePercentage;
	}

	public void setOnlinePercentage(float onlinePercentage) {
		this.onlinePercentage = onlinePercentage;
	}
	
	
	


}
