package com.navinfo.opentsp.gateway.tcp.proto.location;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * User: zhanhk
 * Date: 16/7/18
 * Time: 下午1:52
 */
public class DiscardClientHandler extends SimpleChannelInboundHandler<Object> {

    private ByteBuf content;
    private ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;

        // Initialize the message.
        content = ctx.alloc().directBuffer(DiscardClient.SIZE).writeZero(DiscardClient.SIZE);

        // Send the initial messages.
        generateTraffic();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        content.release();
    }

    public void messageReceived(ChannelHandlerContext ctx, Object msg) {
        // Server is supposed to send nothing, but if it sends something, discard it.
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    long counter;

    private void generateTraffic() {
        if (DiscardClient.useIsWritable) {
            // Flush the outbound buffer to the socket.
            // But checked the isWritable() property before generating the same amount of traffic again.
            while (ctx.channel().isWritable()) {
                ctx.writeAndFlush(content.duplicate().retain());
            }
        } else {
            // Flush the outbound buffer to the socket.
            // Once flushed, generate the same amount of traffic again.
            ctx.writeAndFlush(content.duplicate().retain()).addListener(trafficGenerator);
        }
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        if (DiscardClient.useIsWritable && ctx.channel().isWritable()) {
            // We are in useIsWritable mode and the channel is again writable
            generateTraffic();
        }
        ctx.fireChannelWritabilityChanged();
    }

    private final ChannelFutureListener trafficGenerator = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) {
            if (future.isSuccess()) {
                generateTraffic();
            } else {
                future.cause().printStackTrace();
                future.channel().close();
            }
        }
    };

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

    }
}