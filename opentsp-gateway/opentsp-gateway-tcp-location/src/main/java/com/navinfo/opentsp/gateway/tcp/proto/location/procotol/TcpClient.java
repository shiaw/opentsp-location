package com.navinfo.opentsp.gateway.tcp.proto.location.procotol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.Lifecycle;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanliang on 2017/3/23.
 * 转发数据
 */
public class TcpClient implements Lifecycle {
    private static final Logger LOG = LoggerFactory.getLogger(TcpClient.class);
    private final Bootstrap clientBootstrap;
    private final EventLoopGroup group;
    private volatile boolean started;
    private  ChannelFuture future = null;


    private final String host;

    private final int port;

    public TcpClient(String host, int port, ChannelHandler channelHandler) {
        this.host = host;
        this.port = port;
        clientBootstrap = new Bootstrap();
        this.group = new NioEventLoopGroup();
        clientBootstrap.channel(NioSocketChannel.class);
        clientBootstrap.group(group);
        clientBootstrap.handler(channelHandler);
    }

    @Override
    public void start() {
        try {
            if(StringUtils.isEmpty(host)||port<1){
                LOG.error("Don't forward capability");
                return;
            }
            future = clientBootstrap.connect(new InetSocketAddress(
                    host, port));
            future.addListener(new ChannelFutureListener(){
                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (f.isSuccess()) {
                        LOG.info("TAClient connect success, {}:{}", host, port);
                        started = true;
                    } else {
                        LOG.info("TAClient connect failed,  {}:{}", host, port);
                        f.channel().eventLoop().schedule(new Runnable() {
                            @Override
                            public void run() {
                                start();
                            }
                        }, 60, TimeUnit.SECONDS);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Unable to start TCP client", e);
            start();
        }
    }

    @Override
    public void stop() {
        if (!started) {
            return;
        }
        try {
            future.channel().close();
        } catch (Exception e) {
            LOG.warn("Unable to stop client", e);
        } finally {
            group.shutdownGracefully(0, 0, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public boolean isRunning() {
        return started;
    }

    public Channel getChannel() throws InterruptedException {
        if(future ==null){
            return null;
        }
        return future.sync().channel();
    }
}
