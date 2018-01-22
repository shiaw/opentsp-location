package com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于异步消息，存放808协议包中的必要信息
 */
public class PacketCache {

	//终端的链路资源回收,采用被动方式
	//在获取终端链路,当链路不存在,重置数据

	//<"0"+terminalId,TerminalInfo>
	private static Map<String, PacketArguments> cache = new ConcurrentHashMap<String, PacketArguments>();

	private static PacketCache instance = new PacketCache();
	private PacketCache(){}
	public static PacketCache getInstance (){
		return instance;
	}


	public Map<String, PacketArguments> get(){
		Map<String, PacketArguments> temp = cache;
		return temp;
	}

	public void add(String key , PacketArguments value){
		cache.put(key, value);
	}

	public void remove(String key){
		cache.remove(key);
	}


	public PacketArguments get(String key){

		return cache.get(key);
	}

	

}
