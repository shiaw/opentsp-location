package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCRealTimeDataCollect;

/**
 * Created by 修伟 on 2017/10/20 0020.
 * 实时数据采集汇报设置
 */
@LocationCommand(id = "2274")
public class DP_2274_RealTimeDataCollect extends DPCommand {

    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        // 缓存指令信息
        AnswerEntry answerEntry = new AnswerEntry();
        answerEntry.setInternalCommand(0x2274);// 2270
        answerEntry.setSerialNumber(packet.getSerialNumber());
        answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);// 8103
        answerEntry.setTimeout(ta_commandTimeoutThreshold);
        AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
        try {
            LCRealTimeDataCollect.RealTimeDataCollect realTimeDataCollect = LCRealTimeDataCollect.RealTimeDataCollect.parseFrom(packet.getContent());
            int pnumber = 0;
            BytesArray bytesArray = new BytesArray();
            if (realTimeDataCollect.hasUploadCycle()){
                bytesArray.append(Convert.longTobytes(0xF022, 4));
                bytesArray.append(Convert.longTobytes(2, 1));
                bytesArray.append(Convert.longTobytes(realTimeDataCollect.getUploadCycle(), 2));
                pnumber++;
            }
            if (realTimeDataCollect.hasCollectCycle()){
                bytesArray.append(Convert.longTobytes(0xF023, 4));
                bytesArray.append(Convert.longTobytes(2, 1));
                bytesArray.append(Convert.longTobytes(realTimeDataCollect.getCollectCycle(), 2));
                pnumber++;
            }
            if (realTimeDataCollect.hasCollectionTime()){
                bytesArray.append(Convert.longTobytes(0xF024, 4));
                bytesArray.append(Convert.longTobytes(2, 1));
                bytesArray.append(Convert.longTobytes(realTimeDataCollect.getCollectionTime(), 2));
                pnumber++;
            }
            BytesArray bytesArrayFin = new BytesArray();
            bytesArrayFin.append(Convert.longTobytes(pnumber, 1));
            bytesArrayFin.append(bytesArray.get());
            Packet outpacket = new Packet();
            outpacket.setCommand(Constant.JTProtocol.TerminalSetting);
            outpacket.setUniqueMark(packet.getUniqueMark());
            outpacket.setSerialNumber(packet.getSerialNumber());
            outpacket.setContent(bytesArrayFin.get());
            //return super.writeToTerminal(outpacket);
            PacketResult packetResult=new PacketResult();
            packetResult.setTerminalPacket(outpacket);
            return packetResult;
        }catch (InvalidProtocolBufferException e){
            e.printStackTrace();
        }
        return null;
    }
}
