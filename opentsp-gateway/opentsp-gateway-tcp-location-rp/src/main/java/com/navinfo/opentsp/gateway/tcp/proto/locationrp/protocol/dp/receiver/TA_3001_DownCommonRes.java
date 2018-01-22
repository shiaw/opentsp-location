package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.UpperOplogCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPAnno;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.PushCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCDownCommonRes;
import com.navinfo.opentsp.platform.push.ResultNoticeCommand;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * 下行消息的通用应答 RP调用push服务获得下行消息应答
 */
@DPAnno(id = "3001")
public class TA_3001_DownCommonRes extends PushCommand {
    /**
     * 不删除指令寻址缓存的指令集合
     */
    static Set<Integer> commands = new HashSet<>();

    @Autowired
    private UpperOplogCache cache;

    static {
        commands.add(AllCommands.Terminal.TRVersionCollect_VALUE);
        commands.add(AllCommands.Terminal.TRSpeedCollect_VALUE);
        commands.add(AllCommands.Terminal.TRDoubtCollect_VALUE);
        commands.add(AllCommands.Terminal.TRPulseCollection_VALUE);
        commands.add(AllCommands.Terminal.TRCollectDriverInfo_VALUE);
        commands.add(AllCommands.Terminal.TRCollectRealTime_VALUE);
        commands.add(AllCommands.Terminal.TRCollectMileage_VALUE);
        commands.add(AllCommands.Terminal.TRCollectVehicleInfo_VALUE);
        commands.add(AllCommands.Terminal.TRCollectStatusSignal_VALUE);
        commands.add(AllCommands.Terminal.TRCollectOnlyCode_VALUE);
        commands.add(AllCommands.Terminal.TRCollectLocationData_VALUE);
        commands.add(AllCommands.Terminal.TRCollectOvertimeRecord_VALUE);
        commands.add(AllCommands.Terminal.TRCollectDriverIdentity_VALUE);
        commands.add(AllCommands.Terminal.TRCollectPowerRecord_VALUE);
        commands.add(AllCommands.Terminal.TRCollectParaModifyRecord_VALUE);
        commands.add(AllCommands.Terminal.TRCollectSpeedLog_VALUE);
        commands.add(AllCommands.Terminal.QueryTerminalProperty_VALUE);
        commands.add(AllCommands.Terminal.TakePhotography_VALUE);
        commands.add(AllCommands.Terminal.MediaDataQuery_VALUE);
        commands.add(AllCommands.Terminal.ParameterQuery_VALUE);
        commands.add(AllCommands.Terminal.ParameterQueryText_VALUE);
        commands.add(AllCommands.Terminal.QueryAppointPara_VALUE);
        commands.add(AllCommands.Terminal.TerminalUpgradePackage_VALUE);
        commands.add(AllCommands.Terminal.QueryTerminalProperty_VALUE);
        commands.add(AllCommands.Terminal.MediaDataQueryRes_VALUE);
        commands.add(AllCommands.Terminal.QueryAppointPara_VALUE);
        commands.add(AllCommands.Terminal.MessageBroadcastInArea_VALUE);
        commands.add(AllCommands.Terminal.MessageBroadcastInAreaDel_VALUE);
        commands.add(AllCommands.Terminal.OvertimeParkingInArea_VALUE);
        commands.add(AllCommands.Terminal.OvertimeParkingInAreaDel_VALUE);
        commands.add(AllCommands.Terminal.OutRegionToLimitSpeed_VALUE);
        commands.add(AllCommands.Terminal.OutRegionToLimitSpeedDel_VALUE);
        commands.add(AllCommands.Terminal.TerminalStatusControl_VALUE);
        commands.add(AllCommands.Terminal.DelayOvertimeParkingInArea_VALUE);
        commands.add(AllCommands.Terminal.InAreaTriggerActivationOrLockNotify_VALUE);

    }

    /*
     * 多媒体指令的应答与其它的操作指令不同,终端收到指令后,会先向平台响应一个通用应答指令,然后再依据通用应答上传对应的指令数据.
     * 而后续上传的数据将不会再带上流水号信息,导致平台无法通过指令数据获取到对应操作此终端的业务系统.此处对此指令做特殊处理,将其缓存到操作队列当中
     */

    @Override
    public void handle(ResultNoticeCommand command) {
        Packet packet = new Packet();
        packet.setContent(command.getMessage());
        packet.setUniqueMark(command.getDevice());
        packet.setCommand(command.getCommand());
        packet.setProtocol(command.getProtocol());
        packet.setSerialNumber(command.getSerialNumber());
        //kafkaCommand为消息对象，可直接获，进行对应的业务处理
        try {
            LCDownCommonRes.DownCommonRes commonRes = LCDownCommonRes.DownCommonRes.parseFrom(packet.getContent());
            String upperUniqueMark = cache.getUpper(
                    packet.getUniqueMark(), commonRes.getResponseId(),
                    commonRes.getSerialNumber(), commands.contains(commonRes.getResponseId()));
            if (upperUniqueMark == null) {
                logger.error("未找到应答的业务系统的链路.");
            }
            packet.setUpperUniqueMark(packet.getUniqueMark());
            super.writeToUpper(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
