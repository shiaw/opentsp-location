package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.entry.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.common.ServiceMarkFactory;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerCommonRes.ServerCommonRes;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCReconnect.Reconnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;

/**
 * 重连
 */
@LocationCommand(id = "0105")
public class Mutual_0105_Reconnect extends RPCommand {
    private static final Logger log = LoggerFactory.getLogger(Mutual_0105_Reconnect.class);

    @Autowired
    private AnswerCommandCache answerCommandCache;

    /*
     * 重连采用与登录同样的机制
	 */

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        try {
            String paramterForString = packet.getParamterForString(P_SESSION_ID);
            if (paramterForString == null || "".equals(paramterForString)) {
                return null;
            }
            //long sessionId = Long.parseLong(paramterForString);
            if (connection.getChannel() != null) {
                String address = connection.getChannel().remoteAddress().toString();
                String[] ip_port = address.split(":");

                Reconnect reconnect = Reconnect.parseFrom(packet.getContent());
                logger.error("收到业务系统重连请求[ " + reconnect.getServerIdentifies() + " ]");

                Packet feedBack = new Packet();
                //重登录返回值  0x1100
                feedBack.setCommand(AllCommands.Platform.ServerLoginRes_VALUE);
                feedBack.setProtocol(LCConstant.LCMessageType.PLATFORM);
                feedBack.setTo(Long.parseLong(reconnect.getServerIdentifies() + "")); //设置唯一标识码
                feedBack.setUniqueMark(reconnect.getServerIdentifies() + "");
                ServerCommonRes.Builder result = ServerCommonRes.newBuilder();
                result.setSerialNumber(packet.getSerialNumber());
                result.setResponseId(AllCommands.Platform.Reconnect_VALUE);
                if (ServiceMarkFactory.isLoginKey(reconnect.getServerIdentifies() + "")) {
                    result.setResults(LCPlatformResponseResult.PlatformResponseResult.success);
                } else {
                    result.setResults(LCPlatformResponseResult.PlatformResponseResult.failure);
                }
                feedBack.setContent(result.build().toByteArray());
                //绑定平台鉴权标识与Session
                connection.setDevice(packet.getUniqueMark());
                final String id = connection.getId();
                connection.setDevice(packet.getUniqueMark());
                AnswerEntry answerEntry = new AnswerEntry();
                answerEntry.setUniqueMark(packet.getUniqueMark());
                answerEntry.setInternalCommand(packet.getCommand());
                answerEntry.setSerialNumber(packet.getSerialNumber());
                answerCommandCache.addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
                return feedBack;
            } else {
                log.error("业务系统链路断开");
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

}
