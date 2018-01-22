package com.navinfo.opentsp.gateway.tcp.proto.location.netty;

//import com.navinfo.opentsp.gateway.tcp.proto.location.handler.ClientDispatchTcpHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by hk on 2016/11/14.
 */

@Component
public class ClientInitializer extends ChannelInitializer<Channel> {

    @Value("${tcp.heartbeat.time:300}")
    private int heartbeat;

    @Autowired
    private  ClientDispatchTcpHandler clientDispatchTcpHandler;

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("ping", new IdleStateHandler(heartbeat, 0, 0, TimeUnit.SECONDS));
        pipeline.addLast(new DelimiterBasedFrameDecoder(1024, Delimiters.lineDelimiter()));
        pipeline.addLast(new ByteArrayDecoder());
        pipeline.addLast(new ByteArrayEncoder());
        pipeline.addLast("handle", clientDispatchTcpHandler);
    }

    public ClientDispatchTcpHandler getClientDispatchTcpHandler() {
        return clientDispatchTcpHandler;
    }

    public void setClientDispatchTcpHandler(ClientDispatchTcpHandler clientDispatchTcpHandler) {
        this.clientDispatchTcpHandler = clientDispatchTcpHandler;
    }
}
