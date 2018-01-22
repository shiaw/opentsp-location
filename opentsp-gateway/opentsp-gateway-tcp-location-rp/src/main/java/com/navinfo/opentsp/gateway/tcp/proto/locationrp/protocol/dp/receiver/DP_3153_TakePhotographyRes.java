package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.MultimediaOperateQueue;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.UpperOplogCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPAnno;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.PushCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes;
import com.navinfo.opentsp.platform.push.ResultNoticeCommand;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 立即拍照/录像应答
 */
@DPAnno(id = "3153")
public class DP_3153_TakePhotographyRes extends PushCommand {

    @Autowired
    private UpperOplogCache cache;

    @Override
    public void handle(ResultNoticeCommand command) {
        // 当应答指令为拍照,并且操作成功
        // 将操作此终端的业务系统标识加入到此终端对应的操作队列
        Packet packet = new Packet();
        packet.setContent(command.getMessage());
        packet.setUniqueMark(command.getDevice());
        packet.setCommand(command.getCommand());
        packet.setProtocol(command.getProtocol());
        packet.setSerialNumber(command.getSerialNumber());
        try {
            LCTakePhotographyRes.TakePhotographyRes commonRes = LCTakePhotographyRes.TakePhotographyRes.parseFrom(packet.getContent());
            String upperUniqueMark = cache.getUpper(
                    packet.getUniqueMark(), LCAllCommands.AllCommands.Terminal.TakePhotography_VALUE,
                    commonRes.getSerialNumber(), true);
            if (commonRes.getResults().getNumber() == LCPhotoResult.PhotoResult.success1_VALUE) {
                MultimediaOperateQueue.addMultimediaOperate(
                        packet.getUniqueMark(), upperUniqueMark);
            }
            //packet.setTo(upperUniqueMark);
            packet.setUpperUniqueMark(upperUniqueMark);
            super.writeToUpper(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
