package com.navinfo.opentsp.platform.location.kit.servicemonitor.mode;

import java.io.Serializable;

/**
 * 各分区信息.
 */
public class Partition implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 分区总容量. */
	private String diskSize;
	/** 分区总的已使用空间大小. usedSize = diskSize - freeSize. */
	private String usedSize;
	/** 分区总的可用空间大小. availableSize <= freeSize,指在当前用户权限下可使用的空间大小. */
	private String useableSize;
	/** 分区总的未使用空间大小. freeSize = diskSize - usedSize */
	private String freeSize;
	/** 分区已使用的百分比. */
	private String usedPerc;
	/** 分区挂载点(可理解为分区名,如Linux:{/,/swap,/home}; Windows:{C:\,D:\}). */
	private String mountPoint;
	/**
	 * 分区的文件类型,常见如Linux:{ext<i>n</i>,ReiserFS,JFS,XFS};
	 * Windows:{NTFS,FAT32,FAT}; 基于网络的:NFS等.
	 */
	private String type;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(110);
		sb.append("\n[" + getMountPoint() + "]: \n");
		sb.append("分区容量: " + getDiskSize() + ", 文件类型: " + getType() + "\n");
		sb.append("已使用容量: " + getUsedSize() + ", 使用率为: " + getUsedPerc() + "\n");
		sb.append("空闲容量: " + getFreeSize() + ", 其中可用容量: " + getUseableSize());
		return sb.toString();
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

	public String getMountPoint() {
		return mountPoint;
	}

	public void setMountPoint(String mountPoint) {
		this.mountPoint = mountPoint;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
