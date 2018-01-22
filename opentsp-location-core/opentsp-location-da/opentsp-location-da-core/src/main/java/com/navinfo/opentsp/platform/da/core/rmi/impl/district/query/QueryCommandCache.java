package com.navinfo.opentsp.platform.da.core.rmi.impl.district.query;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class QueryCommandCache {
	private static Map<String, AlarmQueryService> commands = new ConcurrentHashMap<String, AlarmQueryService>();

	private QueryCommandCache() {
	}

	static {
		

	}
	private static final QueryCommandCache instance = new QueryCommandCache();

	public static final QueryCommandCache getInstance() {
		return instance;
	}

//	public void addCommand(String commandId, Command claxx) {
//		commands.put(commandId, claxx);
//	}

//	public Command getCommand(String commandId) {
//		return commands.get(commandId);
//	}

	public static void main(String[] args) {
		getInstance();
	}}
