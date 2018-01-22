package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerCommonRes.ServerCommonRes;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCServerLogin;


/**
 * 业务系统发送“注销”口令
 *
 * @author xubh
 */
@LocationCommand(id = "0106")
public class Business_0106_LogoutReq extends RPCommand {

    public Packet processor(NettyClientConnection connection, Packet packet) {
        //String serverIP = connection.getChannel().remoteAddress().toString().split(":")[0];
        try {
            LCServerLogin.ServerLogin loginInfor = LCServerLogin.ServerLogin.parseFrom(packet.getContent());
            Packet feedBack = new Packet();
            //重登录返回值  0x1100
            feedBack.setCommand(LCAllCommands.AllCommands.Platform.ServerCommonRes_VALUE);
            feedBack.setProtocol(LCConstant.LCMessageType.PLATFORM);
            feedBack.setUniqueMark(loginInfor.getServerIdentifies() + "");
            feedBack.setTo(Long.parseLong(loginInfor.getServerIdentifies() + ""));     //设置唯一标识码
            ServerCommonRes.Builder result = ServerCommonRes.newBuilder();
            result.setSerialNumber(packet.getSerialNumber());
            result.setResults(LCPlatformResponseResult.PlatformResponseResult.success);
            result.setResponseId(LCAllCommands.AllCommands.Platform.Logout_VALUE);
            feedBack.setContent(result.build().toByteArray());
            return feedBack;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }
}
