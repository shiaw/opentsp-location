package com.navinfo.opentsp.gateway.tcp.proto.locationrp.netty;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.handler.LoactionOutboundHandler;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnections;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Initializer for channel which support Landu
 */
@Component
public final class LocationChannelInitializer extends ChannelInitializer<Channel> {

    private final NettyClientConnections connections;

    private DispatchTcpHandler dispatchTcpHandler;

    @Value("${tcp.heartbeat.time:300}")
    private int heartbeat;

    private final LoactionOutboundHandler outboundHandler;

    @Autowired
    public LocationChannelInitializer(@Qualifier("locationConnections") NettyClientConnections connections,
                                      DispatchTcpHandler dispatchTcpHandler,
                                      LoactionOutboundHandler outboundHandler) {
        this.connections = connections;
        this.dispatchTcpHandler = dispatchTcpHandler;
        this.outboundHandler = outboundHandler;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //配置服务端监听读超时，即无法收到客户端发的心跳信息的最长时间间隔：默认5分钟
        pipeline.addLast("ping", new IdleStateHandler(heartbeat, 0, 0, TimeUnit.SECONDS));
        pipeline.addLast(new InnerProtocolDecoder());
        pipeline.addLast(new InnerProtocolEncoder());
        // register handler which count and memorize opened channels
        pipeline.addLast(this.connections.getChannelHandler());
        pipeline.addLast(outboundHandler);
        pipeline.addLast("handle", dispatchTcpHandler);
    }
}
