package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@LocationCommand(id = "2050")
public class DP_2050_LocationQuery extends DPCommand {

    private static final Logger log = LoggerFactory.getLogger(DP_2050_LocationQuery.class);
	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		//缓存指令信息
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setChannelId(packet.getChannels());
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.CallName_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.LocationQuery);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		answerEntry.setDownCmd(packet.getCommandForHex());
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		
		//System.err.println(packet.getUniqueMark()+"_"+packet.getSerialNumber()+"=="+answerEntry);
		packet.setCommand(Constant.JTProtocol.LocationQuery);
		
		//return super.writeToTerminal(packet);
		PacketResult packetResult=new PacketResult();
		packetResult.setTerminalPacket(packet);
        return packetResult;
	}

}
