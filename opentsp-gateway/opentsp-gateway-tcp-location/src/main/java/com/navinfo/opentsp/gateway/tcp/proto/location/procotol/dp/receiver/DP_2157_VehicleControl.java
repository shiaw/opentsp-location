package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCTerminalControl;

@LocationCommand(id = "2157")
public class DP_2157_VehicleControl extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		//缓存指令信息
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.VehicleControl_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.VehicleControl);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

//		packet.setCommand(Constant.JTProtocol.VehicleControl);
//		this.forwardTermianl(packet);
		try {
			LCTerminalControl.TerminalControl terminalControl =
					LCTerminalControl.TerminalControl.parseFrom(packet.getContent());
		    int controlStatus = terminalControl.getControlStatus();
		    Packet outPacket = new Packet(1);
		    outPacket.setCommand(Constant.JTProtocol.VehicleControl);
		    outPacket.setSerialNumber(packet.getSerialNumber());
		    outPacket.setUniqueMark(packet.getUniqueMark());
		    outPacket.appendContent(Convert.longTobytes(controlStatus, 1));
		    outPacket.setTo(Long.parseLong(packet.getUniqueMark()));
		  //  return super.writeToTerminal(outPacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outPacket);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}
