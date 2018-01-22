package com.navinfo.opentsp.platform.location.kit.entity;

import java.io.Serializable;

public class LCServerNode implements Serializable {
	private static final long serialVersionUID = 1L;
	private int nodeCode;
	private String nodeIp;
	private int nodeTcpPort;

	public LCServerNode() {
		super();
	}

	public int getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(int nodeCode) {
		this.nodeCode = nodeCode;
	}

	public String getNodeIp() {
		return nodeIp;
	}

	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}

	public int getNodeTcpPort() {
		return nodeTcpPort;
	}

	public void setNodeTcpPort(int nodeTcpPort) {
		this.nodeTcpPort = nodeTcpPort;
	}

}
