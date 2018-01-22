package com.navinfo.opentsp.platform.dp.core.cache.entity;

import java.io.Serializable;


public class DataAccessCacheEntry implements Serializable {

	private static final long serialVersionUID = -5150326483617094744L;
	
	private long createTime;
	private int timeout = 600;
	private int objectType;
	private Object object;
	private long lastUpdateTime;
	public DataAccessCacheEntry() {
		super();
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public int getObjectType() {
		return objectType;
	}
	public void setObjectType(int objectType) {
		this.objectType = objectType;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}