package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;


import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.StringUtils;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCParameterMessageNumber;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCParameterMessageNumber.ParameterMessageNumber;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCParameterQueryRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

@LocationCommand(id = "0104")
public class JT_0104_ParameterQueryRes extends TerminalCommand {
    static Map<String, List<Packet>> cache = new ConcurrentHashMap<String, List<Packet>>();

    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        List<Packet> packets = cache.get(packet.getUniqueMark());
        if (packets == null) {
            packets = new ArrayList<Packet>();
        }
        packets.add(packet);
        if (packets.size() < packet.getPacketTotal()) {
            cache.put(packet.getUniqueMark(), packets);
            return null;
        }
        cache.remove(packet.getUniqueMark());
        byte[] content = null;
        for (Packet packet2 : packets) {
            content = ArraysUtils.arraycopy(content, packet2.getContent());
        }
        int serialNo = (int) Convert.byte2Long(
                ArraysUtils.subarrays(content, 0, 2), 2);
        int n = (int) Convert
                .byte2Long(ArraysUtils.subarrays(content, 2, 1), 1);
        LCParameterQueryRes.ParameterQueryRes.Builder builder = LCParameterQueryRes.ParameterQueryRes.newBuilder();
        builder.setSerialNumber(serialNo);
        builder.setResult(LCResponseResult.ResponseResult.success);
        Map<Integer, Object> terminalParameterKeyMap = new HashMap<Integer, Object>();
        int idx = 3;
        for (int i = 0; i < n; i++) {
            // ParameterQueryRes.Builder settingBuilder =
            // ParameterQueryRest.newBuilder();
            if (idx + 5 > content.length) {
                break;
            }
            int jt_id = Convert.byte2Int(
                    ArraysUtils.subarrays(content, idx, 4), 4);
            Integer id = paramterMap.get(jt_id);
            int length = Convert.byte2Int(
                    ArraysUtils.subarrays(content, idx + 4, 1), 1);
            if (id == null) {
                idx = idx + 5 + length;
                System.err.println("参数ID：" + jt_id + " 找不到");
                continue;

            }
            if (reservedParameters.contains(id)) {
                //idx = idx + 5 + length;
                //continue;
            }
            // settingBuilder.setSetId(LCSettingIdentify.SettingIdentify.valueOf(id));
            if (stringParameters.contains(id)) {
                String strParam = StringUtils.bytesToGbkString(ArraysUtils.subarrays(content, idx + 5, length));
//				StringUtils.bytesToUtf8String(ArraysUtils.subarrays(content, idx + 5, length));
                terminalParameterKeyMap.put(id, strParam);

                // settingBuilder.setStrParaContent(strParam);
            } else {
                int intParam = Convert
                        .byte2Int(
                                ArraysUtils.subarrays(content, idx + 5, length),
                                length);
                if (id == 0x0080) {
                    intParam = intParam * 100;
                }
                terminalParameterKeyMap.put(id, intParam);
                // settingBuilder.setIntParaContent(intParam);
            }
            // builder.addSetting(settingBuilder.build());
            idx = idx + 5 + length;
        }
        // 消息超时处理
        LCMessageTimeoutProcess.MessageTimeoutProcess.Builder process = null;
        // 终端连接服务配置
        LCConnectServerConfig.ConnectServerConfig.Builder config = null;
//		int i=0;
        // 汇报策略和间隔
        LCReportTacticsAndInterval.ReportTacticsAndInterval.Builder interval = null;
        // 特权号码
        LCPrivilegeNumbers.PrivilegeNumbers.Builder numbers = null;
        // 报警触发设置
        LCAlarmTriggeredSetting.AlarmTriggeredSetting.Builder setting = null;
        // 超速报警
        LCSpeedingAlarm.SpeedingAlarm.Builder speeding = null;
        // 疲劳驾驶报警
        LCFatigueDriving.FatigueDriving.Builder driving = null;
        // 多媒体参数设置
        LCMultiMediaParameter.MultiMediaParameter.Builder parameter = null;
        // 车辆信息设置
        LCVehicleInfoSetting.VehicleInfoSetting.Builder vehiclesetting = null;
        // 道路运输证
        LCRoadTransportPermit.RoadTransportPermit.Builder permit = null;
        // 碰撞报警
        LCCollisionAlarm.CollisionAlarm.Builder collision = null;
        // 侧翻报警
        LCRolloverAlarm.RolloverAlarm.Builder rollover = null;
        // 定时定距拍照控制
        LCTakePictureControl.TakePictureControl.Builder picture = null;
        // GNSS设置
        LCGnssSetting.GnssSetting.Builder gnss = null;
        // CAN总线设置
        LCCanBusSetting.CanBusSetting.Builder canbus = null;
        // 超时停车报警
        LCOvertimeParking.OvertimeParking.Builder parking = null;
        for (Entry<Integer, Object> entry : terminalParameterKeyMap.entrySet()) {
            int messageNumber = entry.getKey();
            Object messageValue = entry.getValue();
            int parameterMessageNumber = paramterKeyMap.get(messageNumber);
            switch (parameterMessageNumber) {
                case ParameterMessageNumber.MessageTimeoutProcess_VALUE:
                    if (process == null) {
                        process = LCMessageTimeoutProcess.MessageTimeoutProcess.newBuilder();
                    }
                    switch (messageNumber) {
                        case 0x0001:
                            process.setHeartbeatInterval((int) messageValue);// 终端心跳发送间隔，单位秒
                            break;
                        case 0x0002:
                            process.setTcpResponseTimeout((int) messageValue);// TCP消息应答超时时间，单位秒
                            break;
                        case 0x0003:
                            process.setTcpRetransTimes((int) messageValue);// TCP消息重传次数
                            break;
                        case 0x0004:
                            process.setUdpResponseTimeout((int) messageValue);// UDP消息应答超时时间，单位秒
                            break;
                        case 0x0005:
                            process.setUdpRetransTimes((int) messageValue);// UDP消息重传次数
                            break;
                        case 0x0006:
                            process.setSmsResponseTimeout((int) messageValue);// SMS消息应答超时时间，单位秒
                            break;
                        case 0x0007:
                            process.setSmsRetransTimes((int) messageValue);// SMS消息重传次数
                            break;
                        default:
                            break;
                    }

                    break;
                case ParameterMessageNumber.ConnectServerConfig_VALUE:
                    if (config == null) {
                        config = LCConnectServerConfig.ConnectServerConfig.newBuilder();
                    }
                    switch (messageNumber) {
                        case 0x0010:
                            config.setMasterApn((String) messageValue);// masterApn 主服务器APN，无线通信拨号访问点。若网络制式为CDMA，则该处为PPP拨号号码
                            break;
                        case 0x0011:
                            config.setMasterName((String) messageValue);// masterName 主服务器无线通信拨号用户名
                            break;
                        case 0x0012:
                            config.setMasterPassword((String) messageValue);// masterPassword 主服务器无线通信拨号密码
                            break;
                        case 0x0013:
                            config.setMasterIP((String) messageValue);// masterIP 主服务器地址IP或域名
                            break;
                        case 0x0014:
                            config.setBackupApn((String) messageValue);// backupApn 备用服务器APN，无线通信拨号访问点
                            break;
                        case 0x0015:
                            config.setBackupName((String) messageValue);// backupName 备用服务器无线通信拨号用户名
                            break;
                        case 0x0016:
                            config.setBackupPasswd((String) messageValue);// backupPasswd 备用服务器无线通信拨号密码
                            break;
                        case 0x0017:
                            config.setBackupIP((String) messageValue);// backupIP 备用服务器地址IP或域名
                            break;
                        case 0x0018:
                            config.setTcpPort((int) messageValue);// tcpPort 服务器TCP端口
                            break;
                        case 0x0019:
                            config.setUdpPort((int) messageValue);// udpPort 服务器UDP端口
                            break;
                        default:
                            break;
                    }
                    break;
                case ParameterMessageNumber.ReportTacticsAndInterval_VALUE:
                    if (interval == null) {
                        interval = LCReportTacticsAndInterval.ReportTacticsAndInterval.newBuilder();
                    }
                    switch ((int) messageNumber) {
                        case 0x0020:
                            switch ((int) messageValue) {
                                case LCReportTacticsAndInterval.ReportTacticsAndInterval.ReportTactics.timing_VALUE:
                                    interval.setTactics(LCReportTacticsAndInterval.ReportTacticsAndInterval.ReportTactics.timing);
                                    break;
                                case LCReportTacticsAndInterval.ReportTacticsAndInterval.ReportTactics.distance_VALUE:
                                    interval.setTactics(LCReportTacticsAndInterval.ReportTacticsAndInterval.ReportTactics.distance);
                                    break;
                                case LCReportTacticsAndInterval.ReportTacticsAndInterval.ReportTactics.timingAndDistance_VALUE:
                                    interval.setTactics(LCReportTacticsAndInterval.ReportTacticsAndInterval.ReportTactics.timingAndDistance);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 0x0021:
                            switch ((int) messageValue) {
                                case LCReportTacticsAndInterval.ReportTacticsAndInterval.ReportProgram.accStatus_VALUE:
                                    interval.setProgram(LCReportTacticsAndInterval.ReportTacticsAndInterval.ReportProgram.accStatus);
                                    break;
                                case LCReportTacticsAndInterval.ReportTacticsAndInterval.ReportProgram.loginAndAcc_VALUE:
                                    interval.setProgram(LCReportTacticsAndInterval.ReportTacticsAndInterval.ReportProgram.loginAndAcc);
                                    break;
                                default:
                                    break;
                            }
                            // program  汇报方案枚举(ReportProgram)parameterKeyMap.get(keyMap.get("program"))
                            break;
                        case 0x0022:
                            interval.setNotLoginTime((int) messageValue);// notLoginTime 驾驶员未登录汇报间隔，单位秒
                            break;
                        case 0x0027:
                            interval.setSleepingTime((int) messageValue);// sleepingTime 休眠时汇报时间间隔，单位秒
                            break;
                        case 0x0028:
                            interval.setUrgentTime((int) messageValue);// urgentTime 紧急报警汇报间隔，单位秒
                            break;
                        case 0x0029:
                            interval.setDefaultTime((int) messageValue);// defaultTime 缺省时间汇报间隔，单位秒
                            break;
                        case 0x002C:
                            interval.setDefaultDistance((int) messageValue);// defaultDistance缺省距离汇报间隔，单位米
                            break;
                        case 0x002D:
                            interval.setNotLoginDistance((int) messageValue);// notLoginDistance 驾驶员未登录汇报距离间隔，单位米
                            break;
                        case 0x002E:
                            interval.setSleepingDistance((int) messageValue);// sleepingDistance 休眠时汇报距离间隔，单位米
                            break;
                        case 0x002F:
                            interval.setUrgentDistance((int) messageValue);// urgentDistance 紧急报警时汇报距离间隔，单位米
                            break;
                        case 0x0030:
                            interval.setInflectionAngle((int) messageValue);// inflectionAngle拐点补传角度(<180)
                            break;
                        case 0x0031:
                            interval.setFenceRadius((int) messageValue);// fenceRadius 电子围栏半径（非法位移阀值），单位米
                            break;
                        default:
                            break;
                    }
                    break;
                case ParameterMessageNumber.PrivilegeNumbers_VALUE:
                    if (numbers == null) {
                        numbers = LCPrivilegeNumbers.PrivilegeNumbers.newBuilder();
                    }
                    switch (messageNumber) {
                        case 0x0040:
                            numbers.setPlatformPhone((String) messageValue);// platformPhone 监控平台电话号码
                            break;
                        case 0x0041:
                            numbers.setResetPhone((String) messageValue);// resetPhone复位电话号码，可采用此电话号码拨打终端电话让终端复位
                            break;
                        case 0x0042:
                            numbers.setFactoryResetPhone((String) messageValue);// factoryResetPhone 恢复出厂设置电话号码，可采用此电话号码拨打终端电话让终端恢复出厂设置
                            break;
                        case 0x0043:
                            numbers.setPlatformsmsPhone((String) messageValue);// PlatformsmsPhone 监控平台SMS电话号码
                            break;
                        case 0x0044:
                            numbers.setSmsAlarmPhone((String) messageValue);// smsAlarmPhone 接收终端SMS文本报警号码
                            break;
                        case 0x0045:
                            switch ((int) messageValue) {
                                case LCPrivilegeNumbers.PrivilegeNumbers.AnswerTactics.AnswerByAcc_VALUE:
                                    numbers.setTactics(LCPrivilegeNumbers.PrivilegeNumbers.AnswerTactics.AnswerByAcc);
                                    break;
                                case LCPrivilegeNumbers.PrivilegeNumbers.AnswerTactics.autoAnswer_VALUE:
                                    numbers.setTactics(LCPrivilegeNumbers.PrivilegeNumbers.AnswerTactics.autoAnswer);
                                    break;
                                default:
                                    break;
                            }
                            // tactics 终端电话接听策略(AnswerTactics)parameterKeyMap.get(keyMap.get("tactics"))
                            break;
                        case 0x0046:
                            numbers.setEveryTalking((int) messageValue);// everyTalking 每次最长通话时间，单位秒
                            break;
                        case 0x0047:
                            numbers.setMonthTalking((int) messageValue);// monthTalking 每月最长通话时间，单位秒
                            break;
                        case 0x0048:
                            numbers.setListenerPhone((String) messageValue);// listenerPhone 监听电话号码
                            break;
                        case 0x0049:
                            numbers.setPrivilegeSmsPhone((String) messageValue);// privilegeSmsPhone 监管平台特权短信号码
                            break;
                        default:
                            break;
                    }
                    break;

                case ParameterMessageNumber.AlarmTriggeredSetting_VALUE:
                    if (setting == null) {
                        setting = LCAlarmTriggeredSetting.AlarmTriggeredSetting.newBuilder();
                    }
                    switch (messageNumber) {
                        case 0x0050:
                            setting.setAlarmMask((int) messageValue);// alarmMask 报警屏蔽，与报警枚举相对应，相应值为1则表示该位报警屏蔽
                            break;
                        case 0x0051:
                            setting.setAlarmSmsSwitch((int) messageValue);// alarmSmsSwitch 报警发送文本SMS开关，位对应关系同上，1为发送文本SMS
                            break;
                        case 0x0052:
                            setting.setAlarmPhotoSwitch((int) messageValue);// alarmPhotoSwitch报警拍摄开关，位对应关系同上，1为报警时拍照
                            break;
                        case 0x0053:
                            setting.setAlarmPhotoSave((int) messageValue);// alarmPhotoSave 报警拍摄存储标志，为对应关系同上，1为存储在终端，否则实时上传
                            break;
                        case 0x0054:
                            setting.setAlarmKeyStatus((int) messageValue);// alarmKeyStatus 关键标志，位对应关系同上，1为对应报警为关键报警
                            break;
                        default:
                            break;
                    }
                    break;
                case ParameterMessageNumber.SpeedingAlarm_VALUE:
                    if (speeding == null) {
                        speeding = LCSpeedingAlarm.SpeedingAlarm.newBuilder();
                    }
                    switch (messageNumber) {
                        case 0x0055:
                            speeding.setMaxSpeed((int) messageValue);// maxSpeed 最高速度，单位千米/小时
                            break;
                        case 0x0056:
                            speeding.setContinueTime((int) messageValue);// continueTime 超速持续时间，单位秒
                            break;
                        case 0x005B:
                            speeding.setWarningSpeed((int) messageValue);// warningSpeed 超速报警预警差值，单位千米/小时
                            break;
                        default:
                            break;
                    }
                    break;
                case ParameterMessageNumber.FatigueDriving_VALUE:
                    if (driving == null) {
                        driving = LCFatigueDriving.FatigueDriving.newBuilder();
                    }
                    switch (messageNumber) {
                        case 0x0057:
                            driving.setContinueDrivingTime((int) messageValue);// continueDrivingTime 连续驾驶时间门限，单位秒
                            break;
                        case 0x0058:
                            driving.setDayCumulativeDrivingTime((int) messageValue);// dayCumulativeDrivingTime 当天累计驾驶时间门限，单位秒
                            break;
                        case 0x0059:
                            driving.setMinRestingTime((int) messageValue);// minRestingTime 最小休息时间，单位秒
                            break;
                        case 0x005C:
                            driving.setWarningFatigue((int) messageValue);// warningFatigue 疲劳驾驶预警差值，单位千米/小时
                            break;
                        default:
                            break;
                    }
                    break;
                case ParameterMessageNumber.MultiMediaParameter_VALUE:
                    if (parameter == null) {
                        parameter = LCMultiMediaParameter.MultiMediaParameter.newBuilder();
                    }
                    switch (messageNumber) {
                        case 0x0070:
                            parameter.setPictureQuality((int) messageValue);// pictureQuality 图像/视频质量，1~10,1最好
                            break;
                        case 0x0071:
                            parameter.setBrightness((int) messageValue);// brightness 亮度，0~255
                            break;
                        case 0x0072:
                            parameter.setContrast((int) messageValue);// contrast 对比度，0~127
                            break;
                        case 0x0073:
                            parameter.setSaturation((int) messageValue);// saturation 饱和度，0~127
                            break;
                        case 0x0074:
                            parameter.setChroma((int) messageValue);// chroma 色度，0~255
                            break;
                        default:
                            break;
                    }
                    break;

                case ParameterMessageNumber.VehicleInfoSetting_VALUE:
                    if (vehiclesetting == null) {
                        vehiclesetting = LCVehicleInfoSetting.VehicleInfoSetting.newBuilder();
                    }
                    switch (messageNumber) {
                        case 0x0080:
                            vehiclesetting.setMileageValues((int) messageValue);// mileageValues 车辆里程表读数，单位米
                            break;
                        case 0x0081:
                            vehiclesetting.setProvinceIdentify((int) messageValue);// provinceIdentify 车辆所在的省域ID
                            break;
                        case 0x0082:
                            vehiclesetting.setCityIdentify((int) messageValue);// cityIdentify 车辆所在的市域ID
                            break;
                        case 0x0083:
                            vehiclesetting.setVehicleLicense((String) messageValue);// vehicleLicense 公安交通管理部门颁发的机动车号牌
                            break;
                        case 0x0084:
                            vehiclesetting.setLicenseColor((int) messageValue);// licenseColor 车牌颜色按照JT/T415-2006中5.4.12的规定
                            break;
                        default:
                            break;
                    }
                    break;
                case ParameterMessageNumber.RoadTransportPermit_VALUE:
                    if (permit == null) {
                        permit = LCRoadTransportPermit.RoadTransportPermit.newBuilder();
                    }
                    switch (messageNumber) {
                        case 0x001A:
                            permit.setMainServerIp(String.valueOf(messageValue));// mainServerIp IC卡认证主服务器IP或域名
                            break;
                        case 0x001B:
                            permit.setTcpPort((int) messageValue);// tcpPort IC卡认证主服务器TCP端口
                            break;
                        case 0x001C:
                            permit.setUdpPort((int) messageValue);// udpPort IC卡认证主服务器UDP端口
                            break;
                        case 0x001D:
                            permit.setBackupServerIp(String.valueOf(messageValue));// backupServerIp IC卡认证备份服务器IP或域名
                            break;
                        default:
                            break;
                    }
                    break;
                case ParameterMessageNumber.CollisionAlarm_VALUE:
                    if (collision == null) {
                        collision = LCCollisionAlarm.CollisionAlarm.newBuilder();
                    }
                    switch (messageNumber) {
                        case 0x005D:

                            collision.setCollisionTime((((int) messageValue) & 255) * 4);// collisionTime 碰撞时间，单位毫秒(ms) **b7-b0： 碰撞时间，单位4ms；
                            int acceleration = ((int) messageValue & 65280) >> 8;
                            float accelerationValue = (float) (acceleration / 10.0);
                            collision.setAcceleration(accelerationValue);// acceleration 碰撞加速度，单位g  **b7-b0： 碰撞时间，单位4ms；
                            System.err.println("collision--->:" + collision.build());
                            break;
                        default:
                            break;
                    }
                    break;
                case ParameterMessageNumber.RolloverAlarm_VALUE:
                    if (rollover == null) {
                        rollover = LCRolloverAlarm.RolloverAlarm.newBuilder();
                    }
                    switch (messageNumber) {
                        case 0x005E:
                            rollover.setRolloverAngle((int) messageValue);// rolloverAngle 侧翻角度

                        default:
                            break;
                    }
                    break;
                case ParameterMessageNumber.TakePictureControl_VALUE:
                    if (picture == null) {
                        picture = LCTakePictureControl.TakePictureControl.newBuilder();
                    }
                    switch (messageNumber) {
                        case 0x00000:
                            picture.addPassageStatus(LCTakePictureControl.PassageStatus.newBuilder());// passageStatus 通道状态(PassageStatus) parameterKeyMap.get(keyMap.get("passageStatus"))
                            break;
                        case 0x00001:
                            picture.setStatus(true);// status true：定时；false定距 (boolean) parameterKeyMap.get(keyMap.get("status"))
                            break;
                        case 0x00002:
                            picture.setTimingInterval((int) messageValue);// timingInterval 定时定距间隔数值
                            break;
                        case 0x00003:
                            picture.setUnit(LCTakePictureControl.IntervalUnit.timing_minute);// unit 定时定距间隔单位 (IntervalUnit) parameterKeyMap.get(keyMap.get("unit"))
                            break;
                        default:
                            break;
                    }
                    break;
                case ParameterMessageNumber.GnssSetting_VALUE:
                    if (gnss == null) {
                        gnss = LCGnssSetting.GnssSetting.newBuilder();
                    }
                    switch (messageNumber) {
                        case 0x0090:// gnss.setLocationMode( (LocationMode) parameterKeyMap.get(keyMap.get("locationMode")));locationMode 定位模式
                            break;
                        case 0x0091:// gnss.setBaudRate( (BaudRate) parameterKeyMap.get(keyMap.get("baudRate")));// baudRate 波特率
                            break;
                        case 0x0092:    // gnss.setLocationRate( (LocationRate) parameterKeyMap.get(keyMap.get("locationRate"))); locationRate 定位数据输出频率
                            break;
                        case 0x0093:
                            gnss.setCollectRate((int) messageValue);// collectRate 定位数据采集频率，单位秒
                            break;
                        case 0x0095://gnss.addUploadSetting( (UpLoadSetting) parameterKeyMap.get(keyMap.get("uploadSetting"))); uploadSetting 定位数据上传设置
                            break;
                        default:
                            break;
                    }
                    break;
                case ParameterMessageNumber.CanBusSetting_VALUE:
                    if (canbus == null) {
                        canbus = LCCanBusSetting.CanBusSetting.newBuilder();
                    }
                    switch (messageNumber) {
                        case 0x0100:    // canbus.addSettings( (ControlSetting) parameterKeyMap.get(keyMap.get("settings")));// settings CAN总线控制设置
                            break;
                        case 0x0110:// canbus.addCollects( (SingleCollect) parameterKeyMap.get(keyMap.get("collects")));// collects CAN总线ID单独采集设置
                            break;

                        default:
                            break;
                    }
                    break;
                case ParameterMessageNumber.OvertimeParking_VALUE:
                    if (parking == null) {
                        parking = LCOvertimeParking.OvertimeParking.newBuilder();
                    }
                    switch (messageNumber) {
                        case 0x005A:
                            parking.setParkingLimit((int) messageValue);// parkingLimit 超时停车阀值
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
        if (process != null)
            // 消息超时处理
        {
            builder.setProcess(process);
        }

        // 终端连接服务配置
        if (config != null) {
            builder.setConfig(config);
        }
        // 汇报策略和间隔
        if (interval != null) {
            builder.setInterval(interval);
        }
        // 特权号码
        if (numbers != null) {
            builder.setNumbers(numbers);
        }
        // 报警触发设置
        if (setting != null) {
            builder.setSetting(setting);
        }
        // 超速报警
        if (speeding != null) {
            builder.setSpeeding(speeding);
        }
        // 疲劳驾驶报警
        if (driving != null) {
            builder.setDriving(driving);
        }
        // 多媒体参数设置
        if (parameter != null) {
            builder.setParameter(parameter);
        }
        // 车辆信息设置
        if (vehiclesetting != null) {
            builder.setVehiclesetting(vehiclesetting);
        }
        // 道路运输证
        if (permit != null) {
            builder.setPermit(permit);
        }
        // 碰撞报警
        if (collision != null) {
            builder.setCollision(collision);
        }
        // 侧翻报警
        if (rollover != null) {
            builder.setRollover(rollover);
        }
        // 定时定距拍照控制
        if (picture != null) {
            builder.setPicture(picture);
        }
        // GNSS设置
        if (gnss != null)
            // builder.setGnss(gnss);
            // CAN总线设置
        {
            if (canbus != null)
            // builder.setCanbus(canbus);
            // 超时停车报警
            {
                if (parking != null) {
                    builder.setParking(parking);
                }
            }
        }
        System.err.println("ParameterQueryResVALUE--->:" + builder.build());
        byte[] bytes = builder.build().toByteArray();
        Packet _out_packet = new Packet();
        _out_packet.setCommand(LCAllCommands.AllCommands.Terminal.ParameterQueryRes_VALUE);
        _out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
        _out_packet.setUniqueMark(packet.getUniqueMark());
        _out_packet.setSerialNumber(packet.getSerialNumber());
        _out_packet.setContent(bytes);
        //	super.writeToDataProcessing(_out_packet);
        PacketResult packetResult=new PacketResult();
        packetResult.setTerminalPacket(_out_packet);
        packetResult.setKafkaPacket(_out_packet);
        return packetResult;
        //return 0;
    }

    // 保留参数ID
    private static List<Integer> reservedParameters = new ArrayList<Integer>();
    // 值为字符串参数ID
    private static List<Integer> stringParameters = new ArrayList<Integer>();
    // 部标参数ID->内部参数ID
    private static Map<Integer, Integer> paramterMap = new HashMap<Integer, Integer>();

    // 部标参数ID->内部参数ID
    public static Map<Integer, Map<String, Integer>> paramterInternlKeyMap = new HashMap<Integer, Map<String, Integer>>();
    // 部标参数ID->内部参数ID
    public static Map<Integer, Integer> paramterKeyMap = new HashMap<Integer, Integer>();

    static {
        paramterKeyMap.put(0x0001, ParameterMessageNumber.MessageTimeoutProcess_VALUE);// 终端心跳发送间隔，单位秒
        paramterKeyMap.put(0x0002, ParameterMessageNumber.MessageTimeoutProcess_VALUE);// TCP消息应答超时时间，单位秒
        paramterKeyMap.put(0x0003, ParameterMessageNumber.MessageTimeoutProcess_VALUE);// TCP消息重传次数
        paramterKeyMap.put(0x0004, ParameterMessageNumber.MessageTimeoutProcess_VALUE);// UDP消息应答超时时间，单位秒
        paramterKeyMap.put(0x0005, ParameterMessageNumber.MessageTimeoutProcess_VALUE);// UDP消息重传次数
        paramterKeyMap.put(0x0006, ParameterMessageNumber.MessageTimeoutProcess_VALUE);// SMS消息应答超时时间，单位秒
        paramterKeyMap.put(0x0007, ParameterMessageNumber.MessageTimeoutProcess_VALUE);// SMS消息重传次数

        paramterKeyMap.put(0x0010, ParameterMessageNumber.ConnectServerConfig_VALUE);// masterApn 主服务器APN，无线通信拨号访问点。若网络制式为CDMA，则该处为PPP拨号号码
        paramterKeyMap.put(0x0011, ParameterMessageNumber.ConnectServerConfig_VALUE);// masterName 主服务器无线通信拨号用户名
        paramterKeyMap.put(0x0012, ParameterMessageNumber.ConnectServerConfig_VALUE);// masterPassword主服务器无线通信拨号密码
        paramterKeyMap.put(0x0013, ParameterMessageNumber.ConnectServerConfig_VALUE);// masterIP 主服务器地址IP或域名
        paramterKeyMap.put(0x0014, ParameterMessageNumber.ConnectServerConfig_VALUE);// backupApn 备用服务器APN，无线通信拨号访问点
        paramterKeyMap.put(0x0015, ParameterMessageNumber.ConnectServerConfig_VALUE);// backupName备用服务器无线通信拨号用户名
        paramterKeyMap.put(0x0016, ParameterMessageNumber.ConnectServerConfig_VALUE);// backupPasswd 备用服务器无线通信拨号密码
        paramterKeyMap.put(0x0017, ParameterMessageNumber.ConnectServerConfig_VALUE);// backupIP 备用服务器地址IP或域名
        paramterKeyMap.put(0x0018, ParameterMessageNumber.ConnectServerConfig_VALUE);// tcpPort 服务器TCP端口
        paramterKeyMap.put(0x0019, ParameterMessageNumber.ConnectServerConfig_VALUE);// udpPort 服务器UDP端口

        paramterKeyMap.put(0x0020, ParameterMessageNumber.ReportTacticsAndInterval_VALUE);// tactics 汇报策略枚举
        paramterKeyMap.put(0x0021, ParameterMessageNumber.ReportTacticsAndInterval_VALUE);// program 汇报方案枚举
        paramterKeyMap.put(0x0022, ParameterMessageNumber.ReportTacticsAndInterval_VALUE);// notLoginTime驾驶员未登录汇报间隔，单位秒
        paramterKeyMap.put(0x0027, ParameterMessageNumber.ReportTacticsAndInterval_VALUE);// sleepingTime休眠时汇报时间间隔，单位秒
        paramterKeyMap.put(0x0028, ParameterMessageNumber.ReportTacticsAndInterval_VALUE);// urgentTime紧急报警汇报间隔，单位秒
        paramterKeyMap.put(0x0029, ParameterMessageNumber.ReportTacticsAndInterval_VALUE);// defaultTime 缺省时间汇报间隔，单位秒
        paramterKeyMap.put(0x002C, ParameterMessageNumber.ReportTacticsAndInterval_VALUE);// defaultDistance 缺省距离汇报间隔，单位米
        paramterKeyMap.put(0x002D, ParameterMessageNumber.ReportTacticsAndInterval_VALUE);// notLoginDistance 驾驶员未登录汇报距离间隔，单位米
        paramterKeyMap.put(0x002E, ParameterMessageNumber.ReportTacticsAndInterval_VALUE);// sleepingDistance 休眠时汇报距离间隔，单位米
        paramterKeyMap.put(0x002F, ParameterMessageNumber.ReportTacticsAndInterval_VALUE);// urgentDistance 紧急报警时汇报距离间隔，单位米
        paramterKeyMap.put(0x0030, ParameterMessageNumber.ReportTacticsAndInterval_VALUE);// inflectionAngle 拐点补传角度(<180)
        paramterKeyMap.put(0x0031, ParameterMessageNumber.ReportTacticsAndInterval_VALUE);// fenceRadius// 电子围栏半径（非法位移阀值），单位米

        paramterKeyMap.put(0x0040, ParameterMessageNumber.PrivilegeNumbers_VALUE);// platformPhone 监控平台电话号码
        paramterKeyMap.put(0x0041, ParameterMessageNumber.PrivilegeNumbers_VALUE);// resetPhone 复位电话号码，可采用此电话号码拨打终端电话让终端复位
        paramterKeyMap.put(0x0042, ParameterMessageNumber.PrivilegeNumbers_VALUE);// factoryResetPhone恢复出厂设置电话号码，可采用此电话号码拨打终端电话让终端恢复出厂设置
        paramterKeyMap.put(0x0043, ParameterMessageNumber.PrivilegeNumbers_VALUE);// PlatformsmsPhone 监控平台SMS电话号码
        paramterKeyMap.put(0x0044, ParameterMessageNumber.PrivilegeNumbers_VALUE);// smsAlarmPhone 接收终端SMS文本报警号码
        paramterKeyMap.put(0x0045, ParameterMessageNumber.PrivilegeNumbers_VALUE);// tactics 终端电话接听策略
        paramterKeyMap.put(0x0046, ParameterMessageNumber.PrivilegeNumbers_VALUE);// everyTalking 每次最长通话时间，单位秒
        paramterKeyMap.put(0x0047, ParameterMessageNumber.PrivilegeNumbers_VALUE);// monthTalking 每月最长通话时间，单位秒
        paramterKeyMap.put(0x0048, ParameterMessageNumber.PrivilegeNumbers_VALUE);// listenerPhone 监听电话号码
        paramterKeyMap.put(0x0048, ParameterMessageNumber.PrivilegeNumbers_VALUE);// privilegeSmsPhone 监管平台特权短信号码
        paramterKeyMap.put(0x0050, ParameterMessageNumber.AlarmTriggeredSetting_VALUE);// alarmMask报警屏蔽，与报警枚举相对应，相应值为1则表示该位报警屏蔽
        paramterKeyMap.put(0x0051, ParameterMessageNumber.AlarmTriggeredSetting_VALUE);// alarmSmsSwitch报警发送文本SMS开关，位对应关系同上，1为发送文本SMS
        paramterKeyMap.put(0x0051, ParameterMessageNumber.AlarmTriggeredSetting_VALUE);// alarmPhotoSwitch报警拍摄开关，位对应关系同上，1为报警时拍照
        paramterKeyMap.put(0x0053, ParameterMessageNumber.AlarmTriggeredSetting_VALUE);// alarmPhotoSave报警拍摄存储标志，为对应关系同上，1为存储在终端，否则实时上传
        paramterKeyMap.put(0x0054, ParameterMessageNumber.AlarmTriggeredSetting_VALUE);// alarmKeyStatus关键标志，位对应关系同上，1为对应报警为关键报警

        paramterKeyMap.put(0x0055, ParameterMessageNumber.SpeedingAlarm_VALUE);// maxSpeed 最高速度，单位千米/小时
        paramterKeyMap.put(0x0056, ParameterMessageNumber.SpeedingAlarm_VALUE);// continueTime 超速持续时间，单位秒
        paramterKeyMap.put(0x005B, ParameterMessageNumber.SpeedingAlarm_VALUE);// warningSpeed
        paramterKeyMap.put(0x0057, ParameterMessageNumber.FatigueDriving_VALUE);// continueDrivingTime连续驾驶时间门限，单位秒
        paramterKeyMap.put(0x0058, ParameterMessageNumber.FatigueDriving_VALUE);// dayCumulativeDrivingTime当天累计驾驶时间门限，单位秒
        paramterKeyMap.put(0x0059, ParameterMessageNumber.FatigueDriving_VALUE);// minRestingTime最小休息时间，单位秒
        paramterKeyMap.put(0x005C, ParameterMessageNumber.FatigueDriving_VALUE);// warningFatigue 疲劳驾驶预警差值，单位千米/小时

        paramterKeyMap.put(0x0070, ParameterMessageNumber.MultiMediaParameter_VALUE);// pictureQuality图像/视频质量，1~10,1最好
        paramterKeyMap.put(0x0070, ParameterMessageNumber.MultiMediaParameter_VALUE);// brightness 亮度，0~255
        paramterKeyMap.put(0x0072, ParameterMessageNumber.MultiMediaParameter_VALUE);// contrast 对比度，0~127
        paramterKeyMap.put(0x0072, ParameterMessageNumber.MultiMediaParameter_VALUE);// saturation 饱和度，0~127
        paramterKeyMap.put(0x0074, ParameterMessageNumber.MultiMediaParameter_VALUE);// chroma 色度，0~255

        paramterKeyMap.put(0x0080, ParameterMessageNumber.VehicleInfoSetting_VALUE);// mileageValues 车辆里程表读数，单位米
        paramterKeyMap.put(0x0081, ParameterMessageNumber.VehicleInfoSetting_VALUE);// provinceIdentify 车辆所在的省域ID
        paramterKeyMap.put(0x0082, ParameterMessageNumber.VehicleInfoSetting_VALUE);// cityIdentify 车辆所在的市域ID
        paramterKeyMap.put(0x0083, ParameterMessageNumber.VehicleInfoSetting_VALUE);// vehicleLicense 公安交通管理部门颁发的机动车号牌
        paramterKeyMap.put(0x0084, ParameterMessageNumber.VehicleInfoSetting_VALUE);// licenseColor车牌颜色按照JT/T415-2006中5.4.12的规定
        paramterKeyMap.put(0x001A, ParameterMessageNumber.RoadTransportPermit_VALUE);// mainServerIpIC卡认证主服务器IP或域名
        paramterKeyMap.put(0x001B, ParameterMessageNumber.RoadTransportPermit_VALUE);// tcpPort IC卡认证主服务器TCP端口
        paramterKeyMap.put(0x001C, ParameterMessageNumber.RoadTransportPermit_VALUE);// udpPort IC卡认证主服务器UDP端口
        paramterKeyMap.put(0x001D, ParameterMessageNumber.RoadTransportPermit_VALUE);// backupServerIpIC卡认证备份服务器IP或域名

        paramterKeyMap.put(0x005D, ParameterMessageNumber.CollisionAlarm_VALUE);// collisionTime碰撞时间，单位毫秒(ms) **b7-b0： 碰撞时间，单位4ms；
        //paramterKeyMap.put( 0x0084,ParameterMessageNumber.CollisionAlarm_VALUE);// acceleration 碰撞加速度， **b7-b0： 碰撞时间，单位4ms；

        paramterKeyMap.put(0x005E, ParameterMessageNumber.RolloverAlarm_VALUE);// rolloverAngle 侧翻角度

        paramterKeyMap.put(0, ParameterMessageNumber.TakePictureControl_VALUE);// passageStatus 通道状态
        paramterKeyMap.put(0, ParameterMessageNumber.TakePictureControl_VALUE);// status true：定时；false定距
        paramterKeyMap.put(0, ParameterMessageNumber.TakePictureControl_VALUE);// timingInterval 定时定距间隔数值
        paramterKeyMap.put(0, ParameterMessageNumber.TakePictureControl_VALUE);// unit 定时定距间隔单位

        paramterKeyMap.put(0x0090, ParameterMessageNumber.GnssSetting_VALUE);// locationMode 定位模式
        paramterKeyMap.put(0x0091, ParameterMessageNumber.GnssSetting_VALUE);// baudRate 波特率
        paramterKeyMap.put(0x0092, ParameterMessageNumber.GnssSetting_VALUE);// locationRate 定位数据输出频率
        paramterKeyMap.put(0x0093, ParameterMessageNumber.GnssSetting_VALUE);// collectRate 定位数据采集频率，单位秒
        paramterKeyMap.put(0x0095, ParameterMessageNumber.GnssSetting_VALUE);// uploadSetting 定位数据上传设置

        paramterKeyMap.put(0x0110, ParameterMessageNumber.CanBusSetting_VALUE);// settings CAN总线控制设置
        paramterKeyMap.put(0x0110, ParameterMessageNumber.CanBusSetting_VALUE);// collects CAN总线ID单独采集设置

        paramterKeyMap.put(0x005A, ParameterMessageNumber.OvertimeParking_VALUE);// parkingLimit 超时停车阀值
        /*****/

        Map<String, Integer> messageTimeoutProcess = new HashMap<String, Integer>();
        messageTimeoutProcess.put("heartbeatInterval", 0x0001);// 终端心跳发送间隔，单位秒
        messageTimeoutProcess.put("tcpResponseTimeout", 0x0002);// TCP消息应答超时时间，单位秒
        messageTimeoutProcess.put("tcpRetransTimes", 0x0003);// TCP消息重传次数
        messageTimeoutProcess.put("udpResponseTimeout", 0x0003);// UDP消息应答超时时间，单位秒
        messageTimeoutProcess.put("udpRetransTimes", 0x0005);// UDP消息重传次数
        messageTimeoutProcess.put("smsResponseTimeout", 0x0006);// SMS消息应答超时时间，单位秒
        messageTimeoutProcess.put("smsRetransTimes", 0x0007);// SMS消息重传次数

        Map<String, Integer> connectServerConfig = new HashMap<String, Integer>();
        connectServerConfig.put("masterApn", 0x0010);// masterApn
        // 主服务器APN，无线通信拨号访问点。若网络制式为CDMA，则该处为PPP拨号号码
        connectServerConfig.put("masterName", 0x0011);// masterName
        // 主服务器无线通信拨号用户名
        connectServerConfig.put("masterPassword", 0x0012);// masterPassword
        // 主服务器无线通信拨号密码
        connectServerConfig.put("masterIP", 0x0013);// masterIP 主服务器地址IP或域名
        connectServerConfig.put("backupApn", 0x0014);// backupApn
        // 备用服务器APN，无线通信拨号访问点
        connectServerConfig.put("backupName", 0x0015);// backupName
        // 备用服务器无线通信拨号用户名
        connectServerConfig.put("backupPasswd", 0x0016);// backupPasswd
        // 备用服务器无线通信拨号密码
        connectServerConfig.put("backupIP", 0x0017);// backupIP 备用服务器地址IP或域名
        connectServerConfig.put("tcpPort", 0x0018);// tcpPort 服务器TCP端口
        connectServerConfig.put("udpPort", 0x0019);// udpPort 服务器UDP端口

        Map<String, Integer> reportTacticsAndInterval = new HashMap<String, Integer>();
        reportTacticsAndInterval.put("tactics", 0x0020);// tactics 汇报策略枚举
        reportTacticsAndInterval.put("program", 0x0021);// program 汇报方案枚举
        reportTacticsAndInterval.put("notLoginTime", 0x0022);// notLoginTime 驾驶员未登录汇报间隔，单位秒
        reportTacticsAndInterval.put("sleepingTime", 0x0027);// sleepingTime 休眠时汇报时间间隔，单位秒
        reportTacticsAndInterval.put("urgentTime", 0x0028);// urgentTime 紧急报警汇报间隔，单位秒
        reportTacticsAndInterval.put("defaultTime", 0x0029);// defaultTime 缺省时间汇报间隔，单位秒
        reportTacticsAndInterval.put("defaultDistance", 0x002C);// defaultDistance 缺省距离汇报间隔，单位米
        reportTacticsAndInterval.put("notLoginDistance", 0x002D);// notLoginDistance 驾驶员未登录汇报距离间隔，单位米
        reportTacticsAndInterval.put("sleepingDistance", 0x002E);// sleepingDistance 休眠时汇报距离间隔，单位米
        reportTacticsAndInterval.put("urgentDistance", 0x002F);// urgentDistance 紧急报警时汇报距离间隔，单位米
        reportTacticsAndInterval.put("inflectionAngle", 0x0030);// inflectionAngle 拐点补传角度(<180)
        reportTacticsAndInterval.put("fenceRadius", 0x0031);// fenceRadius 电子围栏半径（非法位移阀值），单位米

        Map<String, Integer> privilegeNumbers = new HashMap<String, Integer>();
        privilegeNumbers.put("platformPhone", 0x0040);// platformPhone 监控平台电话号码
        privilegeNumbers.put("resetPhone", 0x0041);// resetPhone 复位电话号码，可采用此电话号码拨打终端电话让终端复位
        privilegeNumbers.put("factoryResetPhone", 0x0042);// factoryResetPhone恢复出厂设置电话号码，可采用此电话号码拨打终端电话让终端恢复出厂设置
        privilegeNumbers.put("PlatformsmsPhone", 0x0043);// PlatformsmsPhone 监控平台SMS电话号码
        privilegeNumbers.put("smsAlarmPhone", 0x0044);// smsAlarmPhone 接收终端SMS文本报警号码
        privilegeNumbers.put("tactics", 0x0045);// tactics 终端电话接听策略
        privilegeNumbers.put("everyTalking", 0x0046);// everyTalking 每次最长通话时间，单位秒
        privilegeNumbers.put("monthTalking", 0x0047);// monthTalking 每月最长通话时间，单位秒
        privilegeNumbers.put("listenerPhone", 0x0048);// listenerPhone 监听电话号码
        privilegeNumbers.put("privilegeSmsPhone", 0x0048);// privilegeSmsPhone 监管平台特权短信号码

        Map<String, Integer> alarmTriggeredSetting = new HashMap<String, Integer>();
        alarmTriggeredSetting.put("alarmMask", 0x0050);// alarmMask报警屏蔽，与报警枚举相对应，相应值为1则表示该位报警屏蔽
        alarmTriggeredSetting.put("alarmSmsSwitch", 0x0051);// alarmSmsSwitch 报警发送文本SMS开关，位对应关系同上，1为发送文本SMS
        alarmTriggeredSetting.put("alarmPhotoSwitch", 0x0051);// alarmPhotoSwitch 报警拍摄开关，位对应关系同上，1为报警时拍照
        alarmTriggeredSetting.put("alarmPhotoSave", 0x0053);// alarmPhotoSave 报警拍摄存储标志，为对应关系同上，1为存储在终端，否则实时上传
        alarmTriggeredSetting.put("alarmKeyStatus", 0x0054);// alarmKeyStatus 关键标志，位对应关系同上，1为对应报警为关键报警

        Map<String, Integer> speedingAlarm = new HashMap<String, Integer>();
        speedingAlarm.put("maxSpeed", 0x0055);// maxSpeed 最高速度，单位千米/小时
        speedingAlarm.put("continueTime", 0x0056);// continueTime 超速持续时间，单位秒
        speedingAlarm.put("warningSpeed", 0x005B);// warningSpeed 超速报警预警差值，单位千米/小时

        Map<String, Integer> fatigueDriving = new HashMap<String, Integer>();
        fatigueDriving.put("continueDrivingTime", 0x0057);// continueDrivingTime 连续驾驶时间门限，单位秒
        fatigueDriving.put("dayCumulativeDrivingTime", 0x0058);// dayCumulativeDrivingTime当天累计驾驶时间门限，单位秒
        fatigueDriving.put("minRestingTime", 0x0059);// minRestingTime 最小休息时间，单位秒
        fatigueDriving.put("warningFatigue", 0x005C);// warningFatigue 疲劳驾驶预警差值，单位千米/小时

        Map<String, Integer> multiMediaParameter = new HashMap<String, Integer>();
        multiMediaParameter.put("pictureQuality", 0x0070);// pictureQuality 图像/视频质量，1~10,1最好
        multiMediaParameter.put("brightness", 0x0070);// brightness 亮度，0~255
        multiMediaParameter.put("contrast", 0x0072);// contrast 对比度，0~127
        multiMediaParameter.put("saturation", 0x0072);// saturation 饱和度，0~127
        multiMediaParameter.put("chroma", 0x0074);// chroma 色度，0~255

        Map<String, Integer> vehicleInfoSetting = new HashMap<String, Integer>();
        vehicleInfoSetting.put("mileageValues", 0x0080);// mileageValues 车辆里程表读数，单位米
        vehicleInfoSetting.put("provinceIdentify", 0x0081);// provinceIdentify 车辆所在的省域ID
        vehicleInfoSetting.put("cityIdentify", 0x0082);// cityIdentify 车辆所在的市域ID
        vehicleInfoSetting.put("vehicleLicense", 0x0083);// vehicleLicense 公安交通管理部门颁发的机动车号牌
        vehicleInfoSetting.put("licenseColor", 0x0084);// licenseColor 车牌颜色按照JT/T415-2006中5.4.12的规定

        Map<String, Integer> roadTransportPermit = new HashMap<String, Integer>();
        roadTransportPermit.put("mainServerIp", 0x001A);// mainServerIp IC卡认证主服务器IP或域名
        roadTransportPermit.put("tcpPort", 0x001B);// tcpPort IC卡认证主服务器TCP端口
        roadTransportPermit.put("udpPort", 0x001C);// udpPort IC卡认证主服务器UDP端口
        roadTransportPermit.put("backupServerIp", 0x001D);// backupServerIpIC卡认证备份服务器IP或域名

        Map<String, Integer> collisionAlarm = new HashMap<String, Integer>();
        collisionAlarm.put("collisionTime", 0x005D);// collisionTime碰撞时间，单位毫秒(ms) **b7-b0： 碰撞时间，单位4ms；
        collisionAlarm.put("acceleration", 0x005D);// acceleration 碰撞加速度，单位g **b7-b0： 碰撞时间，单位4ms；

        Map<String, Integer> rolloverAlarm = new HashMap<String, Integer>();
        rolloverAlarm.put("rolloverAngle", 0x005E);// rolloverAngle 侧翻角度

        Map<String, Integer> takePictureControl = new HashMap<String, Integer>();
        takePictureControl.put("passageStatus", 0);// passageStatus 通道状态
        takePictureControl.put("status", 0);// status true：定时；false定距
        takePictureControl.put("timingInterval", 0);// timingInterval 定时定距间隔数值
        takePictureControl.put("unit", 0);// unit 定时定距间隔单位

        Map<String, Integer> gnssSetting = new HashMap<String, Integer>();
        gnssSetting.put("locationMode", 0x0090);// locationMode 定位模式
        gnssSetting.put("baudRate", 0x0091);// baudRate 波特率
        gnssSetting.put("locationRate", 0x0092);// locationRate 定位数据输出频率
        gnssSetting.put("collectRate", 0x0093);// collectRate 定位数据采集频率，单位秒
        gnssSetting.put("uploadSetting", 0x0095);// uploadSetting 定位数据上传设置

        Map<String, Integer> canBusSetting = new HashMap<String, Integer>();
        canBusSetting.put("settings", 0x0110);// settings CAN总线控制设置
        canBusSetting.put("collects", 0x0110);// collects CAN总线ID单独采集设置

        Map<String, Integer> overtimeParking = new HashMap<String, Integer>();
        overtimeParking.put("parkingLimit", 0x005A);// parkingLimit 超时停车阀值

        paramterInternlKeyMap.put(ParameterMessageNumber.MessageTimeoutProcess_VALUE, messageTimeoutProcess);
        paramterInternlKeyMap.put(ParameterMessageNumber.ConnectServerConfig_VALUE, connectServerConfig);
        paramterInternlKeyMap.put(ParameterMessageNumber.ReportTacticsAndInterval_VALUE, reportTacticsAndInterval);
        paramterInternlKeyMap.put(ParameterMessageNumber.PrivilegeNumbers_VALUE, privilegeNumbers);
        paramterInternlKeyMap.put(ParameterMessageNumber.SpeedingAlarm_VALUE, speedingAlarm);
        paramterInternlKeyMap.put(ParameterMessageNumber.FatigueDriving_VALUE, fatigueDriving);
        paramterInternlKeyMap.put(ParameterMessageNumber.MultiMediaParameter_VALUE, multiMediaParameter);
        paramterInternlKeyMap.put(ParameterMessageNumber.VehicleInfoSetting_VALUE, vehicleInfoSetting);
        paramterInternlKeyMap.put(ParameterMessageNumber.RoadTransportPermit_VALUE, roadTransportPermit);
        paramterInternlKeyMap.put(ParameterMessageNumber.CollisionAlarm_VALUE, collisionAlarm);
        paramterInternlKeyMap.put(ParameterMessageNumber.RolloverAlarm_VALUE, rolloverAlarm);
        paramterInternlKeyMap.put(ParameterMessageNumber.TakePictureControl_VALUE, takePictureControl);
        paramterInternlKeyMap.put(ParameterMessageNumber.GnssSetting_VALUE, gnssSetting);
        paramterInternlKeyMap.put(ParameterMessageNumber.CanBusSetting_VALUE, canBusSetting);
        paramterInternlKeyMap.put(ParameterMessageNumber.OvertimeParking_VALUE, overtimeParking);

        stringParameters.add(26);
        stringParameters.add(16);
        stringParameters.add(17);
        stringParameters.add(18);
        stringParameters.add(19);
        stringParameters.add(20);
        stringParameters.add(21);
        stringParameters.add(22);
        stringParameters.add(23);
        stringParameters.add(64);
        stringParameters.add(65);
        stringParameters.add(66);
        stringParameters.add(67);
        stringParameters.add(68);
        stringParameters.add(72);
        stringParameters.add(73);
        stringParameters.add(131);

        reservedParameters.add(8);
        reservedParameters.add(9);
        reservedParameters.add(10);
        reservedParameters.add(11);
        reservedParameters.add(12);
        reservedParameters.add(13);
        reservedParameters.add(14);
        reservedParameters.add(15);
        reservedParameters.add(26);
        reservedParameters.add(27);
        reservedParameters.add(28);
        reservedParameters.add(29);
        reservedParameters.add(30);
        reservedParameters.add(31);
        reservedParameters.add(35);
        reservedParameters.add(36);
        reservedParameters.add(37);
        reservedParameters.add(38);
        reservedParameters.add(42);
        reservedParameters.add(43);
        reservedParameters.add(49);
        reservedParameters.add(50);
        reservedParameters.add(51);
        reservedParameters.add(52);
        reservedParameters.add(53);
        reservedParameters.add(54);
        reservedParameters.add(55);
        reservedParameters.add(56);
        reservedParameters.add(57);
        reservedParameters.add(58);
        reservedParameters.add(59);
        reservedParameters.add(60);
        reservedParameters.add(61);
        reservedParameters.add(62);
        reservedParameters.add(63);
        reservedParameters.add(74);
        reservedParameters.add(75);
        reservedParameters.add(76);
        reservedParameters.add(77);
        reservedParameters.add(78);
        reservedParameters.add(79);
        reservedParameters.add(91);
        reservedParameters.add(92);
        //reservedParameters.add(93);
        reservedParameters.add(94);
        reservedParameters.add(95);
        reservedParameters.add(96);
        reservedParameters.add(97);
        reservedParameters.add(98);
        reservedParameters.add(99);
        reservedParameters.add(100);
        reservedParameters.add(101);
        reservedParameters.add(102);
        reservedParameters.add(103);
        reservedParameters.add(104);
        reservedParameters.add(105);
        reservedParameters.add(106);
        reservedParameters.add(107);
        reservedParameters.add(108);
        reservedParameters.add(109);
        reservedParameters.add(110);
        reservedParameters.add(111);
        reservedParameters.add(117);
        reservedParameters.add(118);
        reservedParameters.add(119);
        reservedParameters.add(120);
        reservedParameters.add(121);
        reservedParameters.add(122);
        reservedParameters.add(123);
        reservedParameters.add(124);
        reservedParameters.add(125);
        reservedParameters.add(126);
        reservedParameters.add(127);

        paramterMap.put(0x0001, 0x0001);
        paramterMap.put(0x0002, 0x0002);
        paramterMap.put(0x0003, 0x0003);
        paramterMap.put(0x0004, 0x0004);
        paramterMap.put(0x0005, 0x0005);
        paramterMap.put(0x0006, 0x0006);
        paramterMap.put(0x0007, 0x0007);

        paramterMap.put(0x0008, 0x0008);
        paramterMap.put(0x0009, 0x0009);
        paramterMap.put(0x000A, 0x000A);
        paramterMap.put(0x000B, 0x000B);
        paramterMap.put(0x000C, 0x000C);
        paramterMap.put(0x000D, 0x000D);
        paramterMap.put(0x000E, 0x000E);
        paramterMap.put(0x000F, 0x000F);

        paramterMap.put(0x0010, 0x0010);
        paramterMap.put(0x0011, 0x0011);
        paramterMap.put(0x0012, 0x0012);
        paramterMap.put(0x0013, 0x0013);
        paramterMap.put(0x0014, 0x0014);
        paramterMap.put(0x0015, 0x0015);
        paramterMap.put(0x0016, 0x0016);
        paramterMap.put(0x0017, 0x0017);

        paramterMap.put(0x0018, 0x0018);
        paramterMap.put(0x0019, 0x0019);
        paramterMap.put(0x001A, 0x001A);
        paramterMap.put(0x001B, 0x001B);
        paramterMap.put(0x001C, 0x001C);
        paramterMap.put(0x001D, 0x001D);
        paramterMap.put(0x001E, 0x001E);
        paramterMap.put(0x001F, 0x001F);
        paramterMap.put(0x0020, 0x0020);
        paramterMap.put(0x0021, 0x0021);
        paramterMap.put(0x0022, 0x0022);
        paramterMap.put(0x0023, 0x0023);
        paramterMap.put(0x0024, 0x0024);
        paramterMap.put(0x0025, 0x0025);
        paramterMap.put(0x0026, 0x0026);
        paramterMap.put(0x0027, 0x0027);
        paramterMap.put(0x0028, 0x0028);
        paramterMap.put(0x0029, 0x0029);
        paramterMap.put(0x002A, 0x002A);
        paramterMap.put(0x002B, 0x002B);
        paramterMap.put(0x002C, 0x002C);
        paramterMap.put(0x002D, 0x002D);
        paramterMap.put(0x002E, 0x002E);
        paramterMap.put(0x002F, 0x002F);
        paramterMap.put(0x0030, 0x0030);
        paramterMap.put(0x0031, 0x0031);
        paramterMap.put(0x0032, 0x0032);
        paramterMap.put(0x0033, 0x0033);
        paramterMap.put(0x0034, 0x0034);
        paramterMap.put(0x0035, 0x0035);
        paramterMap.put(0x0036, 0x0036);
        paramterMap.put(0x0037, 0x0037);
        paramterMap.put(0x0038, 0x0038);
        paramterMap.put(0x0039, 0x0039);
        paramterMap.put(0x003A, 0x003A);
        paramterMap.put(0x003B, 0x003B);
        paramterMap.put(0x003C, 0x003C);
        paramterMap.put(0x003D, 0x003D);
        paramterMap.put(0x003E, 0x003E);
        paramterMap.put(0x003F, 0x003F);
        paramterMap.put(0x0040, 0x0040);
        paramterMap.put(0x0041, 0x0041);
        paramterMap.put(0x0042, 0x0042);
        paramterMap.put(0x0043, 0x0043);
        paramterMap.put(0x0044, 0x0044);
        paramterMap.put(0x0045, 0x0045);
        paramterMap.put(0x0046, 0x0046);
        paramterMap.put(0x0047, 0x0047);
        paramterMap.put(0x0048, 0x0048);
        paramterMap.put(0x0049, 0x0049);
        paramterMap.put(0x004A, 0x004A);
        paramterMap.put(0x004B, 0x004B);
        paramterMap.put(0x004C, 0x004C);
        paramterMap.put(0x004D, 0x004D);
        paramterMap.put(0x004E, 0x004E);
        paramterMap.put(0x004F, 0x004F);
        paramterMap.put(0x0050, 0x0050);
        paramterMap.put(0x0051, 0x0051);
        paramterMap.put(0x0052, 0x0052);
        paramterMap.put(0x0053, 0x0053);
        paramterMap.put(0x0054, 0x0054);
        paramterMap.put(0x0055, 0x0055);
        paramterMap.put(0x0056, 0x0056);
        paramterMap.put(0x0057, 0x0057);
        paramterMap.put(0x0058, 0x0058);
        paramterMap.put(0x0059, 0x0059);
        paramterMap.put(0x005A, 0x005A);
        paramterMap.put(0x005B, 0x005B);
        paramterMap.put(0x005C, 0x005C);
        paramterMap.put(0x005D, 0x005D);
        paramterMap.put(0x005E, 0x005E);
        paramterMap.put(0x005F, 0x005F);
        paramterMap.put(0x0060, 0x0060);
        paramterMap.put(0x0061, 0x0061);
        paramterMap.put(0x0062, 0x0062);
        paramterMap.put(0x0063, 0x0063);
        paramterMap.put(0x0064, 0x0064);
        paramterMap.put(0x0065, 0x0065);
        paramterMap.put(0x0066, 0x0066);
        paramterMap.put(0x0067, 0x0067);
        paramterMap.put(0x0068, 0x0068);
        paramterMap.put(0x0069, 0x0069);
        paramterMap.put(0x006A, 0x006A);
        paramterMap.put(0x006B, 0x006B);
        paramterMap.put(0x006C, 0x006C);
        paramterMap.put(0x006D, 0x006D);
        paramterMap.put(0x006E, 0x006E);
        paramterMap.put(0x006F, 0x006F);
        paramterMap.put(0x0070, 0x0070);
        paramterMap.put(0x0071, 0x0071);
        paramterMap.put(0x0072, 0x0072);
        paramterMap.put(0x0073, 0x0073);
        paramterMap.put(0x0074, 0x0074);
        paramterMap.put(0x0075, 0075);
        paramterMap.put(0x0076, 0076);
        paramterMap.put(0x0077, 0077);
        paramterMap.put(0x0078, 0x0078);
        paramterMap.put(0x0079, 0x0079);
        paramterMap.put(0x007A, 0x007A);
        paramterMap.put(0x007B, 0x007B);
        paramterMap.put(0x007C, 0x007C);
        paramterMap.put(0x007D, 0x007D);
        paramterMap.put(0x007E, 0x007E);
        paramterMap.put(0x007F, 0x007F);
        paramterMap.put(0x0080, 0x0080);
        paramterMap.put(0x0081, 0x0081);
        paramterMap.put(0x0082, 0x0082);
        paramterMap.put(0x0083, 0x0083);
        paramterMap.put(0x0084, 0x0084);
    }

    public int toInt(Object obj) {
        if (obj == null) {
            return 0;

        } else {
            return (int) obj;
        }
    }

    public String toString(Object obj) {
        if (obj == null) {
            return "";

        } else {
            return (String) obj;
        }
    }
}
