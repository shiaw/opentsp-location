package com.navinfo.opentsp.gateway.tcp.proto.locationrp.handler;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.TerminalProtoVersionCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.ProtocolDispatcher;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnections;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Adapter which handle outgoing messages and convert its into protocol packages.
 */
@ChannelHandler.Sharable
@Component
public class LoactionOutboundHandler extends ChannelOutboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(LoactionOutboundHandler.class);
    @Autowired
    private ProtocolDispatcher protocolDispatcher;

    @Autowired
    protected NettyClientConnections connections;

    private static final String GET_PROTO_VERSION_COMMAND = "getProtoVersion";

    @Autowired
    private TerminalProtoVersionCache terminalProtoVersionCache;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Assert.notNull(msg, "msg is null");
        super.write(ctx, msg, promise);
    }
}
