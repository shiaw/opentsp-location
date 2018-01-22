package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.common.AddressKeyHandle;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.common.LvsConfiguration;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.common.ServiceMarkFactory;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.encrypt.MD5;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCRequestLoginKey.RequestLoginKey;
import com.navinfo.opentsp.platform.location.protocol.platform.auth.LCRequestLoginKeyRes;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 总部中心MM获取到上层业务系统的鉴权验证请求，拿到用户名，密码，分区信息。
 * <p>
 * 1. 存储与业务系统的链路信息
 * 2. 寻找到分区MM节点，转发消息
 */
@LocationCommand(id = "1101")
public class External_1101_RequestLoginKey extends RPCommand {

    @Autowired
    private LvsConfiguration lvsConfiguration;

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        logger.info("中心MM收到上层业务系统发出的鉴权验证请求. ");
        try {
            RequestLoginKey requestLogin = RequestLoginKey.parseFrom(packet.getContent());
            String[] values = new String[]{requestLogin.getName(), requestLogin.getPassword()};
            String key = AddressKeyHandle.combinationKey(values);
            key = MD5.encrypt(key, 32);

            //业务系统请求的分区对象列表
            List<LCDistrictCode.DistrictCode> districts = requestLogin.getDistrictList();

            //注意，这里没有考虑各个分区的按顺序请求。
            for (LCDistrictCode.DistrictCode district : districts) {

                //获取上层业务系统服务IP
                if (connection.getChannel().remoteAddress() == null) {
                    logger.error("获取业务系统Ip地址失败！");
                    return null;
                }
                String upLevelServicesIP = connection.getChannel().remoteAddress().toString();
                String IPstr = upLevelServicesIP.split(":")[0];
                //TODO 登陆判断
                RequestLoginKey loginResult = RequestLoginKey.parseFrom(packet.getContent());

                logger.info("分区编号: " + district.getNumber() + ", 分区名称: " + district.name() + ", IP: " + IPstr.substring(1));

                //直接组装返回对象,省去总MM,DA
                LCRequestLoginKeyRes.RequestLoginKeyRes.Builder builder = LCRequestLoginKeyRes.RequestLoginKeyRes.newBuilder();
                builder.setDistrictCode(district);
                builder.setRequestLoginSerial(packet.getSerialNumber());
                builder.setServerIdentifies(ServiceMarkFactory.getServiceMark(loginResult.getName() + "-" + requestLogin.getVersion()));
                builder.setMmMasterNodeIp(lvsConfiguration.getMmMasterIp());
                builder.setMmMasterNodePort(lvsConfiguration.getMmMasterPort());
                builder.setMmSlaverNodeIp(lvsConfiguration.getMmMasterIp());
                builder.setMmSlaverNodePort(lvsConfiguration.getMmMasterPort());
                builder.setDataSynchronizeIp(lvsConfiguration.getRpwsMasterIp());
                builder.setDataSynchronizePort(lvsConfiguration.getRpwsMasterPort());
                builder.setResults(LCPlatformResponseResult.PlatformResponseResult.valueOf(LCPlatformResponseResult.PlatformResponseResult.success_VALUE));
                builder.setChannelIdentify(key);
                logger.error("[RP-->HY(" + loginResult.getName() + "-" + requestLogin.getVersion() + ")] 返回鉴权验证 0x1102[" + builder.getServerIdentifies() + "]");
                Packet _out_packet = new Packet();
                _out_packet.setSerialNumber(packet.getSerialNumber());
                _out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
                _out_packet.setUniqueMark(Convert.fillZero(lvsConfiguration.getNodeCode(), 12));
                _out_packet.setCommand(LCAllCommands.AllCommands.Platform.RequestLoginKeyRes_VALUE);
                _out_packet.setTo(packet.getFrom());
                _out_packet.setContent(builder.build().toByteArray());
                return _out_packet;
            }
        } catch (InvalidProtocolBufferException e1) {
            e1.printStackTrace();
        }
        return null;
    }
}
