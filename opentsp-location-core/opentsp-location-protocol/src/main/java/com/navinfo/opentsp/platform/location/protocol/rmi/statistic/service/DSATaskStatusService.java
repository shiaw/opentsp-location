package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DSATaskStatusService{

	public Map<Long,Integer> getDSATaskStatusData(List<Long> terminalIsds)  ;

	void saveTerminalDSATaskStatus(long terminalId)  ;

	/**
	 * 查询DP保存的今天发生的滞留超时终端
	 * @param type
	 * @return
	 */
	public Set<Long> getStatisticByType(int type)  ;
}
