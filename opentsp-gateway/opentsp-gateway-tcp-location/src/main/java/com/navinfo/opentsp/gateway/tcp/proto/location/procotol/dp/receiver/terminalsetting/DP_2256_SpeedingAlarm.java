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
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCSpeedingAlarm;

@LocationCommand(id = "2256")
public class DP_2256_SpeedingAlarm extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		try {
			//缓存指令信息
			AnswerEntry answerEntry = new AnswerEntry();
			answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.SpeedingAlarmSetting_VALUE);
			answerEntry.setSerialNumber(packet.getSerialNumber());
			answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);
			answerEntry.setTimeout(ta_commandTimeoutThreshold);
			AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

			LCSpeedingAlarm.SpeedingAlarm speedingAlarm = LCSpeedingAlarm.SpeedingAlarm.parseFrom(packet.getContent());
			int pnumber = 0;
			BytesArray bytesArray = new BytesArray();
			//maxSpeed	最高速度，单位千米/小时
			bytesArray.append(Convert.longTobytes(0x55, 4));
			bytesArray.append(Convert.longTobytes(4, 1));
			bytesArray.append(Convert.longTobytes(speedingAlarm.getMaxSpeed(), 4));
			pnumber++;
			//continueTime	超速持续时间，单位秒
			bytesArray.append(Convert.longTobytes(0x56, 4));
			bytesArray.append(Convert.longTobytes(4, 1));
			bytesArray.append(Convert.longTobytes(speedingAlarm.getContinueTime(), 4));
			pnumber++;
			
			byte[] bytes = ArraysUtils.arraycopy(Convert.intTobytes(pnumber, 1), bytesArray.get());
			packet.setCommand(Constant.JTProtocol.TerminalSetting);
			packet.setContent(bytes);
			//return super.writeToTerminal(packet);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(packet);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}
}
