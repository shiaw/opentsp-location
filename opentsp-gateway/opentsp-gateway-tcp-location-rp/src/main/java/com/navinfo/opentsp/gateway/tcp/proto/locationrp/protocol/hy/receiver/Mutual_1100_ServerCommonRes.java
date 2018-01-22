package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerCommonRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 平台通用应答
 */
@LocationCommand(id = "1100")
public class Mutual_1100_ServerCommonRes extends RPCommand {
    private static final Logger log = LoggerFactory.getLogger(Mutual_1100_ServerCommonRes.class);

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        try {
            LCServerCommonRes.ServerCommonRes commonRes = LCServerCommonRes.ServerCommonRes.parseFrom(packet.getContent());
            log.info("收到业务系统通用应答:[应答指令号:" + Integer.toHexString(commonRes.getResponseId())
                    + ",应答流水号:" + Integer.toHexString(commonRes.getSerialNumber())
                    + ",应答结果:" + commonRes.getResults().name() + "]");
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;

    }

}
