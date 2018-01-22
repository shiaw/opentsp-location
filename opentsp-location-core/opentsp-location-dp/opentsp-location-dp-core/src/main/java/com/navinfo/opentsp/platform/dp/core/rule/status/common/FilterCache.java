package com.navinfo.opentsp.platform.dp.core.rule.status.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 过滤缓存
 * Map<终端ID，Map<区域ID,屏蔽时间戳>>	屏蔽时间戳为当前时间+屏蔽时间段
 * @author Administrator
 *
 */
public class FilterCache {
	public static Logger log = LoggerFactory.getLogger(FilterCache.class);
	static Map<Long, Map<Long, Long>> cache = new ConcurrentHashMap<>(); //青汽平台不用此缓存
	static Map<Long,Long> mapCache = new ConcurrentHashMap<>();
	static FilterCache instance = new FilterCache();

	private FilterCache(){}

	public static  FilterCache getIntence(){
		return instance;
	}

	public void addFilterCache(Long terminalId , Long areaId, Long filterTime){
		Map<Long, Long> areaFilters = cache.get(terminalId);
		if(areaFilters == null){
			areaFilters = new ConcurrentHashMap<>();
			cache.put(terminalId, areaFilters);
		}
		areaFilters.put(areaId, filterTime);
	}

	public void delFilterCache(Long terminalId,long areaId){
		Map<Long, Long> areaFilters = cache.get(terminalId);
		if(areaFilters != null){
			areaFilters.remove(areaId);
		}
	}

	public Long getFilterTime(Long terminalId,long areaId){
		Long filterTime = 0l;
		Map<Long, Long> areaFilters = cache.get(terminalId);
		if(areaFilters != null){
			filterTime = areaFilters.get(areaId);
		}
		return filterTime;
	}

	public void addFilterCache(Long terminalId,Long filterTime){
		mapCache.put(terminalId,filterTime);
	}
	public Long getFilterTime(Long terminalId){
		return mapCache.get(terminalId);
	}

}
