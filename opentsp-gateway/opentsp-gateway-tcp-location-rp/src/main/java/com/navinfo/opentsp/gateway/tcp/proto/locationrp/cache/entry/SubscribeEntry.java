package com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.entry;

import java.io.Serializable;
import java.util.Vector;

public class SubscribeEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private String terminalId;

    /**
     * 上层业务系统标识
     */
    private Vector<String> uniqueMarks;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }


    /**
     * 数据订阅
     *
     * @param uniqueMark
     */
    public void dataSubscribe(String uniqueMark) {
        if (this.uniqueMarks == null) {
            this.uniqueMarks = new Vector<>();
            this.uniqueMarks.add(uniqueMark);
        } else {
            if (!this.uniqueMarks.contains(uniqueMark)) {
                this.uniqueMarks.add(uniqueMark);
            }
        }
    }

    /**
     * 取消订阅
     *
     * @param uniqueMark
     * @return {@link Integer} 1：取消成功,2：消息失败或者未找到需要消息的订阅
     */
    public int dataUnsubscribe(String uniqueMark) {
        if (this.uniqueMarks == null)
            return 0;

        return this.uniqueMarks.removeElement(uniqueMark) ? 1 : 0;
    }

    public Vector<String> getUniqueMarks() {
        return uniqueMarks;
    }

    /**
     * 获取订阅链路列表
     *
     * @return {@link com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection}
     */
    /*public List<ClientConnection> getSubscribeSession() {
        List<ClientConnection> result = new ArrayList<>();
        if (this.uniqueMarks != null) {
            Iterator<String> iterator = this.uniqueMarks.iterator();
            while (iterator.hasNext()) {
                String uniqueMark = iterator.next();
                ClientConnection connection = connections.getConnectionByDevice(uniqueMark + "");
                // 链路空或者关闭,移除订阅
                if (connection == null) {
                    continue;
                } else {
                    result.add(connection);
                }
            }
        }
        return result;
    }*/


}
