package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver.terminalsetting;


import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCCanBusSetting;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCCanBusSetting.CanBusSetting;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCCanBusSetting.ControlSetting;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCCanBusSetting.SingleCollect;

import java.util.List;

@LocationCommand(id = "2267")
public class DP_2267_CanBusSetting extends DPCommand {

    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
//		//缓存指令信息
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.RolloverAlarm_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
        try {
            AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
            CanBusSetting gnssSetting = CanBusSetting.parseFrom(packet.getContent());
            List<ControlSetting> controlSettingsList = gnssSetting.getSettingsList();
            List<SingleCollect> singleCollectsList = gnssSetting.getCollectsList();
            BytesArray bytesArray = new BytesArray();
            int pnumber = 0;
            for (ControlSetting controlSetting : controlSettingsList) {
                bytesArray.append(Convert.longTobytes(0x95, 4));
                bytesArray.append(Convert.longTobytes(4, 1));
                bytesArray.append(Convert.intTobytes(controlSetting.getPassages().getNumber(), 4));
                pnumber++;
                if (controlSetting.getPassages().getNumber() == 0x01) {
                    bytesArray.append(Convert.longTobytes(0x100, 4));
                    bytesArray.append(Convert.longTobytes(4, 1));
                    bytesArray.append(Convert.intTobytes(controlSetting.getTimingInterval(), 4));
                    pnumber++;

                    bytesArray.append(Convert.longTobytes(0x101, 4));
                    bytesArray.append(Convert.longTobytes(2, 1));
                    bytesArray.append(Convert.intTobytes(controlSetting.getUploadInterval(), 2));
                    pnumber++;
                } else {
                    bytesArray.append(Convert.longTobytes(0x102, 4));
                    bytesArray.append(Convert.longTobytes(4, 1));
                    bytesArray.append(Convert.intTobytes(controlSetting.getTimingInterval(), 4));
                    pnumber++;

                    bytesArray.append(Convert.longTobytes(0x103, 4));
                    bytesArray.append(Convert.longTobytes(2, 1));
                    bytesArray.append(Convert.intTobytes(controlSetting.getUploadInterval(), 2));
                    pnumber++;
                }
            }
            for (SingleCollect singleCollect : singleCollectsList) {
                bytesArray.append(Convert.longTobytes(0x110, 4));
                bytesArray.append(Convert.longTobytes(8, 1));
                int[] bytes = new int[64];
                //时间间隔
                String timingInterval = Integer.toBinaryString(singleCollect.getTimingInterval());
                int passages = singleCollect.getPassages().getNumber();
                int types = singleCollect.getTypes().getNumber();
                int collectType = singleCollect.getCollectType() ? 1 : 0;
                String canBusIdentify = Integer.toBinaryString(singleCollect.getCanBusIdentify());
                bytes[31] = passages;
                bytes[30] = types;
                bytes[29] = collectType;
                for (int i = 0; i < canBusIdentify.length(); i++) {
                    bytes[i] = Integer.valueOf(canBusIdentify.substring(i));
                }
                for (int i = 0; i < timingInterval.length(); i++) {
                    bytes[32 + i] = Integer.valueOf(timingInterval.substring(i));
                }
                String str = "";
                for (int i = bytes.length - 1; i >= 0; i--) {
                    str += bytes[i];
                }
                bytesArray.append(Convert.longTobytes(Long.valueOf(str), 8));
                pnumber++;
            }
            byte[] bytes = ArraysUtils.arraycopy(Convert.intTobytes(pnumber, 1), bytesArray.get());
            Packet outpacket = new Packet();
            outpacket.setCommand(Constant.JTProtocol.TerminalSetting);
            outpacket.setUniqueMark(packet.getUniqueMark());
            outpacket.setSerialNumber(packet.getSerialNumber());
            outpacket.setContent(bytes);
            //return super.writeToTerminal(outpacket);
            PacketResult packetResult = new PacketResult();
            packetResult.setTerminalPacket(outpacket);
            return packetResult;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;

    }
}
