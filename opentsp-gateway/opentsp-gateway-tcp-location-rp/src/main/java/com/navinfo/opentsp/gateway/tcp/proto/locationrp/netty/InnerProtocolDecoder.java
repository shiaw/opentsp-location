package com.navinfo.opentsp.gateway.tcp.proto.locationrp.netty;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.DataPacketCache;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * 内容协议decoder
 */
public class InnerProtocolDecoder extends ByteToMessageDecoder {

    private static final Logger log = LoggerFactory.getLogger(InnerProtocolDecoder.class);

    /**
     * 最小包长
     */
    @Value("${opentsp.location.decoder.minimum:13}")
    private int minimum;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        String channelKey = ctx.channel().remoteAddress().toString();
        String startPacker = DataPacketCache.getInstance().get(channelKey);
        //logger.info("[RP<-上层业务系统("+getRemoteAddress(session)[0].substring(1)+":"+ getRemoteAddress(session)[1].substring(1)+")]:" + in.getHexDump());
        log.info("[RP<HY]: {}", ByteBufUtil.prettyHexDump(in));
        if (in.writerIndex() > in.readerIndex()) {
            byte[] bytes = new byte[in.writerIndex() - in.readerIndex()];
            in.readBytes(bytes);
            log.info("HY-->RP:" + ctx.channel().toString() + "收到终端原始数据:" + Convert.bytesToHexString(bytes));
            if(startPacker != null) {
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
                    if (0x7B == bytes[i]) {// 寻找包尾
                        if (minimum > 0) {// 是否做最小包长验证
                            if (i - index + 1 < 13) {
                                index = i;
                            } else {
                                temp = ArraysUtils.subarrays(bytes, index, i - index + 1);
                                result.add(temp);
                                index = -1;
                                log.info(ctx.channel().toString() + "解析分包[" + (result.size() - 1) + "]" + Convert.bytesToHexString(temp));
                            }
                        } else {
                            temp = ArraysUtils.subarrays(bytes, index, i - index + 1);
                            result.add(temp);
                            index = -1;
                            log.info(ctx.channel().toString() + "解析分包[" + (result.size() - 1) + "]" + Convert.bytesToHexString(temp));
                        }
                    }
                }
            }
            if (index != -1)
                result.add(ArraysUtils.subarrays(bytes, index, bytes.length - index));

            for (byte[] packet : result) {
                if(packet != null){
                    if(packet[0] == LCConstant.pkBegin && packet[packet.length -1] == LCConstant.pkEnd){
                        DataPacketCache.getInstance().remove(channelKey);
                        //符合完整包派发
                        out.add(packet);
                    }else if(packet[0] == LCConstant.pkBegin){
                        //不符合加入缓存
                        DataPacketCache.getInstance().add(channelKey, Convert.bytesToHexString(packet));
                    }
                }
            }
        }
    }
}
