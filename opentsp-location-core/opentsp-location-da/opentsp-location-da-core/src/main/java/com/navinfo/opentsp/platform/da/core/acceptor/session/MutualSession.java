package com.navinfo.opentsp.platform.da.core.acceptor.session;

import java.io.Serializable;

import org.apache.mina.core.session.IoSession;

public class MutualSession implements Serializable {
	private static final long serialVersionUID = 1L;
	private long id;
	private IoSession ioSession;
	private long firstConnectTime;
	private long lastMutualTime;
	private byte[] stickPack;
	private boolean isAuth = false;
	private long upFlow;
	private long downFlow;

	public MutualSession() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public IoSession getIoSession() {
		return ioSession;
	}

	public void setIoSession(IoSession ioSession) {
		this.ioSession = ioSession;
	}

	public long getFirstConnectTime() {
		return firstConnectTime;
	}

	public void setFirstConnectTime(long firstConnectTime) {
		this.firstConnectTime = firstConnectTime;
	}

	public long getLastMutualTime() {
		return lastMutualTime;
	}

	public void setLastMutualTime(long lastMutualTime) {
		this.lastMutualTime = lastMutualTime;
	}

	public byte[] getStickPack() {
		byte[] temp = stickPack;
		stickPack = null;
		return temp;
	}

	public void setStickPack(byte[] stickPack) {
		this.stickPack = stickPack;
	}

	public boolean isAuth() {
		return isAuth;
	}

	public void setAuth(boolean isAuth) {
		this.isAuth = isAuth;
	}

	/**
	 * 累计流量
	 *
	 * @param upOrDown
	 *            {@link Integer} 1：上行;2：下行
	 * @param size
	 *            {@link Integer} 本次数据大小
	 */
	public void plusFlow(int upOrDown, int size) {
		if (upOrDown == 1) {
			this.upFlow += size;
		} else if (upOrDown == 2) {
			this.downFlow += size;
		}
	}

	public long getUpFlow() {
		return upFlow;
	}

	public long getDownFlow() {
		return downFlow;
	}

}
