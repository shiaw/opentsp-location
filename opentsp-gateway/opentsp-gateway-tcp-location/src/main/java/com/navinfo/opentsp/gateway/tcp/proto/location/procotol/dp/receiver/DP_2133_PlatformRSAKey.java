package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.LCPlatformRSAKey;
import org.springframework.beans.factory.annotation.Value;
@LocationCommand(id = "2133")
public class DP_2133_PlatformRSAKey extends DPCommand {

    @Value("${ta_commandTimeoutThreshold:3000}")
    private  int ta_commandTimeoutThreshold;
	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		//缓存指令信息
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.PlatformRSAKey_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		answerEntry.setTerminalCommand(Constant.JTProtocol.PlatformRSAKey);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
		
		try {
			LCPlatformRSAKey.PlatformRSAKey platformRSAKey =
					LCPlatformRSAKey.PlatformRSAKey.parseFrom(packet.getContent());
			long eKey = platformRSAKey.getEKey();
			ByteString nKey = platformRSAKey.getNKey();
			
			BytesArray bytesArray = new BytesArray();
			bytesArray.append(Convert.longTobytes(eKey, 4));
			bytesArray.append(nKey.toByteArray());
			
			
			Packet outpacket = new Packet();
			outpacket.setCommand(Constant.JTProtocol.PlatformRSAKey);
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArray.get());
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
