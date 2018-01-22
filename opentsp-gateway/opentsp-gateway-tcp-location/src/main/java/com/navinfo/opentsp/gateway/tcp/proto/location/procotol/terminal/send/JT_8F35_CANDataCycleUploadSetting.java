package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.send;

import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * @author: ChenJie
 * @date 2017/11/7
 * CAN数据周期上传配置
 */
@LocationCommand(id = "8F35")
public class JT_8F35_CANDataCycleUploadSetting {

    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        PacketResult packetResult=new PacketResult();
        packetResult.setTerminalPacket(packet);
        return packetResult;
    }
}
