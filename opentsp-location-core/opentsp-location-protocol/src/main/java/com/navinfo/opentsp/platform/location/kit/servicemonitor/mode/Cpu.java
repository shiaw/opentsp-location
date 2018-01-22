package com.navinfo.opentsp.platform.location.kit.servicemonitor.mode;

import java.io.Serializable;

/**
 * 单个Cpu信息,包含实时<em>CPU时间</em>使用率信息.<br>
 * 
 * CPU时间 = user + system + nice + idle + wait + irq [+ softIrq + steal].
 */
public class Cpu implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 处理用户进程(实际的工作)的百分比. */
	private double userPerc;
	/** 处理系统进程/内核操作的百分比. */
	private double sysPerc;
	/** 处理re-nicing进程的百分比. */
	private double nicePerc;
	/** 空闲时间百分比. */
	private double idlePerc;
	/** 等待I/O操作的百分比. 值越高表示I/O总线有性能瓶颈. */
	private double waitPerc;
	/** 硬件Interrupt Request(中断请求)百分比,用于停止相关硬件工作状态. 值越高表示硬件瓶颈问题. */
	private double irqPerc;
	private double softIrqPerc; // unix & linux
	private double stealPerc; // unix & linux
	/** CPU使用率,由 user + system + nice + wait组成. */
	private double cpuRatio;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(110);
		sb.append("使用率: " + getCpuRatio() + ", 其中: \n");
		sb.append("\t用户进程占用: " + getUserPerc() + "\n");
		sb.append("\t系统进程占用: " + getSysPerc() + "\n");
		sb.append("\tI/O等待占用: " + getWaitPerc() + "\n");
		sb.append("空闲: " + getIdlePerc());
		return sb.toString();
	}
	/** 处理用户进程(实际的工作)的百分比. */
	public double getUserPerc() {
		return userPerc;
	}

	public void setUserPerc(double userPerc) {
		this.userPerc = userPerc;
	}
	/** 处理系统进程/内核操作的百分比. */
	public double getSysPerc() {
		return sysPerc;
	}

	public void setSysPerc(double sysPerc) {
		this.sysPerc = sysPerc;
	}
	/** 处理re-nicing进程的百分比. */
	public double getNicePerc() {
		return nicePerc;
	}

	public void setNicePerc(double nicePerc) {
		this.nicePerc = nicePerc;
	}
	/** 空闲时间百分比. */
	public double getIdlePerc() {
		return idlePerc;
	}

	public void setIdlePerc(double idlePerc) {
		this.idlePerc = idlePerc;
	}
	/** 等待I/O操作的百分比. 值越高表示I/O总线有性能瓶颈. */
	public double getWaitPerc() {
		return waitPerc;
	}

	public void setWaitPerc(double waitPerc) {
		this.waitPerc = waitPerc;
	}
	/** 硬件Interrupt Request(中断请求)百分比,用于停止相关硬件工作状态. 值越高表示硬件瓶颈问题. */
	public double getIrqPerc() {
		return irqPerc;
	}

	public void setIrqPerc(double irqPerc) {
		this.irqPerc = irqPerc;
	}

	public double getSoftIrqPerc() {
		return softIrqPerc;
	}

	public void setSoftIrqPerc(double softIrqPerc) {
		this.softIrqPerc = softIrqPerc;
	}

	public double getStealPerc() {
		return stealPerc;
	}

	public void setStealPerc(double stealPerc) {
		this.stealPerc = stealPerc;
	}
	/**CPU使用率,由 user + system + nice + wait组成*/
	public double getCpuRatio() {
		return cpuRatio;
	}

	public void setCpuRatio(double cpuRatio) {
		this.cpuRatio = cpuRatio;
	}

	
}
