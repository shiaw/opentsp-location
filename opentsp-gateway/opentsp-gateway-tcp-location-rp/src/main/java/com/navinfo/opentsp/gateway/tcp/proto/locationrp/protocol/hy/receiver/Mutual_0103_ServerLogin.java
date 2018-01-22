package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.entry.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.common.ServiceMarkFactory;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.netty.NettyChannelMap;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.da.receiver.DA_1107_ServerStatusNoticeRes;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.da.receiver.RPRmiService;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.da.receiver.RmiConstant;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.configuration.OpentspQueues;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerStatus;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCServerLogin.ServerLogin;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCServerLoginRes.ServerLoginRes;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCServerStatusNotice.ServerStatusNotice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * RP通过（DA提供）RMI调用DA获得应答
 * 1、业务平台->RP 0103 登录请求
 * 2、RP->DA 0107服务状态通知
 * 3、DA->RP 1107 服务状态通知应答
 * 4、RP->业务平台 1103 登录应答
 */
@LocationCommand(id = "0103")
public class Mutual_0103_ServerLogin extends RPCommand {
    private static final Logger log = LoggerFactory.getLogger(Mutual_0103_ServerLogin.class);

    @Autowired
    private RPRmiService rpRmiService;

    @Autowired
    private DA_1107_ServerStatusNoticeRes da_1107_serverStatusNoticeRes;

    @Autowired
    private MessageChannel messageChannel;

    @Autowired
    private AnswerCommandCache answerCommandCache;

    private final String queueName;

    private final TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator();

    @Autowired
    public Mutual_0103_ServerLogin(@Qualifier(OpentspQueues.PERSONAL) Queue queue) {
        this.queueName = queue.getName();
    }

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        try {
            ServerLogin serverLogin = ServerLogin.parseFrom(packet.getContent());

            logger.error("收到业务系统登录请求[ " + serverLogin.getServerIdentifies() + " ]");

            //if (serverLogin.getServerIdentifies() == 88888888) {
            if (serverLogin.getServerIdentifies() >= 0) {
                //绑定平台鉴权标识与Session
                //final String id = connection.getId();
                //connection.setDevice(packet.getUniqueMark());

                //绑定平台鉴权标识与Session
                final String id = connection.getId();
                String ip = connection.getChannel().localAddress().toString();
                NettyChannelMap.userMap.put(id, id);
                connection.setDevice(id + "_" + ip.split(":")[1]);
                NettyChannelMap.put(connection);
                //ChannelMapCache.getInstance().add("",connection.getDevice());
                Packet _out_packet = new Packet(true);
                //登录验证返回值  0x1103
                _out_packet.setCommand(AllCommands.Platform.ServerLoginRes_VALUE);
                //_out_packet.setTo(serverLogin.getServerIdentifies()); //设置唯一标识码
                //_out_packet.setUniqueMark(serverLogin.getServerIdentifies() + "");
                ServerLoginRes.Builder result = ServerLoginRes.newBuilder();
                result.setSerialNumber(packet.getSerialNumber());

                if (ServiceMarkFactory.isLoginKey(serverLogin.getServerIdentifies() + "")) {
                    result.setResults(LCPlatformResponseResult.PlatformResponseResult.success);
                    //服务登录成功,设置心跳间隔(目前固定设置30秒)
                    result.setInterval(30);
                } else {
                    result.setResults(LCPlatformResponseResult.PlatformResponseResult.failure);
                }
                _out_packet.setContent(result.build().toByteArray());


                _out_packet.setUniqueMark(packet.getUniqueMark());
                _out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
                _out_packet.setTo(serverLogin.getServerIdentifies());
                _out_packet.setContent(result.build().toByteArray());
                return _out_packet;
            }
            if (connection.getChannel() != null) {
                String address = connection.getChannel().remoteAddress().toString();
                String[] ip_port = address.split(":");


                //向DA发送服务状态通知 todo RP通过（DA提供）RMI调用DA获得应答
                ServerStatusNotice.Builder builder = ServerStatusNotice.newBuilder();
                builder.setServerIdentifies(serverLogin.getServerIdentifies());
                builder.setChannelIdentify(String.valueOf(serverLogin.getServerIdentifies()));
                builder.setStatus(LCServerStatus.ServerStatus.login);
                builder.setServerIp(ip_port[0].substring(1));

                //packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
                packet.setContent(builder.build().toByteArray());
                packet.setCommand(AllCommands.Platform.ServerStatusNotice_VALUE);
                //super.writeToDataAccess(packet);
                //2、RP->DA 0107服务状态通知
                Packet packet_out = rpRmiService.callRmi(RmiConstant.RMI_SERVICE_ID, RmiConstant.RMI_INTERFACE_NAME,
                        RmiConstant.RmiInterFaceEum.DaRmiInterface.getClassType(),
                        packet);
                //3、DA->RP 1107 服务状态通知应答
                if (packet_out != null) {
                    da_1107_serverStatusNoticeRes.processor(packet_out);

                }

				/*此处认为登录是成功,进行鉴权标识与Session绑定;
                 * 在收到登录应答时候,当失败情况下,再进行解除绑定操作;
				 * 避免此处链路数据的缓存.
				 */

                //绑定平台鉴权标识与Session
                //final String id = connection.getId();
                //connection.setDevice(packet.getUniqueMark());
//                TerminalAuthCommand terminalAuthCommand = new TerminalAuthCommand();
//                terminalAuthCommand.setDeviceId(packet.getUniqueMark());
//                terminalAuthCommand.setAuth(false);
//                terminalAuthCommand.setReturnAddress(queueName);
//                messageChannel.send(terminalAuthCommand);
                AnswerEntry answerEntry = new AnswerEntry();
                answerEntry.setUniqueMark(packet.getUniqueMark());
                answerEntry.setInternalCommand(packet.getCommand());
                answerEntry.setSerialNumber(packet.getSerialNumber());
                answerCommandCache.addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

            } else {
                log.error("业务系统链路断开");
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }

}
