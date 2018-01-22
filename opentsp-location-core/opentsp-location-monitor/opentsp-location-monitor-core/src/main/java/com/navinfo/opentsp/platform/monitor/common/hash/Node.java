package com.navinfo.opentsp.platform.monitor.common.hash;

import java.io.Serializable;

public class Node implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nodeCode;
	private String masterIp;
	private int masterPort;
	private String slaveIp;
	private int slavePort;
	
	private String _master_mark = "";
	private String _slave_mark = "";

	public Node() {
		super();
	}
	/**获取主节点标识*/
	public String getMaster(){
		return this._master_mark;
	}
	/**获取从节点标识*/
	public String getSlave(){
		return this._slave_mark;
	}
	
	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public int getMasterPort() {
		return masterPort;
	}

	public void setMasterPort(int masterPort) {
		if("".equals(this._master_mark)){
			this._master_mark = "_"+masterPort;
		}else{
			String[] _master_marks = this._master_mark.split("_");
			this._master_mark = _master_marks[0]+"_"+masterPort;
		}
		this.masterPort = masterPort;
	}


	public int getSlavePort() {
		return slavePort;
	}

	public void setSlavePort(int slavePort) {
		if("".equals(this._slave_mark)){
			this._slave_mark = "_"+slavePort;
		}else{
			String[] _slave_marks = this._slave_mark.split("_");
			this._slave_mark = _slave_marks[0]+"_"+slavePort;
		}
		this.slavePort = slavePort;
	}


	public String getMasterIp() {
		return masterIp;
	}

	public void setMasterIp(String masterIp) {
		if("".equals(this._master_mark)){
			this._master_mark = masterIp+"_";
		}else{
			String[] _master_marks = this._master_mark.split("_");
			this._master_mark = masterIp +"_"+_master_marks[1];
		}
		this.masterIp = masterIp;
	}

	public String getSlaveIp() {
		return slaveIp;
	}

	public void setSlaveIp(String slaveIp) {
		if("".equals(this._slave_mark)){
			this._slave_mark = slaveIp+"_";
		}else{
			String[] _slave_marks = this._slave_mark.split("_");
			this._slave_mark = slaveIp +"_"+_slave_marks[1];
		}
		this.slaveIp = slaveIp;
	}

}
