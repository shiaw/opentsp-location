package com.navinfo.opentsp.platform.dp.core.cache.entity;

import com.navinfo.opentsp.platform.location.protocol.nodecluster.common.LCNodeInfo.NodeInfo;

/**
 * NodeInfo节点信息包装类，保存ConnectStatus，主要用来保存连接DP节点的RP节点状态信息
 * @author JBP
 */
public class RPNodeInfo {
	private NodeInfo nodeInfo;
	private ConnectStatus connectedStatus;
	
	public ConnectStatus getConnectedStatus() {
		return connectedStatus;
	}

	public void setConnectedStatus(ConnectStatus connectedStatus) {
		this.connectedStatus = connectedStatus;
	}

	public NodeInfo getNodeInfo() {
		return nodeInfo;
	}

	public void setNodeInfo(NodeInfo nodeInfo) {
		this.nodeInfo = nodeInfo;
	}

	
	public RPNodeInfo(NodeInfo nodeInfo, ConnectStatus connectedStatus) {
		super();
		this.nodeInfo = nodeInfo;
		this.connectedStatus = connectedStatus;
	}

}
