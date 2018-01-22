package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;

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
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCSaveMultimediaSingleUpload;
@LocationCommand(id = "2166")
public class DP_2166_SaveMultimediaSingleUpload extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.SaveMultimediaSingleUpload_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.SaveMultimediaSingleUpload);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
		//Packet packetRet = TerminalResponse.response(answerEntry.getInternalCommand(), Long.parseLong(answerEntry.getUniqueMark()), LCResponseResult.ResponseResult.success, answerEntry.getSerialNumber());
		//DPFacade.write(packetRet);
		try {
			LCSaveMultimediaSingleUpload.SaveMultimediaSingleUpload saveMultimediaSingleUpload = LCSaveMultimediaSingleUpload.SaveMultimediaSingleUpload.parseFrom(packet.getContent());
			
			
			BytesArray bytesArray = new BytesArray();
		
			bytesArray.append(Convert.longTobytes(saveMultimediaSingleUpload.getMediaId(), 4));
			bytesArray.append(Convert.intTobytes(saveMultimediaSingleUpload.getIsDelete()?1:0, 1));
			
			
			Packet outpacket = new Packet();
			outpacket.setCommand(Constant.JTProtocol.SaveMultimediaSingleUpload);
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
