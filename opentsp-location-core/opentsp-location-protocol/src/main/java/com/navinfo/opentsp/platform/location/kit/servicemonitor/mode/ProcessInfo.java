package com.navinfo.opentsp.platform.location.kit.servicemonitor.mode;

import java.io.Serializable;

public class ProcessInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 当前所有进程数. */
	private long totalProcesses;
	/** 当前所有线程数. */
	private long totalThreads;
	/** 当前运行状态进程数. */
	private long running;
	/** 当前休眠状态进程数. */
	private long sleeping;
	/** 当前僵死状态进程数. */
	private long zombie;
	/** 当前空闲状态进程数. */
	private long idle;
	/** 当前停止状态进程数. */
	private long stopped;

	// /** 当前top进程(预留). */
	// private Process top;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(110);
		sb.append("---系统当前进程信息---\n");
		sb.append(getTotalProcesses() + " processes: " + getSleeping()
				+ " sleeping, " + getRunning() + " running, " + getZombie()
				+ " zombie, " + getStopped() + " stopped, " + getIdle()
				+ " idle... " + getTotalThreads() + " threads.\n");
		return sb.toString();
	}

	public long getTotalProcesses() {
		return totalProcesses;
	}

	public void setTotalProcesses(long totalProcesses) {
		this.totalProcesses = totalProcesses;
	}

	public long getTotalThreads() {
		return totalThreads;
	}

	public void setTotalThreads(long totalThreads) {
		this.totalThreads = totalThreads;
	}

	public long getRunning() {
		return running;
	}

	public void setRunning(long running) {
		this.running = running;
	}

	public long getSleeping() {
		return sleeping;
	}

	public void setSleeping(long sleeping) {
		this.sleeping = sleeping;
	}

	public long getZombie() {
		return zombie;
	}

	public void setZombie(long zombie) {
		this.zombie = zombie;
	}

	public long getIdle() {
		return idle;
	}

	public void setIdle(long idle) {
		this.idle = idle;
	}

	public long getStopped() {
		return stopped;
	}

	public void setStopped(long stopped) {
		this.stopped = stopped;
	}
}
