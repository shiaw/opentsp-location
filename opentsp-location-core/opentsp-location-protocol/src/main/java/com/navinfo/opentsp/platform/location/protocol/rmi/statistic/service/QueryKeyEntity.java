package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service;

import java.io.Serializable;

public class QueryKeyEntity implements Serializable{
	public String queryKey;
	
	public long nodeCode;
	
	public long lastUpdateTime;

	public String getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public long getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(long nodeCode) {
		this.nodeCode = nodeCode;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
