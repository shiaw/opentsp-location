package com.navinfo.opentsp.platform.da.core.rmi.impl.district.query;

import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;

import java.util.List;


public abstract class AlarmQueryService {
	
	
	abstract  public void getAlarmSummary(List<Long> terminalIds,
			long startDate, long endDate, CommonParameter comParameter,String queryKey);
	
	abstract  public void getAlarmRecords();
	
	abstract  public void saveStatisticsInfo();
}
