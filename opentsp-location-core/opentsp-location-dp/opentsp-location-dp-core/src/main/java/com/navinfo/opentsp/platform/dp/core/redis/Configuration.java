package com.navinfo.opentsp.platform.dp.core.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class Configuration {
	private static final Logger log = LoggerFactory
			.getLogger(Configuration.class);
	private static Map<String, Object> config = new ConcurrentHashMap<String, Object>();
	static {
		Properties serverConfig = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(System.getProperty("user.dir")
					+ "//bin//ServerConfig.properties");
			serverConfig.load(input);
			for (Entry<Object, Object> e : serverConfig.entrySet()) {
				Configuration.addConfig(String.valueOf(e.getKey()),
						String.valueOf(e.getValue()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		log.info("Load configuration .");

		Properties redisConfig = new Properties();
		try {
			redisConfig.load(new FileInputStream(System.getProperty("user.dir")
					+"//bin//RedisConfig.properties"));
			for (Entry<Object, Object> e : redisConfig.entrySet()) {
				Configuration.addConfig(String.valueOf(e.getKey()), String.valueOf(e.getValue()));
			}
		} catch (IOException e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
		}
		log.info("Load RedisConfig Finished.");
	}

	public static void load(String configFile) {

	}

	public static void addConfig(String key, String value) {
		config.put(key, value);
	}

	public static void addConfig(String key, Long value) {
		config.put(key, value);
	}

	public static void addConfig(String key, Integer value) {
		config.put(key, value);
	}

	public static Object getConfig(String key) {
		return config.get(key);
	}

	public static String getString(String key) {
		Object value = getConfig(key);

		if (value != null && !"".equals(value.toString())) {
			return value.toString();
		} else {
			throw new RuntimeException("config key [" + key + "] is not find.");
		}
	}

	public static int getInt(String key) {
		Object value = config.get(key);
		if (value != null && !"".equals(value.toString())) {
			return Integer.parseInt(value.toString());
		} else {
			throw new RuntimeException("config key [" + key + "] is not find.");
		}
	}

	public static boolean getBoolean(String key){
		Object value = config.get(key);
		if (value != null && !"".equals(value.toString())) {
			return Boolean.valueOf(value.toString());
		} else {
			log.error("config key :["+key+"] is not find.");
			throw new RuntimeException("config key is not find.");
		}
	}
}
