package com.navinfo.opentsp.platform.da.core.webService.manage;

import java.io.Serializable;

/**
 * 封装处理的实体
 *
 * @author ss
 *
 */
public class SynchronousDataModel implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3711794863205303643L;

	/* 处理数据实体 */
	private byte[] object;
	/* 区域 */
	private String district;
	/* 操作类型：新增、修改、删除 */
	private String operationType;
	/* webservice处理类Class */
	private String webserviceClass;
	/*模块名称*/
	private String moduleName;
	/*自身类型*/
	private String type;
	/*删除操作时的实体类主键*/
	private String primaryKeys;
	/*操作时间*/
	private long operationDate;
	/*数据状态，1:同步成功，0:同步失败，2：同步中*/
	private int status;
	public byte[] getObject() {
		return object;
	}
	public void setObject(byte[] object) {
		this.object = object;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getWebserviceClass() {
		return webserviceClass;
	}
	public void setWebserviceClass(String webserviceClass) {
		this.webserviceClass = webserviceClass;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPrimaryKeys() {
		return primaryKeys;
	}
	public void setPrimaryKeys(String primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
	public long getOperationDate() {
		return operationDate;
	}
	public void setOperationDate(long operationDate) {
		this.operationDate = operationDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}



}
