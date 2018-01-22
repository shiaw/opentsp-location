package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TerminalSnInfoCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalSnInfo;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.LCResultCode;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 修伟 on 2017/9/25 0025.
 */
@LocationCommand(id = "2035")
public class JT_2035_TerminalSnInfo extends TerminalCommand {

    @Autowired
    TerminalSnInfoCache cache;

    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        byte[] data = packet.getContent();
        String vin = Convert.bytesToHexString(ArraysUtils.subarrays(data, 0, 17));
        String ecu_id = Convert.bytesToHexString(ArraysUtils.subarrays(data, 17, 12));
        String van = Convert.bytesToHexString(ArraysUtils.subarrays(data, 29, 10));
        String sn = Convert.bytesToHexString(ArraysUtils.subarrays(data, 39, 32));
        String vehicle = Convert.bytesToHexString(ArraysUtils.subarrays(data, 71, data.length - 1));
        log.error("0x2035-->vin={},ecu_id={},van={},sn={},vehicle={}", vin, ecu_id, van, sn, vehicle);
        TerminalSnInfo info = new TerminalSnInfo();
        info.setSn(sn);
        info.setEcuid(ecu_id);
        info.setVan(van);
        info.setVehicle(vehicle);
        info.setVin(vin);
        info.setTerminalId(Long.parseLong(packet.getUniqueMark()));
        cache.add(info);

        PacketResult packetResult = new PacketResult();
        packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(), LCResultCode.JTTerminal.SUCCESS));
        return packetResult;
    }
}
