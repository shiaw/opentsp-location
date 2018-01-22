package com.navinfo.opentsp.platform.location.kit.servicemonitor.mode;

import java.io.Serializable;
import java.util.List;

/**
 * 总Cpu信息,包含硬件制造信息及实时<em>CPU时间</em>使用率信息.<br>
 * 
 * CPU时间 = user + system + nice + idle + wait + irq [+ softIrq + steal].
 */
public class CpuInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 生产厂商 */
	private String vendor;
	/** 型号 */
	private String model;
	/** CPU频率,1.0GHz以上以GHz显示,否则以MHz显示 */
	private String hz;
	/**
	 * 逻辑cores数,totalCores = totalSockes * coresPerSocket,
	 * 这里应该不考虑Hyper-Thread(超线程)的1倍提升.
	 */
	private int totalCores;
	/** 物理CPU个数,单核:single/双核:dual/四核:quad/其它:multi-core */
	private int totalSockets;
	/**
	 * 单个物理CPU上cores个数.<br>
	 * 注意hyperc-sigar中对于totalSockets和coresPerSocket的获取有bug,在Windows和Linux下不一致.
	 */
	private int coresPerSocket;
	/**cpu汇总信息*/
	private Cpu totalCpu;
	/** 所包含所有CPU的列表,获取单个CPU状态需要逐个查询. */
	private List<Cpu> cpuList;


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(600);
		sb.append("---硬件信息---\n");
		sb.append("制造商 & 型号: " + getVendor() + " " + getModel() + "\n");
		sb.append("主频: " + getHz() + "\n");
		sb.append("物理CPU数: " + getTotalSockets() + "("
				+ MonitorUtil.coreConvension(getTotalCores()) + "), 逻辑CPU数: "
				+ getTotalCores() + "\n");
		sb.append("---总CPU实时占用率---\n");
		sb.append("总使用率: " + this.totalCpu.getCpuRatio() + ", 其中: \n");
		sb.append("\t用户进程占用: " + this.totalCpu.getUserPerc() + "\n");
		sb.append("\t系统进程占用: " + this.totalCpu.getSysPerc() + "\n");
		sb.append("\tI/O等待占用: " + this.totalCpu.getWaitPerc() + "\n");
		sb.append("空闲: " + this.totalCpu.getIdlePerc() + "\n");
		if (getTotalSockets() > 1) {
			sb.append("---各CPU实时占用率---");
			int i = 0;
			for (Cpu cpu : getCpuList()) {
				sb.append("\n[CPU " + i++ + "]: \n");
				sb.append(cpu.toString());
			}
		}
		return sb.toString();
	}

	public List<Cpu> getCpuList() {
		return cpuList;
	}

	public void setCpuList(List<Cpu> cpuList) {
		this.cpuList = cpuList;
	}
	/** 生产厂商 */
	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	/** 型号 */
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	/** CPU频率,1.0GHz以上以GHz显示,否则以MHz显示 */
	public String getHz() {
		return hz;
	}

	public void setHz(String hz) {
		this.hz = hz;
	}
	/**
	 * 逻辑cores数,totalCores = totalSockes * coresPerSocket,
	 * 这里应该不考虑Hyper-Thread(超线程)的1倍提升.
	 */
	public int getTotalCores() {
		return totalCores;
	}

	public void setTotalCores(int totalCores) {
		this.totalCores = totalCores;
	}
	/** 物理CPU个数,单核:single/双核:dual/四核:quad/其它:multi-core */
	public int getTotalSockets() {
		return totalSockets;
	}

	public void setTotalSockets(int totalSockets) {
		this.totalSockets = totalSockets;
	}
	/**
	 * 单个物理CPU上cores个数.<br>
	 * 注意hyperc-sigar中对于totalSockets和coresPerSocket的获取有bug,在Windows和Linux下不一致.
	 */
	public int getCoresPerSocket() {
		return coresPerSocket;
	}

	public void setCoresPerSocket(int coresPerSocket) {
		this.coresPerSocket = coresPerSocket;
	}

	public Cpu getTotalCpu() {
		return totalCpu;
	}

	public void setTotalCpu(Cpu totalCpu) {
		this.totalCpu = totalCpu;
	}

	
}
