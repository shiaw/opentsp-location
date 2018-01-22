package com.navinfo.opentsp.platform.location.kit.servicemonitor.mode;

import java.io.Serializable;

public class Memory implements Serializable {
	private static final long serialVersionUID = 1L;
	/** Jvm虚拟机当前占用内存, jvmTotalMemory = jvmFreeMemory + 已使用内存. */
	private int jvmTotalMemory;
	/** Jvm虚拟机当前空闲内存,注意该值不代表JVM实际可用内存; */
	private int jvmFreeMemory;
	/**Jvm当前已使用内存*/
	private int jvmUsedMemory;
	/**Jvm虚拟机最大可用内存(对应-Xmx选项,默认为物理内存1/4).*/
	private int jvmMaxMemory;
	/** 总的物理内存. */
	private int physicalTotalMemory;
	/** 剩余的物理内存. */
	private int physicalFreePhysicalMemory;
	/** 已使用的物理内存. */
	private int physicalUsedMemory;
	
	
	@Override
	public String toString() {
		return "Memory [jvmTotalMemory=" + jvmTotalMemory + ", jvmFreeMemory="
				+ jvmFreeMemory + ", jvmUsedMemory=" + jvmUsedMemory
				+ ", jvmMaxMemory=" + jvmMaxMemory + ", physicalTotalMemory="
				+ physicalTotalMemory + ", physicalFreePhysicalMemory="
				+ physicalFreePhysicalMemory + ", physicalUsedMemory="
				+ physicalUsedMemory + "]";
	}
	public int getJvmTotalMemory() {
		return jvmTotalMemory;
	}
	public void setJvmTotalMemory(int jvmTotalMemory) {
		this.jvmTotalMemory = jvmTotalMemory;
	}
	public int getJvmFreeMemory() {
		return jvmFreeMemory;
	}
	public void setJvmFreeMemory(int jvmFreeMemory) {
		this.jvmFreeMemory = jvmFreeMemory;
	}
	public int getJvmUsedMemory() {
		return jvmUsedMemory;
	}
	public void setJvmUsedMemory(int jvmUsedMemory) {
		this.jvmUsedMemory = jvmUsedMemory;
	}
	public int getJvmMaxMemory() {
		return jvmMaxMemory;
	}
	public void setJvmMaxMemory(int jvmMaxMemory) {
		this.jvmMaxMemory = jvmMaxMemory;
	}
	public int getPhysicalTotalMemory() {
		return physicalTotalMemory;
	}
	public void setPhysicalTotalMemory(int physicalTotalMemory) {
		this.physicalTotalMemory = physicalTotalMemory;
	}
	public int getPhysicalFreePhysicalMemory() {
		return physicalFreePhysicalMemory;
	}
	public void setPhysicalFreePhysicalMemory(int physicalFreePhysicalMemory) {
		this.physicalFreePhysicalMemory = physicalFreePhysicalMemory;
	}
	public int getPhysicalUsedMemory() {
		return physicalUsedMemory;
	}
	public void setPhysicalUsedMemory(int physicalUsedMemory) {
		this.physicalUsedMemory = physicalUsedMemory;
	}




}
