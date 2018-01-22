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
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCRolloverAlarm;

@LocationCommand(id = "2264")
public class DP_2264_RolloverAlarm  extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
			//缓存指令信息
			AnswerEntry answerEntry = new AnswerEntry();
			answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.RolloverAlarm_VALUE);
			answerEntry.setSerialNumber(packet.getSerialNumber());
			answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);
			answerEntry.setTimeout(ta_commandTimeoutThreshold);
			AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
			try {
				LCRolloverAlarm.RolloverAlarm rolloverAlarm= LCRolloverAlarm.RolloverAlarm.parseFrom(packet.getContent());
				int pnumber = 0;
				BytesArray bytesArray = new BytesArray();
				//侧翻角度
				if(rolloverAlarm.hasRolloverAngle()){
					pnumber++;
					bytesArray.append(Convert.longTobytes(0x5E, 4));
					bytesArray.append(Convert.longTobytes(2, 1));
					bytesArray.append(Convert.intTobytes(rolloverAlarm.getRolloverAngle(), 2));
				}
				byte[] content = ArraysUtils.arraycopy(Convert.intTobytes(pnumber, 1), bytesArray.get());
				Packet outpacket = new Packet();
				outpacket.setCommand(Constant.JTProtocol.TerminalSetting);
				outpacket.setUniqueMark(packet.getUniqueMark());
				outpacket.setSerialNumber(packet.getSerialNumber());
				outpacket.setContent(content);
				//return super.writeToTerminal(outpacket);
				PacketResult packetResult=new PacketResult();
				packetResult.setTerminalPacket(outpacket);
				return packetResult;
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
		
			return null;
	
	}
}
