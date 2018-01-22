package com.navinfo.opentsp.platform.da.core.persistence.redis.dao;

import com.navinfo.opentsp.platform.da.core.persistence.redis.IRedis;

import java.util.List;


public interface IRedisListDao extends IRedis {
	/**
	 * 将所有指定的值插入到存于 key 的列表的头部。如果 key 不存在，那么在进行 push 操作前会创建一个空列表。 如果 key
	 * 对应的值不是一个 list 的话，那么会返回一个错误。可以使用一个命令把多个元素 push
	 * 进入列表，只需在命令末尾加上多个指定的参数。元素是从最左端的到最右端的、一个接一个被插入到 list 的头部。 所以对于这个命令例子 LPUSH
	 * mylist a b c，返回的列表是 c 为第一个元素， b 为第二个元素， a 为第三个元素。
	 *
	 * @param key
	 * @param value
	 * @return 当前集合数据总条数
	 */
	abstract long push(String key, Object... values);
	/**
	 * 检查库中是否存在key
	 * @param key
	 * @return
	 */
	abstract public boolean existKey(String key);
	abstract long pushForLimit(String key, Object... values);
	/**
	 * 将指定的值插入到存于 key 的列表的头部。如果 key 不存在，那么在进行 push 操作前会创建一个空列表。 如果 key 对应的值不是一个
	 * list 的话，那么会返回一个错误。元素是从最左端的到最右端的、一个接一个被插入到 list 的头部。
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	abstract long push(String key, Object value);
	abstract long pushForLimit(String key, Object value);

	abstract long push(byte[] key , byte[] value);
	abstract long pushForLimit(byte[] key , byte[] value);

	/**
	 * 返回存储在 key 的列表里指定范围内的元素
	 *
	 * @param key
	 * @return
	 */
	abstract List get(String key);

	/**
	 * 返回存储在 key 的列表里指定范围内的元素 <br>
	 * protobuf对象需要使用此接口进行查询
	 *
	 * @param key
	 * @return
	 */
	abstract List<byte[]> getForBytes(String key);

	/**
	 * 移除指定key
	 *
	 * @param key
	 * @return 被删除的keys的数量
	 */
	abstract long del(String key);
}
