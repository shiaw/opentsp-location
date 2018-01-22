package com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.entry.SubscribeEntry;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.entry.TerminalEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 终端列表<br>
 * 包含此节点所有处理终端的列表
 *
 * @author lgw
 * @see TerminalEntity
 */
public class TerminalManage {

    private static Map<String, TerminalEntity> cache = new ConcurrentHashMap<String, TerminalEntity>();
    //订阅管理
    private static Map<String, SubscribeEntry> subscribes = new ConcurrentHashMap<>();

    private static TerminalManage instance = new TerminalManage();

    private TerminalManage() {
    }

    public static TerminalManage getInstance() {
        return instance;
    }

    /**
     * 获取终端订阅信息
     *
     * @param terminalId
     * @return
     */
    public SubscribeEntry getSubscribe(String terminalId) {
        return subscribes.get(terminalId);
    }

    /**
     * 更新订阅信息
     *
     * @param subscribeEntry
     */
    public void updateSubscribe(SubscribeEntry subscribeEntry) {
        subscribes.put(subscribeEntry.getTerminalId(), subscribeEntry);
    }

    public void addTerminal(TerminalEntity terminal) {
        cache.put("0" + terminal.getTerminalId(), terminal);
    }

    /**
     * 获取终端信息
     *
     * @param terminalId {@link String} 终端标识(存储于数据库中的标识)
     * @return {@link TerminalEntity}
     */
    public TerminalEntity getTerminal(String terminalId) {
        return cache.get("0" + terminalId);
    }

    /**
     * 获取列表
     *
     * @return
     */
    public Map<String, TerminalEntity> get() {
        Map<String, TerminalEntity> temp = cache;
        return temp;
    }

}
