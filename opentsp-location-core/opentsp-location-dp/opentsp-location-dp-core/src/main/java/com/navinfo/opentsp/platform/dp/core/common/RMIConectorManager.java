package com.navinfo.opentsp.platform.dp.core.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Naming;


public class RMIConectorManager {


	public static String RMI_DA_IP ="lcoalhost";
	public static int RMI_DA_PORT;

	private Logger logger = LoggerFactory.getLogger(RMIConectorManager.class);

	private static RMIConectorManager _instance = null;

	public static RMIConectorManager getInstance() {
		if (_instance == null)
			_instance = new RMIConectorManager();
		return _instance;
	}

	/**
	 * 获取RMI接口类对象
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Object getRmiInstance(String name)
			throws Exception {
		Object reulst = null;
		logger.info("[DP-DA > DA]rmi://"
				+ RMI_DA_IP + ":" + RMI_DA_PORT
				+ "/" + name);
		reulst = Naming.lookup("rmi://"
				+ RMI_DA_IP + ":" + RMI_DA_PORT
				+ "/" + name);
		return reulst;
	}

}
