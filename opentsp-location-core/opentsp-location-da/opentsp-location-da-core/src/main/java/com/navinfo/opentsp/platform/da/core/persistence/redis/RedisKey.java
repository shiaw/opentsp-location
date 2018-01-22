package com.navinfo.opentsp.platform.da.core.persistence.redis;

import com.navinfo.opentsp.platform.location.kit.lang.DateUtils.DateFormat;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;

import java.util.Calendar;


public class RedisKey {
	public static String getGpsKey(long terminalId, String day) {
		return terminalId + "_" + day;
	}

	public static String getGpsTitleKey(Calendar calendar) {
		return RedisConstans.RedisKey.GPS_DATA_TITLE.name() + "_"
				+ DateUtils.format(calendar.getTime(), DateFormat.YYYYMMDD);
	}

	/**
	 *redis存储服务标识的时候不是用此方法获得key
	 */
	public static String getServiceUniqueMarkKey(int districtCode,
												 long uniqueMark) {
		return districtCode + "_" + uniqueMark;
	}

	public static String getLinkExceptionKey(int form, int to, int dataType) {
		return form + "_" + to + "_" + dataType;
	}

}
