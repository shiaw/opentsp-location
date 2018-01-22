package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.ServerCommonResCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerCommonRes;
import org.springframework.stereotype.Component;

@KafkaConsumerHandler(topic = "rpposdone", commandId = "1100")
@Component
public class DP_1100_ServerCommonRes extends DPCommand {

    @Override
    public void handle(KafkaCommand kafkaCommand) {
        Packet packet;
        try {
            packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(), Packet.class);
            LCServerCommonRes.ServerCommonRes commonRes = LCServerCommonRes.ServerCommonRes.parseFrom(packet.getContent());
            logger.error("收到DP节点通用应答:[应答指令号:"
                    + Integer.toHexString(commonRes.getResponseId())
                    + ",应答流水号:"
                    + Integer.toHexString(commonRes.getSerialNumber())
                    + ",应答结果:" + commonRes.getResults().name() + "]");

//            if (commonRes.getResponseId() == LCAllCommands.AllCommands.NodeCluster.ReportServerIdentify_VALUE
//                    && commonRes.getResults().getNumber() == LCPlatformResponseResult.PlatformResponseResult.success_VALUE) {
//                GlobalProtocol.sendLinkStatusSwitchNotice(packet.getFrom(),
//                        true);
//            }

            if (commonRes.getResponseId() == LCAllCommands.AllCommands.Terminal.MessageBroadcastInArea_VALUE
                    || commonRes.getResponseId() == LCAllCommands.AllCommands.Terminal.MessageBroadcastInAreaDel_VALUE
                    || commonRes.getResponseId() == LCAllCommands.AllCommands.Terminal.OvertimeParkingInArea_VALUE
                    || commonRes.getResponseId() == LCAllCommands.AllCommands.Terminal.OvertimeParkingInAreaDel_VALUE
                    || commonRes.getResponseId() == LCAllCommands.AllCommands.Terminal.OutRegionToLimitSpeed_VALUE
                    || commonRes.getResponseId() == LCAllCommands.AllCommands.Terminal.OutRegionToLimitSpeedDel_VALUE) {// 区域信息播报

//				String id = "" + packet.getSerialNumber();
                String id = "" + commonRes.getSerialNumber();//dp 原始SerialNumber 在ServerCommonRes
                long to = ServerCommonResCache.getInstance().getServerCommonResCache(id).getFrom();

                Packet _out_packet = new Packet(true);
                _out_packet.setCommand(LCAllCommands.AllCommands.Platform.ServerCommonRes_VALUE);
                _out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
                _out_packet.setTo(to);
                _out_packet.setUniqueMark("");
                _out_packet.setContent(packet.getContent());

                super.writeToUpper(packet);
            }
        } catch (Exception e) {
            logger.error("1100", e);
        }
    }
}
