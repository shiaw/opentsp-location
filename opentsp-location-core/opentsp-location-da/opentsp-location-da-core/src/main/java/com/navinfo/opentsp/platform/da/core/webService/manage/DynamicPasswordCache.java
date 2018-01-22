package com.navinfo.opentsp.platform.da.core.webService.manage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DynamicPasswordCache  {


	private static Map<String,DynamicPassword> cache=new ConcurrentHashMap<String,DynamicPassword>();

	//获取动态口令cache
	public static Map<String,DynamicPassword> getDynamicPasswordCache(){
		return cache;
	}
	//添加
	public static void addInstance(String sessionId, DynamicPassword password) {
		cache.put(sessionId, password);
	}
	//查询
	public static DynamicPassword getInstance(String sessionId) {
		return cache.get(sessionId);
	}
	//删除
	public static void removeInstance(String sessionId){
		cache.remove(sessionId);
	}
	//缓存size
	public static int size() {
		return cache.size();
	}
}