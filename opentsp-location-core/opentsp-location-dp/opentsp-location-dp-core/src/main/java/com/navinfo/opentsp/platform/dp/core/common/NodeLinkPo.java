package com.navinfo.opentsp.platform.dp.core.common;

/**
 * 终端对应集群内节点链路
 * User: zhanhk
 * Date: 16/8/16
 * Time: 上午10:05
 */
public class NodeLinkPo {

    private long taNode;

    private long dpNode;

    private long rpNode;

    private boolean onlineStatus;//在线状态 true 在线 false 不在线

    public NodeLinkPo() {
    }

    public boolean isOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public long getTaNode() {
        return taNode;
    }

    public void setTaNode(long taNode) {
        this.taNode = taNode;
    }

    public long getDpNode() {
        return dpNode;
    }

    public void setDpNode(long dpNode) {
        this.dpNode = dpNode;
    }

    public long getRpNode() {
        return rpNode;
    }

    public void setRpNode(long rpNode) {
        this.rpNode = rpNode;
    }
}
