package com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache;

import com.navinfo.opentsp.platform.location.kit.Packet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 指令缓存<br>
 * <li>指令超时</li> <li>通道寻找</li> <br>
 * <h2>约定：</h2>
 * <p>
 * 缓存key=数据包唯一标识_流水号<br>  uniqueMark_serialNumber
 * <p>
 * <pre>
 * packet.getUniqueMark() + &quot;_&quot; + packet.getSerialNumber()
 * </pre>
 * Packet : 是业务系统发过来原始包。
 * </p>
 *
 * @author
 */
public class ServerCommonResCache {
    private static Map<String, Packet> cache = new ConcurrentHashMap<String, Packet>();
    private static ServerCommonResCache instance = new ServerCommonResCache();

    private ServerCommonResCache() {
    }

    public static final ServerCommonResCache getInstance() {
        return instance;
    }

    public void addServerCommonResCache(String id, Packet packet) {
        cache.put(id, packet);
    }

    public void delServerCommonResCache(String id) {
        cache.put(id, null);
    }

    public Packet getServerCommonResCache(String id) {
        return cache.get(id);
    }

    public int size() {
        return cache.size();
    }
}
