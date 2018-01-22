package com.navinfo.opentsp.platform.da.core.persistence.entity.extend;

import java.io.Serializable;

public class MediaFileModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2265632124538543057L;
	/*文件类型*/
	private Integer fileType;
	/*文件*/
	private byte[] file;
	/*位置数据*/
	private byte[] locationData;
	public Integer getFileType() {
		return fileType;
	}
	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
	public byte[] getLocationData() {
		return locationData;
	}
	public void setLocationData(byte[] locationData) {
		this.locationData = locationData;
	}
	
}
