package com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.netty808;

import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit.PacketProcessing;
import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit.lang.ArraysUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * User: zhanhk
 * Date: 16/7/13
 * Time: 上午9:32
 */
public class JT808Encoder extends MessageToByteEncoder<Packet> {

    private static final Logger log = LoggerFactory.getLogger(JT808Encoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet outpacket, ByteBuf out) throws Exception {
        /**
         * 下行：平台下发的指令中的平台通信标识，如果有变更通信标识，需要替换为通信标识再发送给终端。
         */
        String hexStringUniqueMark = outpacket.getUniqueMark();//terminalId平台通信id
        String tid = hexStringUniqueMark;
        byte[] uniqueMark = Convert.hexStringToBytes(hexStringUniqueMark);
        byte[] serialNumber = Convert.longTobytes(outpacket.getSerialNumber(), 2);
        byte[] content = outpacket.getContent();
        byte[] cmdId = Convert.longTobytes(outpacket.getCommand(), 2);

        if (content != null) {
            if (content.length > LCConstant.TERMINAL_PACKET_MAX_LENGTH_2011) {
                List<byte[]> list = PacketProcessing.dataSegmentation(content, LCConstant.TERMINAL_PACKET_MAX_LENGTH_2011);
                int packetCount = list.size();
                byte[] pkCount = Convert.longTobytes(packetCount, 2);
                for (int i = 0; i < packetCount; i++) {
                    byte[] pkNumber = Convert.longTobytes(i, 2);

                    byte[] pkNode = ArraysUtils.arraycopy(pkCount, pkNumber);
                    byte[] pkProperty = Convert.longTobytes(list.get(i).length + 8192, 2);//8192字节位表示当前数据分包
                    out.writeBytes(this.write(outpacket.getUniqueMark(), cmdId, pkProperty, uniqueMark, serialNumber, pkNode, list.get(i), tid, ctx.channel().toString()));
                }
            } else {
                byte[] pkProperty = Convert.longTobytes(content.length, 2);

                out.writeBytes(this.write(outpacket.getUniqueMark(), cmdId, pkProperty, uniqueMark, serialNumber, null, content, tid, ctx.channel().toString()));
                log.info("TA-->T:" + ctx.channel().toString() + " 回写终端消息 :\r\n" + ByteBufUtil.prettyHexDump(out));

            }
        }
    }


    private byte[] write(String uniqueMarkStr, byte[] cmdId, byte[] pkProperty, byte[] uniqueMark,
                         byte[] serialNumber, byte[] pkNode, byte[] pkContent, String tid, String channel) {
        //消息头尾=2、消息ID=2、消息体属性=2、终端手机号=6、消息流水号=2、消息包装项=4、校验码=1
        int byteLenght = pkNode == null ? pkContent.length + 13 : pkContent.length + 17;
        byte[] data = new byte[byteLenght];
        ArraysUtils.arrayappend(data, 0, cmdId);
        ArraysUtils.arrayappend(data, 2, pkProperty);
        ArraysUtils.arrayappend(data, 4, uniqueMark);
        ArraysUtils.arrayappend(data, 10, serialNumber);
        if (pkNode != null) {// 消息封装项
            ArraysUtils.arrayappend(data, 12, pkNode);
            ArraysUtils.arrayappend(data, 16, pkContent);
            ArraysUtils.arrayappend(data, data.length - 1,
                    new byte[]{PacketProcessing.checkPackage(data, 0, data.length - 1)});
        } else {
            ArraysUtils.arrayappend(data, 12, pkContent);
            ArraysUtils.arrayappend(data, data.length - 1,
                    new byte[]{PacketProcessing.checkPackage(data, 0, data.length - 1)});
        }

        byte[] bytes = PacketProcessing.escape(data, LCConstant.TERMINAL_ESCAPE_2011, LCConstant.TERMINAL_TO_ESCAPE_2011);
        byte[] resultBytes = new byte[bytes.length + 2];
        resultBytes[0] = 0x7E;
        for (int i = 1, length = bytes.length; i <= length; i++) {
            resultBytes[i] = bytes[i - 1];
        }
        resultBytes[resultBytes.length - 1] = 0x7E;

        return resultBytes;

    }
}
