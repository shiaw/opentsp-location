package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.send;

import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.DownCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.platform.auth.DownStatusCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.push.DownCommandState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

@LocationCommand(id = "8107",version = "200110")
public class JT_8107_TerminalAttribute extends TerminalCommand {

    @Autowired
    private MessageChannel messageChannel;
	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		//return super.write(packet);
        PacketResult packetResult=new PacketResult();
        packetResult.setTerminalPacket(packet);
        return packetResult;
	}

}
