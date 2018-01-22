package com.navinfo.opentsp.platform.da.core.cache.alarm;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DACommonSummaryEntity;

import java.util.List;



/***************************
 * 报警概要分页缓存
 * 
 * @author claus
 *
 */
public class AlarmSummaryCache extends AlarmDefaultCache<DACommonSummaryEntity>{

	public AlarmSummaryCache(List<Long> terminalId, String queryKey) {
		super(terminalId, queryKey);
		
	}

}
