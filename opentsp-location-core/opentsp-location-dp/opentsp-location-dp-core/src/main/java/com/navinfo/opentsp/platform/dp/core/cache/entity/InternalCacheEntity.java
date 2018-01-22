package com.navinfo.opentsp.platform.dp.core.cache.entity;

import java.io.Serializable;

public class InternalCacheEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private long createTime;
	private int timeout = 600;
	private Object object;
	private long lastUpdateTime;

	public InternalCacheEntity() {
		this.createTime = System.currentTimeMillis() / 1000;
	}

	@Override
	public String toString() {
		return "InternalCacheEntity [id=" + id + ", createTime=" + createTime
				+ ", timeout=" + timeout + ", object=" + object
				+ ", lastUpdateTime=" + lastUpdateTime + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
		this.lastUpdateTime = System.currentTimeMillis() / 1000;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}
