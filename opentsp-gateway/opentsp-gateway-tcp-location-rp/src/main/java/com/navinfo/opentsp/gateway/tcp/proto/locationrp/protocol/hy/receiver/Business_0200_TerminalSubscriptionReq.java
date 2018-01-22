package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.common.LvsConfiguration;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.platform.LCRPNodeData.RPNodeData;
import com.navinfo.opentsp.platform.location.protocol.platform.LCSubscribeRequest;
import com.navinfo.opentsp.platform.location.protocol.platform.LCSubscribeRequestRes.SubscribeRequestRes;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.navinfo.opentsp.platform.location.protocol.platform.LCSubscribeRequest.*;


/**
 * 终端订阅请求,暂时忽略了未注册终端的验证
 *
 * @author xubh
 */
@LocationCommand(id = "0200")
public class Business_0200_TerminalSubscriptionReq extends RPCommand {

    @Autowired
    private LvsConfiguration lvsConfiguration;

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        logger.info("分区MM收到上层业务系统发出的终端请阅请求");
        try {
            SubscribeRequest subReq = SubscribeRequest.parseFrom(packet.getContent());
            List<Long> terminalIndities = subReq.getTerminalIdentifyList();
            //订阅的RP节点集合
            SubscribeRequestRes.Builder builder = SubscribeRequestRes.newBuilder();
            builder.setSerialNumber(packet.getSerialNumber());

            //for(long terminalId : terminalIndities){
                RPNodeData.Builder rpNodeDataBuilder = RPNodeData.newBuilder();
                rpNodeDataBuilder.setTypes(RPNodeData.ServerType.realTimeCommunication);
                rpNodeDataBuilder.setRpPort(lvsConfiguration.getRpMasterPort());
                rpNodeDataBuilder.setRpIp(lvsConfiguration.getRpMasterIp());
                rpNodeDataBuilder.addAllTerminalIdentify(terminalIndities);
                builder.addDatas(rpNodeDataBuilder);
            //}



            RPNodeData.Builder dataQueryRequest1 = RPNodeData.newBuilder();
            dataQueryRequest1.setTypes(RPNodeData.ServerType.dataQueryRequest);
            dataQueryRequest1.setRpIp(lvsConfiguration.getRpwsMasterIp());
            dataQueryRequest1.setRpPort(lvsConfiguration.getRpwsMasterPort());
            builder.addDatas(dataQueryRequest1);

            Packet response = new Packet();
            response.setCommand(LCAllCommands.AllCommands.Platform.SubscribeRequestRes_VALUE);
            response.setProtocol(LCConstant.LCMessageType.PLATFORM);
            response.setUniqueMark(packet.getUniqueMark());
            response.setContent(builder.build().toByteArray());
            response.setTo(packet.getFrom());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
