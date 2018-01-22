package com.navinfo.opentsp.platform.da.core.persistence.redis.service;

import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempCanData;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempGpsDataEntry;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataReport.CANBUSDataReport;

import java.util.Calendar;
import java.util.List;
import java.util.Map;


public interface ILocationRedisService {
	/**
	 * 添加位置数据
	 *
	 * @param terminalId
	 * @param locationData
	 */
	abstract void savaNormalLocation(long terminalId, LocationData locationData);

	/**
	 * 提供给本地缓存,将本地缓存中的数据,转移到Redis当中
	 *
	 * @param nodeCode
	 *            {@link String} 节点编号
	 * @param list
	 *            {@link List}数据
	 */
	abstract void savaNormalLocation(String nodeCode, List<TempGpsDataEntry> list);

	public void saveCanData(String nodeCode, List<TempCanData> canDatas);

	/**
	 * 从redis中查找某终端某天数据<br>
	 * 不删除redis当中的数据
	 *
	 * @param terminalId
	 * @param calendar
	 * @return
	 */
	abstract List<LocationData> findNormalLocation(long terminalId, Calendar calendar);

	public Map<String, List<LocationData>> findCurrentDayLocations(List<Long> tids, String day);

	/*
	 * added by zhangyj
	 *
	 * modified by zhangyj
	 * 查询时间段内历史位置数据
	 */

	public Map<Long, List<LocationData>> findCurrentDayLocations(List<Long> tids, String day, long start, long end);

	/**
	 * 2016年4月21日新增方法，查询当天指定终端指定数据段的位置数据，segment取值范围为[0, 288)，5分钟一段，左闭右开区间
	 * 返回结果的KEY是REDIS内部KEY
	 *
	 * modified by zhangyj
	 *
	 * @param tids
	 * @param segment
	 * @return
	 */
	public Map<Long, List<LocationData>> findCurrentDaySegmentLocations(List<Long> tids, int segment);

	/**
	 * 2016年4月21日新增方法，查询当天指定终端指定数据段的位置数据，segment取值范围为[0, 288)，5分钟一段，左闭右开区间
	 * 返回结果的KEY是终端ID
	 *
	 * modified by zhangyj
	 *
	 * @param tids
	 * @param segment
	 * @return
	 */
	public Map<Long, List<LocationData>> findCurrentDayLocationsBySegment(List<Long> tids, int segment);

	/**
	 * 随机获取一个Gps数据key名称<br>
	 * 并从集合中移除
	 *
	 * @param calendar
	 * @return
	 */
	abstract String findGpsDataKey(Calendar calendar);

	/**
	 * 根据key从redis中获取数据集合<br>
	 *
	 * @param dataKey
	 *            {@link String} 数据key
	 * @param isDelete
	 *            {@link Boolean} 是否删除redis中的缓存数据
	 * @return
	 */
	abstract List<LocationData> findNormalLocation(String dataKey, boolean isDelete);

	/**
	 * 根据key从redis中删除元素<br>
	 *
	 * @param setName
	 * @param values
	 * @return
	 */
	abstract boolean removeGpsDataKey(String setName, Object values);

	/**
	 * 从redis中删除位置数据
	 *
	 * @param dataKey
	 *            格式为[终端ID_YYYYMMDD]
	 * @return
	 */
	boolean deleteLocationData(String dataKey);

	/**
	 * 从所有集群redis中查询key为dataKey的位置数据
	 *
	 * @param dataKey
	 * @return
	 */
	List<LocationData> findAllLocationDatas(String dataKey);

	List<CANBUSDataReport> findCANDatas(String dataKey);

	/*
	 * added by zhangyj
	 *
	 * 分段删除数据，供dsa定时发起
	 */
	abstract void delGpsDataSegementThisDay(String mapName);

	/**
	 * 根据终端ID，分段集合，删除过期分段数据
	 * @param keyArr
	 */
	void delGpsDataSegementByKeyArr(String[] keyArr);
}
