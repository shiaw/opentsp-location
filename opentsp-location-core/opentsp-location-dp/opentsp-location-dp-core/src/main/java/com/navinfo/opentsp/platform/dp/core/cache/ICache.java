package com.navinfo.opentsp.platform.dp.core.cache;

import java.util.List;

public interface ICache {
	/**
	 * key-value<br>
	 * 一对一模式<br>
	 * 
	 * @param key
	 * @param object
	 * @param timeout
	 *            {@link Integer} 缓存超时时间,0为不超时,单位为秒
	 */
	abstract void writeOne(String key, Object object, int timeout);

	/**
	 * key-value<br>
	 * 一对多模式<br>
	 * timeout=0为不超时
	 * 
	 * @param key
	 * @param value
	 * @param timeout
	 *            {@link Integer} 缓存超时时间,0为不超时,单位为秒
	 */
	abstract void writeArrays(String key, Object value, int timeout);

	/**
	 * 从缓存当中读取一个集合
	 * 
	 * @param key
	 * @param isDelete
	 *            {@link Boolean} 读取后是否删除缓存记录
	 * @return
	 */
	abstract <T> List<T> readArrays(String key, boolean isDelete);

	/**
	 * 从缓存当中读取一个对象
	 * 
	 * @param key
	 * @param isDelete
	 *            {@link Boolean} 读取后是否删除缓存记录
	 * @return
	 */
	abstract <T> T readOne(String key, boolean isDelete);

	/**
	 * 从缓存中移除数据
	 * 
	 * @param key
	 */
	abstract void remove(String key);
	
	abstract int size();
}
