package com.navinfo.opentsp.platform.da.core.rmi.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.StringTokenizer;

import com.navinfo.opentsp.platform.da.core.common.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * DA模块根据配置信息发布服务
 *
 * RMI的绑定对象用静态的
 *
 */
public class DARmiServer {

	private static Logger logger = LoggerFactory.getLogger(DARmiServer.class);

	private static String ip = Configuration.getString("Rmi.Service.Ip");

	private static int port = Integer.parseInt(Configuration.getString("Rmi.Service.Port"));

	private static String services = Configuration.getString("Rmi.Service.services");

	private static boolean center = Boolean.valueOf(Configuration.getString("IS.CENTER"));

	private static String centerServices = Configuration.getString("Rmi.Center.Service.services");


	public static void start() throws Exception {
		LocateRegistry.createRegistry(port);
		StringTokenizer st = null;
		if (center) {
			if (null == centerServices || centerServices.length() == 0) {
				logger.info("没有需要注册的主RMI服务");
				return;
			}
			st = new StringTokenizer(centerServices, ",", false);
		} else {
			if (null == services || services.length() == 0) {
				logger.info("没有需要注册的分RMI服务");
				return;
			}
			st = new StringTokenizer(services, ",", false);
		}
		while (st.hasMoreElements()) {
			String service = st.nextToken();
			Class<? extends Remote> clazz = getRmiServiceClass(service);
			if (clazz == null) {
				throw new Exception("No RmiServiceClass for service: " + service);
			}
			try {
				logger.info("[RMI ] rmi://" + ip + ":" + port + "/" + service + ", ready. ");
				Naming.bind(String.format("rmi://%s:%d/%s", ip, port, service), clazz.newInstance());

			} catch (RemoteException e) {
				logger.error("创建远程对象发生异常！", e);
				e.printStackTrace();
			} catch (MalformedURLException e) {
				logger.error("发生URL异常！", e);
				e.printStackTrace();
			}
		}

	}

	@SuppressWarnings("unchecked")
	private static Class<? extends Remote> getRmiServiceClass(String service) {
		try {
			return (Class<? extends Remote>) Class.forName(Configuration.getString(service));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {

		DARmiServer.start();
	}
}