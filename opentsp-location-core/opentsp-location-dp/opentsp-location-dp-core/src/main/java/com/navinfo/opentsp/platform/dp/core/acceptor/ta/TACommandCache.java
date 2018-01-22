//package com.navinfo.opentsp.platform.dp.core.acceptor;
//
//import com.navinfo.opentsp.gateway.tcp.proto.location.util.Command;
//
//import java.io.InputStream;
//import java.util.Enumeration;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class TACommandCache {
//	private static Map<String, Command> commands = new ConcurrentHashMap<String, Command>();
//
//	private TACommandCache() {
//	}
//
//	static {
//		InputStream inputStream = null;
//		try {
////			inputStream = ResourceLoader.class.getResourceAsStream("/com/lc/dp/acceptor/tal/protocol/protocol.properties");
//
//			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//			inputStream = classLoader.getResourceAsStream("com/lc/dp/acceptor/tal/protocol/protocol.properties");
//
//			Properties properties = new Properties();
//			properties.load(inputStream);
//			Enumeration<Object> keys = properties.keys();
//			while (keys.hasMoreElements()) {
//				String key = keys.nextElement().toString();
//				try {
//					Command command = (Command) Class.forName(
//							properties.getProperty(key)).newInstance();
//					commands.put(key, command);
//				} catch (InstantiationException e) {
//					e.printStackTrace();
//				} catch (IllegalAccessException e) {
//					e.printStackTrace();
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				}
//
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//	private static final TACommandCache instance = new TACommandCache();
//
//	public static final TACommandCache getInstance() {
//		return instance;
//	}
//
//	public void addCommand(String commandId, Command claxx) {
//		commands.put(commandId, claxx);
//	}
//
//	public Command getCommand(String commandId) {
//		return commands.get(commandId);
//	}
//
//	public static void main(String[] args) {
//		getInstance();
//	}
//}
