package com.navinfo.opentsp.gateway.tcp.proto.location.netty;

import com.navinfo.opentsp.gateway.tcp.proto.location.cache.DataPacketCache;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * JT808协议decoder
 * User: zhanhk
 * Date: 16/7/11
 * Time: 下午2:58
 */
public class JT808Decoder extends ByteToMessageDecoder {

    private static final Logger log = LoggerFactory.getLogger(JT808Decoder.class);

    /**
     * 最小包长
     */
    @Value("${opentsp.location.decoder.minimum:13}")
    private int minimum;

    @Value("${opentsp.location.ta.source.log:false}")
    private boolean taSLog;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // if(taSLog) {
//            log.info("T-->TA:" + ctx.channel().toString() + "收到终端原始数据:\r\n" + ByteBufUtil.prettyHexDump(in));
        //}
        String channelKey = ctx.channel().remoteAddress().toString();
        String startPacker = DataPacketCache.getInstance().get(channelKey);
        byte[] bytes = null;
        if (in.writerIndex() > in.readerIndex()) {
            bytes = new byte[in.writerIndex() - in.readerIndex()];
            in.readBytes(bytes);
            if (startPacker != null) {
                //有分包拼接上一包尾数据
                byte[] lastBytes = Convert.hexStringToBytes(startPacker);
                bytes = ArraysUtils.arraycopy(lastBytes, bytes);
            }
            List<byte[]> result = new ArrayList<>();
            int index = -1;
            for (int i = 0, length = bytes.length; i < length; i++) {
                byte[] temp;
                if (index == -1) {// 寻找包头
                    if (0x7E == bytes[i]) {
                        index = i;
                    }
                } else {
                    if (0x7E == bytes[i]) {// 寻找包尾
                        if (minimum > 0) {// 是否做最小包长验证
                            if (i - index + 1 < 13) {
                                index = i;
                            } else {
                                temp = ArraysUtils.subarrays(bytes, index, i - index + 1);
                                result.add(temp);
                                index = -1;
                                //log.info(ctx.channel().toString() + "解析分包[" + (result.size() - 1) + "]" + Convert.bytesToHexString(temp));
                            }
                        } else {
                            temp = ArraysUtils.subarrays(bytes, index, i - index + 1);
                            result.add(temp);
                            index = -1;
                            //log.info(ctx.channel().toString() + "解析分包[" + (result.size() - 1) + "]" + Convert.bytesToHexString(temp));
                        }
                    }
                }
            }
            if (index != -1) {
                result.add(ArraysUtils.subarrays(bytes, index, bytes.length - index));
            }

            for (byte[] packet : result) {
                if (packet != null) {
                    //TODO 把报文转换提出来cj 171222
                    if (packet[0] == LCConstant.pkBegin && packet[packet.length - 1] == LCConstant.TERMINAL_PK_END_2011) {
                        if (packet.length == 1 && "7E".equals(Convert.bytesToHexString(packet))) {
                            DataPacketCache.getInstance().add(channelKey, Convert.bytesToHexString(packet));
                            return;
                        } else if ("7E7E".equals(Convert.bytesToHexString(packet))) {
                            return;
                        }
                        DataPacketCache.getInstance().remove(channelKey);
                        log.info("T-->TA:" + Convert.bytesToHexString(packet));
                        //符合完整包派发
                        out.add(packet);
                    } else if (packet[0] == LCConstant.pkBegin) {
                        //不符合加入缓存
                        DataPacketCache.getInstance().add(channelKey, Convert.bytesToHexString(packet));
                    }
                }
            }
        }
    }

}
