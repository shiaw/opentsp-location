package com.navinfo.opentsp.platform.location.kit;

import java.io.InputStream;
import java.util.Properties;

public class ResourceLoader {
	public static Properties loadproperties(String file) {
		InputStream inputStream = null;
		try {
			inputStream = ResourceLoader.class.getResourceAsStream("/" + file);
			Properties properties = new Properties();
			properties.load(inputStream);
			System.err.println(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
