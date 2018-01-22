package com.navinfo.opentsp.gateway.tcp.proto.location.cache;


import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 指令缓存<br>
 * <li>指令超时</li> <li>通道寻找</li> <br>
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
public class AnswerCommandCache {
	private static Map<String, AnswerEntry> cache = new ConcurrentHashMap<String, AnswerEntry>();
	static ReadWriteLock lock = new ReentrantReadWriteLock();
	private static AnswerCommandCache instance = new AnswerCommandCache();
	private AnswerCommandCache(){}
	public static final AnswerCommandCache getInstance(){
		return instance;
	}
	/**
	 * 添加一个指令信息到缓存<br>
	 * 
	 * @param key
	 * @param answerEntry
	 */
	public void addEntry(String uniqueMark, int serialNumber,
			AnswerEntry answerEntry) {
		answerEntry.setUniqueMark(uniqueMark);
		cache.put(uniqueMark + "-" + serialNumber, answerEntry);
		cache.size();
	}

	/**
	 * 获取一个指令信息
	 * 
	 * @param uniqueMark
	 *            {@link String} 数据包唯一标识
	 * @param serialNumber
	 *            {@link Integer} 流水号
	 * @param isDelete
	 *            {@link Boolean}是否删除缓存中消息
	 * @return
	 */
	public AnswerEntry getAnswerEntry(String uniqueMark, int serialNumber,boolean isRemove) {
		if (isRemove) {
			return this.remove(uniqueMark, serialNumber);
		} else {
			return cache.get(uniqueMark + "_" + serialNumber);
		}
	}

	/**
	 * 移除缓存中一个指令消息
	 * 
	 * @param uniqueMark
	 * @param serialNumber
	 * @return
	 */
	public AnswerEntry remove(String uniqueMark, int serialNumber) {
		return cache.remove(uniqueMark + "-" + serialNumber);
	}

	public int clear() {
		int size = cache.size();
		cache.clear();
		return size;
	}

	/**
	 * 获取超时消息
	 * 
	 * @return
	 */
	public List<AnswerEntry> getTimeoutAnswerEntry() {
		lock.writeLock().lock();
		long currentTime = System.currentTimeMillis() / 1000;
		List<AnswerEntry> result = new ArrayList<AnswerEntry>();
		Iterator<Entry<String, AnswerEntry>> iterator = cache.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, AnswerEntry> entry = iterator.next();
			AnswerEntry answerEntry = entry.getValue();

			if (currentTime - answerEntry.getCreateTime() > answerEntry
					.getTimeout()) {
				result.add(answerEntry);
				iterator.remove();
			}			
		}
		lock.writeLock().unlock();
		return result;
	}
	public int size(){
		return cache.size();
	}
}
