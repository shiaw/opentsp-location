package com.navinfo.opentsp.platform.da.core.persistence.redis.dao;

import java.util.Set;

public interface IRedisSetDao {
	/**
	 * 添加一个元素到集合的 key中.如果已经在集合key中存在则忽略.如果集合key 不存在，则新建集合key,并添加元素到集合key中.
	 *
	 * @param setName
	 * @param value
	 * @return {@link Long} 返回新成功添加到集合里元素的数量，不包括已经存在于集合中的元素.
	 */
	abstract long add(String setName, Object value);

	/**
	 * 添加多个指定元素到集合的 key中.如果已经在集合key中存在则忽略.如果集合key 不存在，则新建集合key,并添加元素到集合key中.
	 *
	 * @param setName
	 * @param values
	 * @return {@link Long} 返回新成功添加到集合里元素的数量，不包括已经存在于集合中的元素.
	 */
	abstract long add(String setName, Object... values);

	/**
	 * 返回key集合所有的元素.
	 *
	 * @param setName
	 * @return {@link Set}
	 */
	abstract Set get(String setName);
	/**
	 * 随机获取集合中的一个元素,并删除此元素
	 * @param setName {@link String} 集合名称
	 * @return
	 */
	abstract Object pop(String setName);
	/**
	 * 随机获取集合中的一个元素
	 * @param setName {@link String} 集合名称
	 * @return
	 */
	abstract Object randmember(String setName);

	/**
	 * 在集合中移除指定的元素. 如果指定的元素不是集合中的元素则忽略 如果集合不存在则被视为一个空的集合，该命令返回0.
	 *
	 * @param setName
	 * @param values
	 * @return {@link Long} 从集合中移除元素的个数，不包括不存在的成员.
	 */
	abstract long remove(String setName, Object... values);

	abstract void del(String setName);
}
