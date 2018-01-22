package com.navinfo.opentsp.platform.da.core.webService.manage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WSConfigCacheManage {

	/**
	 *
	 */
	private static Map<String, List<WSConfigCache>> cache = new ConcurrentHashMap<String, List<WSConfigCache>>();


	// 添加
	public static  void addWSConfigCatch(String distric, WSConfigCache object) {
		List<WSConfigCache> list = cache.get(distric);
		if( list==null){
			list=new ArrayList<WSConfigCache>();
		}
		list.add(object);
		cache.put(distric, list);
	}

	// 查询
	public static WSConfigCache getWSConfigCatch(String distric) {
		List<WSConfigCache> list = cache.get(distric);
		if(list!=null){
			for(WSConfigCache config:list){
				if(config.isAvailable){
					return config;
				}
			}
		}
		return null;
	}
	//
	public static void updateWSConfigCatch(WSConfigCache wSConfigCache ) {
		List<WSConfigCache> list = cache.get(wSConfigCache.getDistrict());
		if(list!=null){
			for(WSConfigCache config:list){
				if(config.getNodeCode()==wSConfigCache.getNodeCode());
				config.setAvailable(false);
			}
		}
	}
	// 删除
	public static void removeWSConfigCatch(String distric) {
		cache.remove(distric);
	}

	// 缓存size
	public static int size() {
		return cache.size();
	}


}
