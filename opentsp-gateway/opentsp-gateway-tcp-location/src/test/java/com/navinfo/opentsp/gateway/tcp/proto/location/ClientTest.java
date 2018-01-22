package com.navinfo.opentsp.gateway.tcp.proto.location;

import com.navinfo.opentsp.gateway.tcp.proto.location.netty.JT808Decoder;
import com.navinfo.opentsp.gateway.tcp.proto.location.netty.JT808Encoder;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.HexUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.List;

import static io.netty.util.ReferenceCountUtil.releaseLater;
import static org.junit.Assert.*;

/**
 * User: zhanhk
 * Date: 16/7/12
 * Time: 上午10:18
 */
public class ClientTest {

    protected static Logger log = Logger.getLogger(ClientTest.class);

    public static void main(String args[]) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChatServerInitializer());

            bootstrap.connect("127.0.0.1", 20400).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends SimpleChannelInboundHandler {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            // 将字符串，构造成ChannelBuffer，传递给服务端
            for (int a = 0; a < 1; a++) {
                String msg = "7e020000bf0182740000020003000000000004000301c059c40639eb740191038d001616052418205301040000000a74920d0018fef6006568006700672c0018fef500644000682a3f200018fee400000000000000000018fee000000000000000040618feff00010000000000000018fee90000000000680000000cf00400000000664600000018feef000000cc04000000c918feee00853e642a0000000018fe5600ca4800000000000018fee500002b01000050000018fefc1700f70000000000008307026400016400053c7e7e020000bf0182740000020003000000000004000301c059c40639eb740191038d001616052418205301040000000a74920d0018fef6006568006700672c0018fef500644000682a3f200018fee400000000000000000018fee000000000000000040618feff00010000000000000018fee90000000000680000000cf00400000000664600000018feef000000cc04000000c918feee00853e642a0000000018fe5600ca4800000000000018fee500002b01000050000018fefc1700f70000000000008307026400016400053c7e";
                char[] chars = msg.toCharArray();
                ByteBuf byteBuf = Unpooled.buffer();
                for (int i = 0; i < chars.length; i++) {
                    if ((i != 0 && i % 2 != 0)) {
                        byteBuf.writeByte(HexUtil.fromHexDigit(String.valueOf(chars[i - 1]) + String.valueOf(chars[i])));
                        //     logger.info(chars[i-1]+chars[i]+"--"+HexUtil.fromHexDigit(String.valueOf(chars[i-1])+String.valueOf(chars[i])));
                    }

                }

                ctx.writeAndFlush(byteBuf).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (future.isSuccess()) {
                            log.info("send success");
                        } else {
                            log.info("send fail");
                        }
                    }
                });
                log.info("send msg :" + msg);
                Thread.sleep(3000);
            }
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            List<byte[]> list = (List<byte[]>) msg;
            for (byte[] packet : list) {
                log.info("Receive:" + HexUtil.bytesToHexString(packet));
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                throws Exception {
            cause.printStackTrace();
            ctx.fireExceptionCaught(cause);
        }
    }

    public static class ChatServerInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new JT808Decoder());
            pipeline.addLast(new JT808Encoder());
            pipeline.addLast("handle", new ClientHandler());
        }
    }


    @Test
    public void testFailSlowTooLongFrameRecovery() throws Exception {
        EmbeddedChannel ch = new EmbeddedChannel(
                new LengthFieldBasedFrameDecoder(5, 0, 4, 0, 4, false));

        for (int i = 0; i < 2; i++) {
            assertFalse(ch.writeInbound(Unpooled.wrappedBuffer(new byte[]{0, 0, 0, 2})));
            try {
                assertTrue(ch.writeInbound(Unpooled.wrappedBuffer(new byte[]{0, 0})));
                fail(DecoderException.class.getSimpleName() + " must be raised.");
            } catch (TooLongFrameException e) {
                // Expected
            }

            ch.writeInbound(Unpooled.wrappedBuffer(new byte[]{0, 0, 0, 1, 'A'}));
            ByteBuf buf = releaseLater((ByteBuf) ch.readInbound());
            assertEquals("A", buf.toString(CharsetUtil.ISO_8859_1));
            buf.release();
        }
    }
}
