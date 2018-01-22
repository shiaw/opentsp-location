package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPAnno;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.PushCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.push.ResultNoticeCommand;

/**
 * Created by 修伟 on 2017/9/22 0022.
 */
@DPAnno(id = "0500")
public class DP_0500_VehicleControlRes extends PushCommand {

    @Override
    public void handle(ResultNoticeCommand command) {
        Packet packet = new Packet();
        packet.setContent(command.getMessage());
        packet.setUniqueMark(command.getDevice());
        packet.setCommand(command.getCommand());
        packet.setProtocol(command.getProtocol());
        packet.setSerialNumber(command.getSerialNumber());
        try {
            //SubscribeEntry subscribeEntry = TerminalManage.getInstance().getSubscribe(command.getDevice());
            //if (subscribeEntry != null) {
               /* List<ClientConnection> subscribe = subscribeEntry.getSubscribeSession();
                if (subscribe != null) {
                    for (ClientConnection clientConnection : subscribe) {
                        if (clientConnection != null) {
                            clientConnection.send(packet);
                        }
                    }
                }*/
            super.writeToUpper(packet);
                /*if (subscribe.size() == 0) {
                    logger.error(df.format(new Date()) + " :终端[" + packet.getUniqueMark() + "] : 获取到 List<MutualSession> 对象的长度为0，没有获取到MutualSession");
                }*/
            //}
        } catch (Exception e) {
            logger.error("0500", e);
        }
    }
}
