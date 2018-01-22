package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.common.LvsConfiguration;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.common.ServiceMarkFactory;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCMultiServerAuth.MultiServerAuth;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCMultiServerAuthRes.MultiServerAuthRes.ServerAuthInfo;
import org.springframework.beans.factory.annotation.Autowired;

import static com.navinfo.opentsp.platform.location.protocol.platform.auth.LCMultiServerAuthRes.MultiServerAuthRes;


/**
 * 业务系统发送多任务鉴权请求
 *
 * @author xubh
 */
@LocationCommand(id = "0109")
public class Business_0109_MultiServerAuth extends RPCommand {

    @Autowired
    private LvsConfiguration lvsConfiguration;

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        try {
            logger.info("业务系统请求多服务鉴权..");
            MultiServerAuth multiServerAuth = MultiServerAuth.parseFrom(packet.getContent());
            MultiServerAuthRes.Builder builder = MultiServerAuthRes.newBuilder();
            ServerAuthInfo.Builder authInfo = ServerAuthInfo.newBuilder();
            builder.setChannelIdentify(multiServerAuth.getChannelIdentify());
            builder.setResults(LCPlatformResponseResult.PlatformResponseResult.success);
            builder.setSerialNumber(packet.getSerialNumber());
            //对鉴权码进行验证
            long channelIdentify = Long.parseLong(multiServerAuth.getChannelIdentify());
            Packet feedBack = new Packet();

            builder.setChannelIdentify(multiServerAuth.getChannelIdentify());
            builder.setResults(LCPlatformResponseResult.PlatformResponseResult.success);
            builder.setSerialNumber(packet.getSerialNumber());

            authInfo.setServerIdentify(ServiceMarkFactory.getServiceMark(multiServerAuth.getChannelIdentify()));
            authInfo.setServerIp(lvsConfiguration.getNodeIp());
            builder.addInfos(authInfo.build());

            feedBack.setCommand(LCAllCommands.AllCommands.Platform.MultiServerAuthRes_VALUE);
            feedBack.setProtocol(LCConstant.LCMessageType.PLATFORM);
            feedBack.setUniqueMark(multiServerAuth.getChannelIdentify());
            feedBack.setContent(builder.build().toByteArray());
            feedBack.setTo(channelIdentify);
            return feedBack;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

}
