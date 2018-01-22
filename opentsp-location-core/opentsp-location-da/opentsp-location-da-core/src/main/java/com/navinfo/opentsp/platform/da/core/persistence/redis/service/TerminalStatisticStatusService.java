package com.navinfo.opentsp.platform.da.core.persistence.redis.service;

import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;

import java.util.Set;


public interface TerminalStatisticStatusService {
	
	public LCPlatformResponseResult.PlatformResponseResult saveStatus(int type, long terminalId);
	
	public Set<Long> getTerminalsByType(int type);
	
	public boolean removeLastDayStatus(int type);
}
