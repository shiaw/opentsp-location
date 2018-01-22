package com.navinfo.opentsp.platform.da.core.persistence.common;

public class ServiceMarkFactory {
	/* 计数器，从1开始，当达到最大值后，重新置1 */
	public static int counter = 1;

	/**
	 * 获取服务标识(当前时间毫秒数低位截取12位与计数器求和)
	 * 
	 * @return String
	 */
	public static long getServiceMark() {
		long id = System.currentTimeMillis() / 10 + counter;

		if (counter == Integer.MAX_VALUE) {
			counter = 1;
		} else {
			counter++;
		}
		return id;
	}
}
