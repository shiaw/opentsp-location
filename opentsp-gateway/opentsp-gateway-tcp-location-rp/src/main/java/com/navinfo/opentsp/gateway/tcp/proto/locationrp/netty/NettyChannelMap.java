package com.navinfo.opentsp.gateway.tcp.proto.locationrp.netty;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lenovo
 * @date 2016-11-09
 * @modify
 * @copyright
 */
public class NettyChannelMap {
    public static BiMap<String, String> biMap = HashBiMap.create();

    public static BiMap<String, String> deviceChannelMap = Maps.synchronizedBiMap(biMap);

    public static Map<String, NettyClientConnection> deviceChannel = new HashMap<>();

    public static Map<String, String> userMap = new ConcurrentHashMap<>();

    public static void put(NettyClientConnection connection) {
        deviceChannel.put(connection.getDevice(), connection);
    }

    public static void remove(NettyClientConnection connection) {
        deviceChannel.remove(connection.getDevice());
    }

    public static BiMap<String, String> getDeviceChannelMap() {
        return deviceChannelMap;
    }
}