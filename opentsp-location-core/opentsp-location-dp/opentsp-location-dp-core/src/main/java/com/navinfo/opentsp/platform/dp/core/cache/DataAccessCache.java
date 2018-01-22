package com.navinfo.opentsp.platform.dp.core.cache;

import com.navinfo.opentsp.platform.dp.core.cache.entity.DataAccessCacheEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据访问层交互缓存<br>
 * 缓存所有发送给数据访问层存储的数据,仅当存储成功后,删除缓存数据
 * key=指令号_流水号,value=DataAccessCacheEntry
 * 
 * @author lgw
 * 
 */
public class DataAccessCache {
	private static Map<String, DataAccessCacheEntry> cache = new ConcurrentHashMap<String, DataAccessCacheEntry>();
	private final static DataAccessCache instance = new DataAccessCache();

	private DataAccessCache() {
	}

	public final static DataAccessCache getInstance() {
		return instance;
	}

	public DataAccessCacheEntry getDataAccessCacheEntry(String uniqueMark, int commandId ,
														int serialNumber) {
		return cache.get(commandId + "_" + serialNumber);
	}

	public void addDataAccessCacheEntry(String uniqueMark, int commandId, int serialNumber,
										DataAccessCacheEntry cacheEntry) {
		cache.put(commandId + "_" + serialNumber, cacheEntry);
	}

	public boolean removeDataAccessCacheEntry(String uniqueMark, int commandId ,
											  int serialNumber) {
		return cache.remove(commandId + "_" + serialNumber) != null;
	}

	/**
	 * 获取缓存超时对象 <br>
	 * 缓存自动删除超时数据
	 * 
	 * @return
	 */
	public List<DataAccessCacheEntry> getExpiredObject() {
		Iterator<Entry<String, DataAccessCacheEntry>> iterable = cache.entrySet().iterator();
		List<DataAccessCacheEntry> result = new ArrayList<DataAccessCacheEntry>();
		long time = System.currentTimeMillis() / 1000;
		while (iterable.hasNext()) {
			Entry<String, DataAccessCacheEntry> entry = iterable.next();
			if (entry != null) {
				if (time - entry.getValue().getLastUpdateTime() > entry.getValue().getTimeout()) {
					result.add(entry.getValue());
					iterable.remove();
				}
			}
		}
		return result;
	}
	public int size(){
		return cache.size();
	}
}
