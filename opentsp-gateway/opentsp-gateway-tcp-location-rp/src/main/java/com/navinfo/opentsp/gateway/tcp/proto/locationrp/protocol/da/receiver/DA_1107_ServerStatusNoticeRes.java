package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.da.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.da.DACommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnections;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerCommonRes;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerStatus;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCServerLoginRes.ServerLoginRes;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCServerStatusNoticeRes;
import com.navinfo.opentspcore.common.gateway.ClientConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RP通过（DA提供）RMI调用DA获得应答
 * 1、业务平台->RP 0103 登录请求
 * 2、RP->DA 0107服务状态通知
 * 3、DA->RP 1107 服务状态通知应答
 * 4、RP->业务平台 1103 登录应答
 */
@Component
public class DA_1107_ServerStatusNoticeRes extends DACommand {

    @Autowired
    protected NettyClientConnections connections;

    @Autowired
    private AnswerCommandCache answerCommandCache;



    @Override
    public Packet processor(Packet packet) {
        try {
            LCServerStatusNoticeRes.ServerStatusNoticeRes noticeRes = LCServerStatusNoticeRes.ServerStatusNoticeRes.parseFrom(packet.getContent());
            switch (noticeRes.getStatus().getNumber()) {
                case LCServerStatus.ServerStatus.login_VALUE://登录：将服务状态通知应答  转换为  登录应答  向RP业务系统接入模块转发
                    this.serverStatusForLogin(noticeRes);
                    break;
                case LCServerStatus.ServerStatus.reconnect_VALUE://重连：转通用应答
                    this.serverStatusForReconnect(noticeRes);
                    break;
                default:
                    break;
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void serverStatusForLogin(LCServerStatusNoticeRes.ServerStatusNoticeRes noticeRes) {

        ServerLoginRes.Builder builder = ServerLoginRes.newBuilder();
        builder.setInterval(30);
        builder.setResults(noticeRes.getResults());
        builder.setSerialNumber(noticeRes.getSerialNumber());

        Packet _out_packet = new Packet(true);
        _out_packet.setCommand(LCAllCommands.AllCommands.Platform.ServerLoginRes_VALUE);
        _out_packet.setUniqueMark(noticeRes.getChannelIdentify());
        _out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
        _out_packet.setTo(Long.parseLong(noticeRes.getChannelIdentify()));
        _out_packet.setContent(builder.build().toByteArray());
        ClientConnection conn = connections.getConnectionByDevice(_out_packet.getUniqueMark());
        conn.send(_out_packet);
        //super.writeToUpper(_out_packet);
        System.err.println("result==" + builder.getResults().getNumber());
        //登录失败,需要解除链路绑定关系并主动断开此链路
        if (builder.getResults().getNumber() != LCPlatformResponseResult.PlatformResponseResult.success_VALUE) {
            ClientConnection connection = connections.getConnection(_out_packet.getUniqueMark());
            if (connection != null) {
                connection.close();
            }
        }
        //释放超时缓存
        answerCommandCache.remove(noticeRes.getChannelIdentify(), noticeRes.getSerialNumber());
    }

    private void serverStatusForReconnect(LCServerStatusNoticeRes.ServerStatusNoticeRes noticeRes) {
        LCServerCommonRes.ServerCommonRes.Builder builder = LCServerCommonRes.ServerCommonRes.newBuilder();
        builder.setResponseId(LCAllCommands.AllCommands.Platform.Reconnect_VALUE);
        builder.setSerialNumber(noticeRes.getSerialNumber());
        builder.setResults(noticeRes.getResults());


        Packet _out_packet = new Packet(true);
        _out_packet.setCommand(LCAllCommands.AllCommands.Platform.ServerLoginRes_VALUE);
        _out_packet.setUniqueMark(noticeRes.getChannelIdentify());
        _out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
        _out_packet.setTo(Long.parseLong(noticeRes.getChannelIdentify()));
        _out_packet.setContent(builder.build().toByteArray());
        ClientConnection conn = connections.getConnectionByDevice(_out_packet.getUniqueMark());
        conn.send(_out_packet);
        //重连失败,需要解除链路绑定关系并主动断开此链路
        if (builder.getResults().getNumber() != LCPlatformResponseResult.PlatformResponseResult.success_VALUE) {
            ClientConnection connection = connections.getConnection(_out_packet.getUniqueMark());
            if (connection != null) {
                connection.close();
            }
        }
        //释放超时缓存
        answerCommandCache.remove(noticeRes.getChannelIdentify(), noticeRes.getSerialNumber());
    }
}
