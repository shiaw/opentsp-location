package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import java.util.List;

import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.common.NodeHelper;
import com.navinfo.opentsp.platform.da.core.persistence.RegularDataAreaAndDataManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.RegularDataAreaAndDataManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRuleDBEntity;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.*;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCQueryRegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCQueryRegularDataRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCInOrOutAreaNotifySetPara;
import org.apache.commons.collections.CollectionUtils;

import com.google.protobuf.InvalidProtocolBufferException;

@DaRmiNo(id = "0932")
public class Mutual_0932_QueryRegularData extends Dacommand {
    final static RegularDataAreaAndDataManage manager = new RegularDataAreaAndDataManageImpl();

    @Override
    public Packet processor(Packet packet) {
        try {
            // 获取消息主体
            LCQueryRegularData.QueryRegularData qrd = LCQueryRegularData.QueryRegularData
                    .parseFrom(packet.getContent());
            // 获取参数
            int districtCode = qrd.getDistrictCode().getNumber();
            long node_code = qrd.getNodeCode();
            // 从数据库取数据
            List<LcTerminalRuleDBEntity> result = manager.queryRegularData(districtCode, node_code);
            return buildPacket(packet, result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }


    /**
     * 构建packet
     *
     * @param packet
     * @param result
     * @return
     * @throws InvalidProtocolBufferException
     */
    private Packet buildPacket(Packet packet, List<LcTerminalRuleDBEntity> result) throws InvalidProtocolBufferException {
        LCQueryRegularDataRes.QueryRegularDataRes.Builder list_builder = LCQueryRegularDataRes.QueryRegularDataRes
                .newBuilder();

        list_builder.setSerialNumber(packet.getSerialNumber());
        list_builder.setResults(LCPlatformResponseResult.PlatformResponseResult.success);
        if (CollectionUtils.isNotEmpty(result)) {
            for (LcTerminalRuleDBEntity obj : result) {
                LCRegularData.RegularData.Builder obj_builder = LCRegularData.RegularData
                        .newBuilder();
                obj_builder.setTerminalId(obj.getTerminal_id());
                obj_builder.setLastModifyDate(obj.getLast_update_time());
//				obj_builder.setIsGeneral(obj.getRule_type()==0?true:false);
                obj_builder.setType(LCRegularData.RegularType.valueOf(obj.getRule_type()));
                switch (obj.getBusiness_type()) {
                    case RegularCode.speeding_VALUE:
                        obj_builder.setRegularCode(RegularCode.speeding);
                        obj_builder.setSpeeding(LCAreaSpeeding.AreaSpeeding.parseFrom(obj.getRule_content()));
                        break;
                    case RegularCode.inOutArea_VALUE:
                        obj_builder.setRegularCode(RegularCode.inOutArea);
                        obj_builder.setInOutArea(LCInOutArea.InOutArea.parseFrom(obj.getRule_content()));
                        break;
                    case RegularCode.routeDriverTime_VALUE:
                        obj_builder.setRegularCode(RegularCode.routeDriverTime);
                        obj_builder.setDriverTime(LCRouteDriverTime.RouteDriverTime.parseFrom(obj.getRule_content()));
                        break;
                    case RegularCode.driverNotCard_VALUE:
                        obj_builder.setRegularCode(RegularCode.driverNotCard);
                        obj_builder.setDriverNotCard(LCDriverNotCard.DriverNotCard.parseFrom(obj.getRule_content()));
                        break;
                    case RegularCode.doorOpenOutArea_VALUE:
                        obj_builder.setRegularCode(RegularCode.doorOpenOutArea);
                        obj_builder.setDoorOpenOutArea(LCDoorOpenOutArea.DoorOpenOutArea.parseFrom(obj.getRule_content()));
                        break;
                    case RegularCode.drivingBan_VALUE:
                        obj_builder.setRegularCode(RegularCode.drivingBan);
                        obj_builder.setDrivingBan(LCDrivingBan.DrivingBan.parseFrom(obj.getRule_content()));
                        break;
                    case RegularCode.keyPointFence_VALUE:
                        obj_builder.setRegularCode(RegularCode.keyPointFence);
                        obj_builder.setKeyPointFence(LCKeyPointFence.KeyPointFence.parseFrom(obj.getRule_content()));
                        break;
                    case RegularCode.messageBroadcast_VALUE:
                        obj_builder.setRegularCode(RegularCode.messageBroadcast);
                        obj_builder.setMessageBroadcast(LCMessageBroadcast.MessageBroadcast.parseFrom(obj.getRule_content()));
                        break;
                    case RegularCode.overtimePark_VALUE:
                        obj_builder.setRegularCode(RegularCode.overtimePark);
                        obj_builder.setOvertimePark(LCOverTimePark.OverTimePark.parseFrom(obj.getRule_content()));
                        break;
                    case RegularCode.outregionToLSpeed_VALUE:
                        obj_builder.setRegularCode(RegularCode.outregionToLSpeed);
                        obj_builder.setOutregionToLSpeed(LCOutRegionToLSpeed.OutRegionToLSpeed.parseFrom(obj.getRule_content()));
                        break;
                    case RegularCode.terminalBroadcastSwitch_VALUE://170130
                        obj_builder.setRegularCode(RegularCode.terminalBroadcastSwitch);//终端信息广播
                        obj_builder.setTerminalMessage(LCTerminalMessageSwitch.TerminalMessageSwitch.parseFrom(obj.getRule_content()));
                        break;
                    case RegularCode.delayOvertimePark_VALUE://170150
                        obj_builder.setRegularCode(RegularCode.delayOvertimePark);
                        obj_builder.setDelayPark(LCDelayOvertimePark.DelayOvertimePark.parseFrom(obj.getRule_content()));
                        break;
                    case RegularCode.vehiclePassStatistic_VALUE://175010
                        obj_builder.setRegularCode(RegularCode.vehiclePassStatistic);
                        obj_builder.setPassTimes(LCVehiclePassTimes.VehiclePassTimes.parseFrom(obj.getRule_content()));
                        break;
                    case RegularCode.inAreaTriggerActivationOrLockNotify_VALUE:
                        obj_builder.setRegularCode(RegularCode.inAreaTriggerActivationOrLockNotify);
                        obj_builder.setInAreaTriggerActivationOrLock(LCInAreaTriggerActivationOrLock.InAreaTriggerActivationOrLock.parseFrom(obj.getRule_content()));
                        break;
                    case RegularCode.inOrOutAreaNotifySwitchPara_VALUE:
                        obj_builder.setRegularCode(RegularCode.inOrOutAreaNotifySwitchPara);
                        break;
                    case RegularCode.inOrOutAreaNotifySetPara_VALUE:
                        obj_builder.setRegularCode(RegularCode.inOrOutAreaNotifySetPara);
                        obj_builder.setSetPara(LCInOrOutAreaNotifySetPara.InOrOutAreaNotifySetPara.parseFrom(obj.getRule_content()));
                        break;
                    default:
                        System.err.println("0932终端解析出错：" + obj.toString());
                }
                list_builder.addDatas(obj_builder.build());
            }
        }

        Packet _out_packet = new Packet(true);
        _out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
        _out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
        _out_packet.setCommand(LCAllCommands.AllCommands.DataAccess.QueryRegularDataRes_VALUE);
        _out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
        _out_packet.setTo(packet.getFrom());
        _out_packet.setFrom(NodeHelper.getNodeCode());
        _out_packet.setContent(list_builder.build().toByteArray());
        return _out_packet;
    }

}
