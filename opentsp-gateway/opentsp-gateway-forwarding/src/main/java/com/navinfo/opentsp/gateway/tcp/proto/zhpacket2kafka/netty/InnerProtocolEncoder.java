package com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.netty;

import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit.PacketProcessing;
import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit.lang.ArraysUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class InnerProtocolEncoder extends MessageToByteEncoder<Packet> {

    private static final Logger log = LoggerFactory.getLogger(InnerProtocolEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet outpacket, ByteBuf out) throws Exception {

        byte[] messageType = Convert.intTobytes(outpacket.getProtocol(), 1);
        byte[] uniqueMark = Convert.hexStringToBytes(outpacket.getUniqueMark());
        byte[] serialNumber = Convert.longTobytes(outpacket.getSerialNumber(), 2);
        byte[] content = outpacket.getContent();
        byte[] cmdId = Convert.longTobytes(outpacket.getCommand(), 2);

        if (content != null) {
            if (content.length > LCConstant.PACKET_MAX_LENGTH) {
                List<byte[]> list = PacketProcessing.dataSegmentation(content, LCConstant.PACKET_MAX_LENGTH);
                int packetCount = list.size();
                byte[] pkCount = Convert.longTobytes(packetCount, 2);
                byte[] blockId = Convert.intTobytes(PacketProcessing.getBlockId(), 2);
                for (int i = 0; i < packetCount; i++) {
                    byte[] pkNumber = Convert.longTobytes(i + 1, 2);

                    byte[] pkNode = ArraysUtils.arraycopy(pkCount, pkNumber);
                    pkNode = ArraysUtils.arraycopy(pkNode, blockId);

                    byte[] pkProperty = Convert.longTobytes(list.get(i).length + 8192, 2);//8192字节位表示当前数据分包

                    this.write(messageType, cmdId, pkProperty, uniqueMark, serialNumber, pkNode, list.get(i), out, ctx);
                }
            } else {
                byte[] pkProperty = Convert.longTobytes(content.length, 2);
                this.write(messageType, cmdId, pkProperty, uniqueMark, serialNumber, null, content, out, ctx);
            }
        }

    }


    private void write(byte[] messageType, byte[] cmdId, byte[] pkProperty, byte[] uniqueMark,
                       byte[] serialNumber, byte[] pkNode, byte[] pkContent, ByteBuf out, ChannelHandlerContext ctx) {
        //首标识位=1		消息类型=1 	消息头 ={消息ID=2		消息体属性=2		唯一标识=6		消息流水号=2		消息封包项=4}		消息体=N		校验码=1		尾标识位=1
        int contentLength = pkContent != null ? pkContent.length : 0;
        int pkLength = pkNode == null ? contentLength + 14 : contentLength + 20;
        byte[] data = new byte[pkLength];
        ArraysUtils.arrayappend(data, 0, messageType);//消息类型
        ArraysUtils.arrayappend(data, 1, cmdId);//指令号
        ArraysUtils.arrayappend(data, 3, pkProperty);//消息体属性
        ArraysUtils.arrayappend(data, 5, uniqueMark);//唯一标识
        ArraysUtils.arrayappend(data, 11, serialNumber);//流水号
        if (pkNode != null) {// 消息封装项
            ArraysUtils.arrayappend(data, 13, pkNode);
            ArraysUtils.arrayappend(data, 19, pkContent);
            ArraysUtils.arrayappend(data, data.length - 1,
                    new byte[]{PacketProcessing.checkPackage(data, 0, data.length - 1)});
        } else {
            ArraysUtils.arrayappend(data, 13, pkContent);
            ArraysUtils.arrayappend(data, data.length - 1,
                    new byte[]{PacketProcessing.checkPackage(data, 0, data.length - 1)});
        }

        byte[] bytes = PacketProcessing.escape(data, LCConstant.ESCAPE_BYTE, LCConstant.TO_ESCAPE_BYTE);
        byte[] resultBytes = new byte[bytes.length + 2];
        resultBytes[0] = LCConstant.pkBegin;
        for (int i = 1, length = bytes.length; i <= length; i++) {
            resultBytes[i] = bytes[i - 1];
        }

        //RP写给业务系统的数据包
        //NodeHelper.RPWritetoBUPackageNum++;

        if (Convert.bytesToHexString(resultBytes).contains("7E 02 30 02")) {
            //NodeHelper.RP3002Num++;
        }
        resultBytes[resultBytes.length - 1] = LCConstant.pkEnd;
        out.writeBytes(resultBytes);
        log.error("[RP->HY " + getIp(ctx) + "]: " + Convert.bytesToHexString(resultBytes).replace(" ", ""));
    }

    private String getIp(ChannelHandlerContext ctx) {
        if (ctx == null || ctx.channel().remoteAddress() == null) {
            return "";
        }
        String address = ctx.channel().remoteAddress().toString();
        String[] array = address.split(":");
        return array[0].substring(1);
    }

    public String[] getRemoteAddress(ChannelHandlerContext ctx) {
        String address = ctx.channel().remoteAddress().toString();
        String[] array = address.split(":");
        return array;
    }

}
