package com.navinfo.opentsp.platform.da.core.connector.mm.session;

import com.navinfo.opentsp.platform.location.kit.LCConstant.MasterSlave;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class MMMutualSessionManage {
	private static Map<Long, MMMutualSession> sessionList = new ConcurrentHashMap<Long, MMMutualSession>();
	private static Map<String, Long> masterSlaver = new ConcurrentHashMap<String, Long>();
	private static Map<Long, Long> nodeCodeMapping = new ConcurrentHashMap<Long, Long>();

	private static MMMutualSessionManage instance = new MMMutualSessionManage();

	private MMMutualSessionManage() {
	}

	public final static MMMutualSessionManage getInstance() {
		return instance;
	}
	/////////////////Session链路管理/////////////////////////////
	public MMMutualSession addMutualSession(Long key,
			MMMutualSession mutualSession) {
		return sessionList.put(key, mutualSession);
	}

	public boolean removeMutualSession(Long key) {
		return sessionList.remove(key) != null;
	}

	public MMMutualSession getMutualSession(Long key) {
		return sessionList.get(key);
	}

	public Map<Long, MMMutualSession> get() {
		Map<Long, MMMutualSession> temp = sessionList;
		return temp;
	}

	//////////////////节点编号与链路映射//////////////////////////////
	/**
	 * 节点与链路映射
	 * @param nodeCode
	 * @param sessionId
	 */
	public void nodeCodeSessionBind(long nodeCode , long sessionId){
		nodeCodeMapping.put(nodeCode, sessionId);
	}
	/**
	 * 据节点编号获取链路
	 * @param nodeCode
	 * @return
	 */
	public MMMutualSession getMMMutualSessionForCode(long nodeCode){
		Long sessionId = nodeCodeMapping.get(nodeCode);
		if(sessionId != null){
			return sessionList.get(sessionId);
		}
		return null;
	}
	///////////////////////////////////////////////////////////////////////

	////////////////////////主从链路管理/////////////////////////////////////
	/**
	 * 主从链路映射
	 * @param ms
	 * @param sessionId
	 */
	public void masterOrSlaveBindSession(MasterSlave ms , long sessionId){
		masterSlaver.put(ms.name(), sessionId);
	}
	/**
	 * 获取MM链路<br>
	 * 优先获取Master,当Master断开,获取Slave
	 * 
	 * @return
	 */
	public MMMutualSession getMM() {
		MMMutualSession mmMutualSession = this.getMaster();
		if (mmMutualSession == null) {
		}
		mmMutualSession = this.getSlave();
		return mmMutualSession;
	}

	private MMMutualSession getMaster() {
		Long sessionId = masterSlaver.get(MasterSlave.Master.name());
		if (sessionId != null) {
			MMMutualSession masterSession = sessionList.get(sessionId);
			if (masterSession != null) {
				if (masterSession.getIoSession() != null
						|| masterSession.getIoSession().isConnected()) {
					return masterSession;
				}
			}
		}
		return null;
	}

	private MMMutualSession getSlave() {
		Long sessionId = masterSlaver.get(MasterSlave.Slave.name());
		if (sessionId != null) {
			MMMutualSession slaveSession = sessionList.get(sessionId);
			if (slaveSession != null) {
				if (slaveSession.getIoSession() != null
						|| slaveSession.getIoSession().isConnected()) {
					return slaveSession;
				}
			}
		}
		return null;
	}
}
