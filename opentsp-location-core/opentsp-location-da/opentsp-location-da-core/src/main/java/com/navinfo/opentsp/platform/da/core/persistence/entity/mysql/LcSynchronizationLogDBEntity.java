package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;


import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;

/**
 * LcUser entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_SYNCHRONIZATIONLOG")
public class LcSynchronizationLogDBEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	@LCTransient
	@LCPrimaryKey
	private Integer id;
	/* 处理数据实体 */
	private byte[] operationObject;
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
	/*数据状态，1:同步成功，0:同步失败*/
	private int status;

	// Constructors

	/** default constructor */
	public LcSynchronizationLogDBEntity() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte[] getOperationObject() {
		return operationObject;
	}

	public void setOperationObject(byte[] operationObject) {
		this.operationObject = operationObject;
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