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

@LocationCommand(id = "2259")
public class DP_2259_CancelFatigueDriving extends DPCommand {
	// 　　注：取消疲劳驾驶报警使用疲劳驾驶报警消息，连续驾驶时间、累计驾驶时间、最小休息时间、最长停车时间值设置为：0xFFFFFFFF。
	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		//缓存指令信息
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.FatigueDrivingCancel_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		int pnumber = 0;
		BytesArray bytesArray = new BytesArray();
		// continueDrivingTime 连续驾驶时间门限，单位秒
		bytesArray.append(Convert.longTobytes(0x57, 4));
		bytesArray.append(Convert.longTobytes(4, 1));
		bytesArray.append(Convert.longTobytes(0xFFFFFFFF, 4));
		pnumber++;
		// dayCumulativeDrivingTime 当天累计驾驶时间门限，单位秒
		bytesArray.append(Convert.longTobytes(0x58, 4));
		bytesArray.append(Convert.longTobytes(4, 1));
		bytesArray.append(Convert.longTobytes(0xFFFFFFFF, 4));
		pnumber++;
		// minRestingTime 最小休息时间，单位秒
		bytesArray.append(Convert.longTobytes(0x59, 4));
		bytesArray.append(Convert.longTobytes(4, 1));
		bytesArray.append(Convert.longTobytes(0xFFFFFFFF, 4));
		pnumber++;
		// maxParkingTime 最长停车时间，单位秒
		bytesArray.append(Convert.longTobytes(0x5A, 4));
		bytesArray.append(Convert.longTobytes(4, 1));
		bytesArray.append(Convert.longTobytes(0xFFFFFFFF, 4));
		pnumber++;
		byte[] bytes = ArraysUtils.arraycopy(Convert.intTobytes(pnumber, 1), bytesArray.get());
		packet.setCommand(Constant.JTProtocol.TerminalSetting);
		packet.setContent(bytes);
		//return super.writeToTerminal(packet);
		PacketResult packetResult=new PacketResult();
		packetResult.setTerminalPacket(packet);
		return packetResult;
	}
}
