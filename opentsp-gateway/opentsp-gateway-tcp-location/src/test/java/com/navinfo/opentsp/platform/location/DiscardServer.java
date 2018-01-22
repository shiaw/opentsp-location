package com.navinfo.opentsp.platform.location;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;

/**
 * User: zhanhk
 * Date: 16/7/18
 * Time: 下午1:54
 */
public final class DiscardServer {

    static final boolean SSL = System.getProperty("ssl") != null;
    static final int PORT = Integer.parseInt(System.getProperty("port", "8009"));
    static final int MAXGLOBALTHROUGHPUT = Integer.parseInt(System.getProperty("maxGlobalThroughput", "0"));
    static final int MAXCHANNELTHROUGHPUT = Integer.parseInt(System.getProperty("maxChannelThroughput", "0"));

    public static void main(String[] args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx;
        if (SSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
        } else {
            sslCtx = null;
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        final GlobalTrafficShapingHandler gtsh;
        final GlobalChannelTrafficShapingHandlerWithLog gctsh;
        if (MAXGLOBALTHROUGHPUT > 0 && MAXCHANNELTHROUGHPUT > 0) {
            gctsh = new GlobalChannelTrafficShapingHandlerWithLog(workerGroup, 0, MAXGLOBALTHROUGHPUT,
                    0, MAXCHANNELTHROUGHPUT, 1000);
            gtsh = null;
        } else if (MAXGLOBALTHROUGHPUT > 0) {
            gtsh = new GlobalTrafficShapingHandler(workerGroup, 0, MAXGLOBALTHROUGHPUT, 1000);
            gctsh = null;
        } else {
            gtsh = null;
            gctsh = null;
        }
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc()));
                            }
                            if (MAXGLOBALTHROUGHPUT > 0 && MAXCHANNELTHROUGHPUT > 0) {
                                p.addLast(gctsh);
                            } else if (MAXGLOBALTHROUGHPUT > 0) {
                                p.addLast(gtsh);
                            } else if (MAXCHANNELTHROUGHPUT > 0) {
                                p.addLast(new ChannelTrafficShapingHandler(0, MAXCHANNELTHROUGHPUT, 1000));
                            }
                            p.addLast(new DiscardServerHandler());
                        }
                    });

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(PORT).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
