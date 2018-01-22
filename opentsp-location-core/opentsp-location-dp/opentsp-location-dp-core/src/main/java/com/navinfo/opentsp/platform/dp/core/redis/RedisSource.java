package com.navinfo.opentsp.platform.dp.core.redis;

import java.util.HashMap;
import java.util.Map;

/**
 * 抽象通用方法
 * @author zhanhk
 * redis的常量配置
 */
public abstract class RedisSource implements RedisDao{

	//redis的服务器地址，满足分片
	protected static final String redis_address= Configuration.getString("redis.host");
	//设置最大的连接数
	protected static final String max_active = Configuration.getString("redis.maxActive");
	//设置池中最大的空闲数量
	protected static final String max_idle = Configuration.getString("redis.maxIdle");
	//连接等待的最大时间
	protected static final String max_wait = Configuration.getString("redis.maxWait");
	//连接超时时间
	protected static final String timeout = Configuration.getString("redis.timeout");
	//是否进行测试连接
	protected static final String isTest = Configuration.getString("redis.isTest");

	public void setHashValue(String mapName, long key, String value) {
		this.setHashValue(mapName , key + "" ,value);
	}

	public String getHashValue(String mapName, long key) {
		return this.getHashValue(mapName , key +"");
	}

	public Long delHashValue(String mapName, long key) {
		return this.delHashValue(mapName , key + "");
	}

	/**
	 * 根据hash key,Class 对象,获取全部redis hash Map
	 * @param mapName
	 * @param valueType
	 * @param <T>
     * @return
     */
	public <T> Map<String, T> getAllKeys(String mapName , Class<T> valueType){
		Map<String, T> map = new HashMap<>();
		Map<String, String> result = this.getAllKeys(mapName);
		for(Map.Entry<String,String> temp : result.entrySet()) {
			map.put(temp.getKey(),JacksonUtil.readValue(temp.getValue(),valueType));
		}
		return map;
	}

	/**
	 * 根据hash key,Class 对象,获取指定key 对象
	 * @param mapName
	 * @param key
	 * @param valueType
	 * @param <T>
     * @return
     */
	public <T> T getHashValue(String mapName, long key, Class<T> valueType) {
		String result = this.getHashValue(mapName,key);
		if(result != null) {
			return JacksonUtil.readValue(result,valueType);
		}
		return null;
	}

	public <T> T getHashValue(String mapName, String key, Class<T> valueType) {
		String result = this.getHashValue(mapName,key);
		if(result != null) {
			return JacksonUtil.readValue(result,valueType);
		}
		return null;
	}

	public void setHashValue(String mapName, String key, long value) {
		this.setHashValue(mapName,key,value+"");
	}

	/**
	 * 存hash 对象
	 * @param mapName
	 * @param key
	 * @param obj
     */
	public void setHashValue(String mapName, String key, Object obj) {
		this.setHashValue(mapName,key,JacksonUtil.toJSon(obj));
	}
}
