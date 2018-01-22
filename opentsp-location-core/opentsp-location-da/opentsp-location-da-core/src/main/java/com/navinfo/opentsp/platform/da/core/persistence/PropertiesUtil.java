package com.navinfo.opentsp.platform.da.core.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	private static PropertiesUtil instance;
	private static Properties properties;

	private PropertiesUtil() {
		init();
	}

	public static PropertiesUtil getInstance() {
		if (instance == null) {
			synchronized (PropertiesUtil.class) {
				if (instance == null) {
					instance = new PropertiesUtil();
				}
			}
		}
		return instance;
	}

	public Properties getProp() {
		return properties;
	}

	private static Properties init() {

		InputStream fs = null;

		try {
			properties = new Properties();
			fs = PropertiesUtil.class.getClassLoader().getResourceAsStream("./config.properties");
			properties.load(fs);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fs != null)
					fs.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
		return properties;
	}

}
