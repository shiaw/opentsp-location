package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol;

import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;

/**
 * 协议解析基类
 *
 * @author lgw
 */
public abstract class Command {

    public abstract Packet processor(NettyClientConnection connection,Packet packet);

}
