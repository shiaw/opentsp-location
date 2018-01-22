package com.navinfo.opentsp.platform.da.core.persistence.redis.service;


public interface IDASQueryKeyService {
	void test(String key, Object value);
	/**
	 * 添加查询key对应的dsa节点编号
	 * @param key
	 * @param value
	 */
	public void putData(String key, Object value) ;



	/**
	 * 获取查询key对应的节点编号
	 * @param queryKey
	 * @return
	 */
	public Object findQueryKey(String  queryKey) ;
}
