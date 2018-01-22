package com.navinfo.opentsp.platform.da.core.cache.alarm;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DADriverLogin;

import java.util.List;



/***************************
 * 多终端报警详情分页缓存
 * 
 * @author claus
 *
 */
public class DriverLoginCache extends AlarmDefaultCache<DADriverLogin>{

	public DriverLoginCache(List<Long> terminalId, String queryKey) {
		super(terminalId,queryKey);
	}

}
