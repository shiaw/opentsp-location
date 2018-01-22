package com.navinfo.opentsp.gateway.tcp.proto.location.procotol;

import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;

/**
 * 协议解析基类
 *
 * @author lgw
 */
public abstract class Command {

    public abstract PacketResult processor(NettyClientConnection connection, Packet packet);

}
