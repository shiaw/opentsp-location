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
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCFatigueDriving;

@LocationCommand(id = "2258")
public class DP_2258_FatigueDriving extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		try {
			//缓存指令信息
			AnswerEntry answerEntry = new AnswerEntry();
			answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.FatigueDrivingSetting_VALUE);
			answerEntry.setSerialNumber(packet.getSerialNumber());
			answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);
			answerEntry.setTimeout(ta_commandTimeoutThreshold);
			AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

			LCFatigueDriving.FatigueDriving fatigueDriving = LCFatigueDriving.FatigueDriving.parseFrom(packet.getContent());
			int pnumber = 0;
			BytesArray bytesArray = new BytesArray();
			//continueDrivingTime	连续驾驶时间门限，单位秒
			bytesArray.append(Convert.longTobytes(0x57, 4));
			bytesArray.append(Convert.longTobytes(4, 1));
			bytesArray.append(Convert.longTobytes(fatigueDriving.getContinueDrivingTime(), 4));
			pnumber++;
			//dayCumulativeDrivingTime	当天累计驾驶时间门限，单位秒
			bytesArray.append(Convert.longTobytes(0x58, 4));
			bytesArray.append(Convert.longTobytes(4, 1));
			bytesArray.append(Convert.longTobytes(fatigueDriving.getDayCumulativeDrivingTime(), 4));
			pnumber++;
			//minRestingTime	最小休息时间，单位秒
			bytesArray.append(Convert.longTobytes(0x59, 4));
			bytesArray.append(Convert.longTobytes(4, 1));
			bytesArray.append(Convert.longTobytes(fatigueDriving.getMinRestingTime(), 4));
			pnumber++;
			//maxParkingTime	最长停车时间，单位秒
//			bytesArray.append(Convert.longTobytes(0x5A, 4));
//			bytesArray.append(Convert.longTobytes(4, 1));
//			bytesArray.append(Convert.longTobytes(fatigueDriving.getMaxParkingTime(), 4));
//			pnumber++;
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
