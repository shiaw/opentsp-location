package com.navinfo.opentsp.platform.da.core.persistence.redis.dao;

import java.util.List;
import java.util.Map;

public interface IRedisMapDao {
	/**
	 * 设置hash里面一个字段的值
	 *
	 * @param mapName
	 *            {@link String} hash名称
	 * @param field
	 *            {@link String} hash中的字段
	 * @param value
	 *            {@link String} hash字段值
	 * @return {@link Boolean}
	 */
	abstract boolean put(String mapName, String field, Object value);

	/**
	 * 批量设置hash里面字段的值<br>
	 * 字段与值所在数组位置需要对应
	 *
	 * @param mapName
	 * @param fields
	 * @param values
	 * @return
	 */
	abstract boolean put(String mapName, String[] fields, Object[] values);

	/**
	 * 读取一个哈希域值
	 *
	 * @param mapName
	 *            {@link String} hash名称
	 * @param field
	 *            {@link String} 字段名称
	 * @return {@link Object}
	 */
	abstract Object get(String mapName, String field);

	abstract byte[] hget(String mapName, String field);

	/**
	 * 读取多个哈希域值
	 *
	 * @param mapName
	 *            {@link String} hash名称
	 * @param fields
	 *            {@link String}[] 字段名称
	 * @return {@link List} {@link Object}
	 */
	abstract List get(String mapName, String... fields);

	/**
	 * 读取一个哈希集中读取全部的域和值
	 *
	 * @param mapName
	 *            {@link String} hash名称
	 * @return {@link Map}
	 */
	abstract Map<String, Object> get(String mapName);

	/**
	 * 获得hash的所有值
	 *
	 * @param mapName
	 *            {@link String} hash名称
	 * @return {@link List}
	 */
	abstract List allValues(String mapName);

	/**
	 * 获取hash的所有key
	 *
	 * @param mapName
	 *            {@link String} hash名称
	 * @return {@link List}
	 */
	abstract List<String> allKeys(String mapName);

	/**
	 * 删除指定hash的一个或者多个字段
	 *
	 * @param mapName
	 *            {@link String} hash名称
	 * @param fields
	 *            {@link String} 字段名称
	 * @return
	 */
	abstract long del(String mapName, String... fields);

}
