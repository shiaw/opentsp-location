package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver.terminalsetting;

import com.google.protobuf.InvalidProtocolBufferException;
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
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCAlarmTriggeredSetting;

@LocationCommand(id = "2255")
public class DP_2255_AlarmTriggeredSetting extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		//缓存指令信息
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.AlarmTriggeredSetting_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			LCAlarmTriggeredSetting.AlarmTriggeredSetting alarmTriggeredSetting = LCAlarmTriggeredSetting.AlarmTriggeredSetting.parseFrom(packet.getContent());
			BytesArray bytesArray = new BytesArray();
			int pnumber = 0;
			if(alarmTriggeredSetting.hasAlarmMask()){
				pnumber++;
				bytesArray.append(Convert.longTobytes(0x0050, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(alarmTriggeredSetting.getAlarmMask(), 4));
			}
			if(alarmTriggeredSetting.hasAlarmSmsSwitch()){
				pnumber++;
				bytesArray.append(Convert.longTobytes(0x0051, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(alarmTriggeredSetting.getAlarmSmsSwitch(), 4));				
			}
			if(alarmTriggeredSetting.hasAlarmPhotoSwitch()){
				pnumber++;
				bytesArray.append(Convert.longTobytes(0x0052, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(alarmTriggeredSetting.getAlarmPhotoSwitch(), 4));				
			}
			if(alarmTriggeredSetting.hasAlarmPhotoSave()){
				pnumber++;
				bytesArray.append(Convert.longTobytes(0x0053, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(alarmTriggeredSetting.getAlarmPhotoSave(), 4));				
			}			
			if(alarmTriggeredSetting.hasAlarmKeyStatus()){
				pnumber++;
				bytesArray.append(Convert.longTobytes(0x0054, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(alarmTriggeredSetting.getAlarmKeyStatus(), 4));				
			}
			byte[] content = ArraysUtils.arraycopy(Convert.intTobytes(pnumber, 1), bytesArray.get());
			Packet outPacket = new Packet();
			outPacket.setCommand(Constant.JTProtocol.TerminalSetting);
			outPacket.setSerialNumber(packet.getSerialNumber());
			outPacket.setUniqueMark(packet.getUniqueMark());
			outPacket.setContent(content);
			//return super.writeToTerminal(outPacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outPacket);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}
