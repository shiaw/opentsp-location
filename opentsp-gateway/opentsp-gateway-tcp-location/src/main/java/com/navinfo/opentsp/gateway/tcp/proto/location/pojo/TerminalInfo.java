package com.navinfo.opentsp.gateway.tcp.proto.location.pojo;


import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;

import java.io.Serializable;

public class TerminalInfo implements Serializable {
    //private final static int onOffLineTime = Configuration.getInt(ConfigKey.onOffLineTime);
    private static final long serialVersionUID = 1L;
    public static final long NOT_SESSION = -1;
    private long terminalId;
    private long sessionId = NOT_SESSION;
    private int protocolType;
    private String authCode;
    private boolean isAuth;

    private String deviceId;//设备id
    private long changeTid;//变更通信id
//    private BusinessType type;//0默认，1江淮


    private int upFlow;
    private int downFlow;
    private long lastTime;
    /**
     * 是否发送过掉线通知,在终端再次上线后需要更新此标记
     */
    private boolean isSendOnLineMark = false;
    /**
     * 是否发送过上线通知,在终端掉线后需要更新此标记
     */
    private boolean isSendOffLineMark = true;


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

    public boolean isSendOnLineMark() {
        return isSendOnLineMark;
    }

    public void setSendOnLineMark(boolean isSendOnLineMark) {
        this.isSendOnLineMark = isSendOnLineMark;
    }

    public boolean isSendOffLineMark() {
        return isSendOffLineMark;
    }

    public void setSendOffLineMark(boolean isSendOffLineMark) {
        this.isSendOffLineMark = isSendOffLineMark;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(long terminalId) {
        this.terminalId = terminalId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sid) {
//		if(this.sessionId != NOT_SESSION && sid != NOT_SESSION){
//			MutualSessionManage.getInstance().destroyLink(this.sessionId);
//		}
        this.sessionId = sid;
    }

    public int getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(int protocolType) {
        this.protocolType = protocolType;
    }

    /**
     * 累计流量
     *
     * @param  {@link Integer} 1：上行;2：下行
     * @param size     {@link Integer} 本次数据大小
     */
    public void plusFlow(LCConstant.FlowDirection flowDirection, int size) {
        if (flowDirection.ordinal() == LCConstant.FlowDirection.Up.ordinal()) {
            this.upFlow += size;
        } else if (LCConstant.FlowDirection.Down.ordinal() == flowDirection.ordinal()) {
            this.downFlow += size;
        }
    }

    private Object lock = new Object();

    public int getUpFlow(boolean isReset) {
        if (isReset) {
            synchronized (lock) {
                int flow = this.upFlow;
                this.upFlow = 0;
                return flow;
            }
        }
        return upFlow;
    }

    public int getDownFlow(boolean isReset) {
        if (isReset) {
            synchronized (lock) {
                int flow = this.downFlow;
                this.downFlow = 0;
                return flow;
            }
        }
        return downFlow;
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
