package com.navinfo.opentsp.platform.da.core.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import  com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCExtendConfig.NodePort;
import  com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCExtendConfig.NodePort.PortType;
import  com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeInfo.NodeInfo;

public class NodeList {
	private static Map<Long, NodeInfo> cache = new ConcurrentHashMap<Long, NodeInfo>();

	public static void addNode(NodeInfo node) {
		cache.put(node.getNodeCode(), node);
	}

	public static NodeInfo get(long nodeCode) {
		if (!check(nodeCode)) {
			throw new RuntimeException("对不起,未找到节点[" + nodeCode + "]的信息.");
		}
		return cache.get(nodeCode);
	}

	/**
	 * 根据节点编号获取IP
	 *
	 * @param nodeCode
	 * @return
	 */
	public static String getLocalIpAddress(long nodeCode) {
		if (!check(nodeCode)) {
			throw new RuntimeException("对不起,未找到节点[" + nodeCode + "]的信息.");
		}
		return cache.get(nodeCode).getAddressLocalIP();
	}

	public static String getInternetIpAddress(long nodeCode) {
		if (!check(nodeCode)) {
			throw new RuntimeException("对不起,未找到节点[" + nodeCode + "]的信息.");
		}
		return cache.get(nodeCode).getAddressInternetIP();
	}

	/**
	 * 根据节点编号以及端口类型<br>
	 * 获取端口号
	 *
	 * @param nodeCode
	 * @param portType
	 * @return
	 */
	public static int getPort(long nodeCode, PortType portType) {
		if (!check(nodeCode)) {
			throw new RuntimeException("对不起,未找到节点[" + nodeCode + "]的信息.");
		}
		NodeInfo nodeInfo = cache.get(nodeCode);
		List<NodePort> ports = nodeInfo.getExtendConfigs().getPortsList();
		for (NodePort nodePort : ports) {
			if (portType.getNumber() == nodePort.getTypes().getNumber()) {
				return nodePort.getPorts();
			}
		}
		return -1;
	}

	public static boolean remove(long nodeCode) {
		return cache.remove(nodeCode) != null;
	}

	private static boolean check(long nodeCode) {
		return cache.containsKey(nodeCode);
	}

	public static int size() {
		return cache.size();
	}
}
