package com.navinfo.opentsp.platform.location.kit.servicemonitor.mode;

import java.io.Serializable;
import java.util.List;

/**
 * 总硬盘信息.没有包含磁盘制造商信息、型号(sda,hda...)、数量(sda1,sda2...)等.
 */
public class HardDiskInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 所有的分区列表. */
	private List<Partition> rootList;
	/** 磁盘总容量.注意不包括还未分区/挂载的部分容量,也不包括双系统下(如Windows+Linux)不能识别的分区容量. */
	private String diskSize;
	/** 磁盘总的已使用空间大小. usedSize = diskSize - freeSize. */
	private String usedSize;
	/** 磁盘总的可用空间大小. availableSize <= freeSize,指在当前用户权限下可使用的空间大小. */
	private String useableSize;
	/** 磁盘总的未使用空间大小. freeSize = diskSize - usedSize */
	private String freeSize;
	/** 磁盘已使用的百分比. */
	private String usedPerc;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(600);
		sb.append("---总磁盘空间报告---\n");
		sb.append("磁盘总容量: " + getDiskSize() + "\t//注意不包含未挂载或不能识别的容量\n");
		sb.append("已使用容量: " + getUsedSize() + ", 使用率为: " + getUsedPerc() + "\n");
		sb.append("空闲容量: " + getFreeSize() + ", 其中可用容量: " + getUseableSize()
				+ "\n");
		if (rootList.size() > 1) {
			sb.append("---各分区空间报告---");
			for (int i = 0; i < rootList.size(); i++) {
				Partition part = rootList.get(i);
				sb.append(part.toString());
			}
		}
		return sb.toString();
	}

	public List<Partition> getRootList() {
		return rootList;
	}

	public void setRootList(List<Partition> rootList) {
		this.rootList = rootList;
	}

	public String getDiskSize() {
		return diskSize;
	}

	public void setDiskSize(String diskSize) {
		this.diskSize = diskSize;
	}

	public String getUsedSize() {
		return usedSize;
	}

	public void setUsedSize(String usedSize) {
		this.usedSize = usedSize;
	}

	public String getUseableSize() {
		return useableSize;
	}

	public void setUseableSize(String useableSize) {
		this.useableSize = useableSize;
	}

	public String getFreeSize() {
		return freeSize;
	}

	public void setFreeSize(String freeSize) {
		this.freeSize = freeSize;
	}

	public String getUsedPerc() {
		return usedPerc;
	}

	public void setUsedPerc(String usedPerc) {
		this.usedPerc = usedPerc;
	}
}
