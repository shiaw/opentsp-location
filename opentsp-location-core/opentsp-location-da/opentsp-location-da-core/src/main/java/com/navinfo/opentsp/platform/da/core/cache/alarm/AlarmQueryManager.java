package com.navinfo.opentsp.platform.da.core.cache.alarm;

import java.lang.Thread.State;
import java.util.concurrent.ConcurrentHashMap;

import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.DASQueryKeyServiceImp;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.QueryKeyEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**************************************************************
 * 管理当前业务系统涉及的分页缓存对象，包括与业务系统的唯一映射，缓存的动态创建与释放。
 *
 * @author claus
 *
 */
public class AlarmQueryManager {

	private static Logger logger = LoggerFactory.getLogger(AlarmQueryManager.class);
	// 保存所有业务请求与对于分页缓存关系
	private static ConcurrentHashMap<String, AlarmSummaryCacheEntity> alarmsCache = new ConcurrentHashMap<String, AlarmSummaryCacheEntity>();
	// 保存所有业务请求与对于分页缓存关系
	private static ConcurrentHashMap<String, AllAlarmSummaryCache> allAlarmsCache = new ConcurrentHashMap<String, AllAlarmSummaryCache>();

	// 保存所有业务请求与对于分页缓存关系
	private static ConcurrentHashMap<String, MilagesSummaryCache> milagesCache = new ConcurrentHashMap<String, MilagesSummaryCache>();
	// 保存所有业务请求与对于分页缓存关系
	private static ConcurrentHashMap<String, WFSummaryCache> wFlowCache = new ConcurrentHashMap<String, WFSummaryCache>();
	// 保存所有业务请求与对于分页缓存关系
	private static ConcurrentHashMap<String, GpsDataCache> gpsDataCache = new ConcurrentHashMap<String, GpsDataCache>();
	// 保存所有业务请求与对于分页缓存关系
	private static ConcurrentHashMap<String, TerminalOnOffDataCache> terminalOnlineDataCache = new ConcurrentHashMap<String, TerminalOnOffDataCache>();
	// Instance
	private static AlarmQueryManager _instance = new AlarmQueryManager();

	// 清理分页动态缓存线程
	private Thread cleanerThread;
	private static DASQueryKeyServiceImp dsaQuerykeyServiceImp = new DASQueryKeyServiceImp();
	/**
	 * 把da查询缓存的md5 key 放到redis缓存中，统一做超时处理 ---hk
	 * @param queryKey
	 * @param nodeCode
	 */
	private static void saveQueryKeyData(String queryKey, int nodeCode) {
		QueryKeyEntity entity=new QueryKeyEntity();
		entity.setQueryKey(queryKey);
		entity.setNodeCode(nodeCode);
		entity.setLastUpdateTime(System.currentTimeMillis()/1000);
		dsaQuerykeyServiceImp.putData(queryKey, entity);
	}

	public static void put(String queryKey, AlarmSummaryCacheEntity instance) {
		alarmsCache.put(queryKey, instance);
		saveQueryKeyData(queryKey, NodeHelper.getNodeCode());
	}

	//
	public static AlarmQueryManager getInstance() {
		if (_instance == null) {
			_instance = new AlarmQueryManager();

		}
		return _instance;
	}

	/**
	 * 清除所有与之相关的缓存数据.
	 *
	 * @param
	 */
	public void cleanCache(String query) {
		alarmsCache.remove(query);
	}

	/***********************************
	 * 当用户退出时，清除所有与之相关的缓存数据.
	 *
	 * @param uuid
	 */
	public void cleanCache() {
		alarmsCache.clear();
	}

	public AlarmSummaryCacheEntity getAlarmCacheByKey(String queryKey) {
		return alarmsCache.get(queryKey);
	}

	/************
	 * 获取当前缓存大小
	 *
	 * @return
	 */
	public int getCacheSize() {
		return alarmsCache.size();
	}

	/*************
	 * 获取当前缓存实例
	 *
	 * @return
	 */
	public ConcurrentHashMap<String, AlarmSummaryCacheEntity> getAlarmCache() {
		return alarmsCache;
	}

	/***********
	 * 获取当前清理线程的状态
	 * 
	 * @return
	 */
	public State getThreadStatus() {
		return cleanerThread.getState();
	}

	public AllAlarmSummaryCache getAllAlarmCacheByKey(String queryKey) {
		return allAlarmsCache.get(queryKey);
	}

	public void putAllAlarm(String queryKey, AllAlarmSummaryCache instance) {
		allAlarmsCache.put(queryKey, instance);
		saveQueryKeyData(queryKey, NodeHelper.getNodeCode());
	}

	public MilagesSummaryCache getMilagesCacheByKey(String queryKey) {
		return milagesCache.get(queryKey);
	}

	public void put(String queryKey, MilagesSummaryCache instance) {
		milagesCache.put(queryKey, instance);
		saveQueryKeyData(queryKey, NodeHelper.getNodeCode());
	}

	public WFSummaryCache getWFCacheByKey(String queryKey) {
		return wFlowCache.get(queryKey);
	}

	public void putWFCache(String queryKey, WFSummaryCache instance) {
		wFlowCache.put(queryKey, instance);
		saveQueryKeyData(queryKey, NodeHelper.getNodeCode());
	}

	public void remove(String key) {
		if (alarmsCache.containsKey(key)) {
			alarmsCache.remove(key);
		} else if (allAlarmsCache.containsKey(key)) {
			allAlarmsCache.remove(key);
		} else if (milagesCache.containsKey(key)) {
			milagesCache.remove(key);
		} else if (wFlowCache.containsKey(key)) {
			wFlowCache.remove(key);
		} else if (gpsDataCache.containsKey(key)) {
			gpsDataCache.remove(key);
		} else if (terminalOnlineDataCache.containsKey(key)) {
			terminalOnlineDataCache.remove(key);
		} else {

		}
	}

	public static GpsDataCache getGpsDataCacheByKey(String queryKey) {
		return gpsDataCache.get(queryKey);
	}

	public static void putGpsData(String queryKey, GpsDataCache instance) {
		gpsDataCache.put(queryKey, instance);
		saveQueryKeyData(queryKey, NodeHelper.getNodeCode());
	}

	public static TerminalOnOffDataCache getTerminalOnOffDataCacheByKey(
			String queryKey) {
		// TODO Auto-generated method stub
		return terminalOnlineDataCache.get(queryKey);
	}

	public static void putTerminalOnlineData(String queryKey,
			TerminalOnOffDataCache instance) {
		terminalOnlineDataCache.put(queryKey, instance);
		saveQueryKeyData(queryKey, NodeHelper.getNodeCode());
	}
}
