package com.navinfo.opentsp.platform.da.core.rmi.impl.district.query.overspeed;

import com.navinfo.opentsp.platform.da.core.rmi.impl.district.query.AlarmQueryService;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;

import java.util.List;


public class OverspeedAlarm extends AlarmQueryService {

	@Override
	public void getAlarmSummary(List<Long> terminalIds,
			long startDate, long endDate, CommonParameter comParameter,String queryKey) {

	}

	@Override
	public void getAlarmRecords() {

	}

	@Override
	public void saveStatisticsInfo() {
		// TODO Auto-generated method stub
		
	}

}
