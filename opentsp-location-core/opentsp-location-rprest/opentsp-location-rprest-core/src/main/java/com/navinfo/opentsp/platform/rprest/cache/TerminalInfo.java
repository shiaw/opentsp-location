package com.navinfo.opentsp.platform.rprest.cache;

import java.io.Serializable;

public class TerminalInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String terminalId;
    private int protocolType;
    private String authCode;
    private boolean isAuth;

    private String deviceId;//设备id 厂商的硬件ID
    private long changeTid;//变更通信id
//    private BusinessType type;//0默认，1江淮


    private long lastTime;

    public TerminalInfo() {
        super();
    }
    /**
     * 获取链路对象
     * @return {@link MutualSession}
     */
//	public MutualSession getMutualSession(){
//		if(sessionId != NOT_SESSION){
//			MutualSession mutualSession = MutualSessionManage.getInstance().getMutualSession(sessionId);
//			if(mutualSession == null || mutualSession.getIoSession().isClosing()){
//				this.setSessionId(NOT_SESSION);
//				return null;
//			}else{
//				return mutualSession;
//			}
//		}
//		return null;
//	}
    /**
     * 判断终端当前是否连接
     * @return
     */
//	public boolean isActive(){
//		if(this.sessionId == NOT_SESSION) return false;
//		MutualSession mutualSession = MutualSessionManage.getInstance().getMutualSession(sessionId);
//		if(mutualSession == null || mutualSession.getIoSession().isClosing()){
//			this.setSessionId(NOT_SESSION);
//			return false;
//		}
//		return true;
//	}

    /**
     * 判断终端是否在线
     *
     * @return
     */
//	public boolean isOnLine(){
//		if(this.sessionId == NOT_SESSION){
//			return false;
//		}
//		long time = NodeHelper.getCurrentTime();
//		if(this.lastTime - time > onOffLineTime){
//			return false;
//		}
//		return true;
//	}
    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }


    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }


    public int getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(int protocolType) {
        this.protocolType = protocolType;
    }


    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getChangeTid() {
        return changeTid;
    }

    public void setChangeTid(long changeTid) {
        this.changeTid = changeTid;
    }

    //TODO 需要处理
//	public BusinessType getType() {
//		return type;
//	}
//	public void setType(BusinessType type) {
//		this.type = type;
//	}
    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }


}
