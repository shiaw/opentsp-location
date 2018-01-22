package com.navinfo.opentsp.gateway.tcp.proto.location.cache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <br>
 * <li>终端最后上报时间</li> <br>
 * <h2>约定：</h2>
 * <p>
 * 缓存key=数据包唯一标识_流水号<br>
 * 
 * <pre>
 * packet.getUniqueMark() + &quot;_&quot; + packet.getSerialNumber()
 * </pre>
 * 
 * </p>
 * 
 * @author lgw
 * 
 */
public class TerminalLastTimeCache {

	private static final Logger log = LoggerFactory.getLogger(TerminalLastTimeCache.class);
	private static Map<String, Long> cache = new ConcurrentHashMap<String, Long>();
	static ReadWriteLock lock = new ReentrantReadWriteLock();
	private static TerminalLastTimeCache instance = new TerminalLastTimeCache();
	private TerminalLastTimeCache(){}
	public static final TerminalLastTimeCache getInstance(){
		return instance;
	}
	/**
	 * 添加一个指令信息到缓存<br>
	 * 
	 * @param uniqueMark
	 * @param time
	 */
	public void addEntry(String uniqueMark, Long time) {
		cache.put(uniqueMark, time);
		cache.size();
	}

	/**
	 * 获取一个指令信息
	 * 
	 * @param uniqueMark
	 *            {@link String} 数据包唯一标识
	 *            {@link Boolean}是否删除缓存中消息
	 * @return
	 */
	public Long getEntry(String uniqueMark,boolean isRemove) {
		if (isRemove) {
			return this.remove(uniqueMark);
		} else {
			return cache.get(uniqueMark);
		}
	}

	/**
	 * 获取超时终端
	 *
	 * @return
	 */
	public List<String> getTimeoutTerminals() {
		lock.writeLock().lock();
		long currentTime = System.currentTimeMillis() ;//毫秒
		List<String> result = new ArrayList<String>();
		Iterator<Map.Entry<String, Long>> iterator = cache.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, Long> entry = iterator.next();
			Long lastTime = entry.getValue();
			log.info("currentTime:{},lastTime:{}，time:{}",currentTime,lastTime,currentTime - lastTime);
			if (currentTime - lastTime > 120000) {
				result.add(entry.getKey());
				iterator.remove();
			}
		}
		lock.writeLock().unlock();
		return result;
	}

	/**
	 * 移除缓存中一个指令消息
	 * 
	 * @param uniqueMark
	 *
	 * @return
	 */
	public Long remove(String uniqueMark) {
		return cache.remove(uniqueMark );
	}

	public int clear() {
		int size = cache.size();
		cache.clear();
		return size;
	}

	public int size(){
		return cache.size();
	}
}
