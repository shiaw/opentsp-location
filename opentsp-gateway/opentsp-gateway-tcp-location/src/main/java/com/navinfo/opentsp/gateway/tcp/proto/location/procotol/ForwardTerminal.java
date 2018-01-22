package com.navinfo.opentsp.gateway.tcp.proto.location.procotol;

import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TerminalProtoVersionCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by wanliang on 2016/12/14.
 */

@Component
public class ForwardTerminal {

    @Autowired
    private ProtocolDispatcher protocolDispatcher;


    @Autowired
    private TerminalProtoVersionCache terminalProtoVersionCache;


    public PacketResult forward(NettyClientConnection connection, Packet packet) {

        Command command = this.protocolDispatcher.getHandler(packet.getCommandForHex());
        String protoCode = terminalProtoVersionCache.get(packet.getUniqueMark());
        if (command != null) {
            return command.processor(connection, packet);
        }
        if (command == null && !org.springframework.util.StringUtils.isEmpty(protoCode)) {
            command = this.protocolDispatcher.getHandler(protoCode + "_" + packet.getCommandForHex());
        }
        return command.processor(connection, packet);
    }
}

