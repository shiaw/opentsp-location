package com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao;

import java.util.List;
import java.util.Map;

import  com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDataEntityDB;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDetailedEntityDB;

public interface LocationMongodbService {
	/**
	 * 存储Gps位置数据<br>
	 * 一次存储某个终端一天的数据
	 *
	 * @param dataEntity
	 *            {@link GpsDataEntityDB}
	 */
	abstract boolean saveGpsData(GpsDataEntityDB dataEntity);

	/**
	 * 查询某终端历史位置数据<br>
	 * <b>注意：</b><br>
	 * 不支持跨月查询<br>
	 * 如需要跨月,上层调用者自动进行日期分隔
	 *
	 * @param terminalId
	 * @param begintime
	 * @param endTime
	 * @return
	 */
	public List<GpsDetailedEntityDB> findLocationData(long terminalId,
													  long begintime, long endTime,String month);

	/**
	 * 查询某终端历史位置数据<br>
	 * <b>注意：</b><br>
	 * 不支持跨月查询<br>
	 * 如需要跨月,上层调用者自动进行日期分隔
	 * @param terminalIds
	 * @param beginTime
	 * @param endTime
	 * @param month
	 * @return map<tid_day,gps>
	 */
	public Map<String, List<GpsDetailedEntityDB>> findLocationData(List<Long> terminalIds,
																   long beginTime, long endTime,String month);

	public List<GpsDetailedEntityDB> _get_redis_gps(long terminalId);

	public Map<String, List<GpsDetailedEntityDB>> getRedisLocationData(List<Long> terminalIds);
	public List<GpsDetailedEntityDB> _filter_gps(long beginTime , long endTime , List<GpsDetailedEntityDB> list);
	/**
	 * 从Redis查询位置数据,查询的是天为单位的数据块
	 *
	 * modified by zhangyj
	 * @param tids
	 * @param begin
	 * @param end
	 * @return
	 */
	public Map<Long, List<LocationData>> findRedisLocationData(List<Long> tids, long begin, long end);

}
