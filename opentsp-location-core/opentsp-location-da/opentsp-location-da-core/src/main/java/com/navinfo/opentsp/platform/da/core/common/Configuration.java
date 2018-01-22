package com.navinfo.opentsp.platform.da.core.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Configuration {
	private static final Logger log = LoggerFactory.getLogger(Configuration.class);
	private static Map<String, Object> config = new ConcurrentHashMap<String, Object>();

	public static void addConfig(String key, String value) {
		config.put(key, value);
	}

	public static void addConfig(String key, Integer value) {
		config.put(key, value);
	}

	public static Object getConfig(String key) {
		if (CollectionUtils.isEmpty(config) || config.get(key)==null){
			return SpringContextUtil.getApplicationContext().getEnvironment().getProperty(key);
		}
		return config.get(key);
	}

	public static String getString(String key) {
		Object value = getConfig(key);

		if (value != null && !"".equals(value.toString())) {
			return value.toString();
		} else {
			return "";
//			log.error("config key :["+key+"] is not find.");
//			throw new RuntimeException("config key :["+key+"] is not find.");
		}
	}

	public static int getInt(String key) {
		Object value = getConfig(key);
		if (value != null && !"".equals(value.toString())) {
			return Integer.parseInt(value.toString());
		} else {
			log.error("config key :["+key+"] is not find.");
			throw new RuntimeException("config key ["+key+"] is not find.");
		}
	}
	public static boolean getBoolean(String key){
		Object value = getConfig(key);
		if (value != null && !"".equals(value.toString())) {
			return Boolean.valueOf(value.toString());
		} else {
			log.error("config key :["+key+"] is not find.");
			throw new RuntimeException("config key is not find.");
		}
	}
}
