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
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCMediaType;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCSaveMultimediaUpload;

@LocationCommand(id = "2167")
public class DP_2167_SaveMultimediaUpload extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.SaveMultimediaUpload_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.SaveMultimediaUpload);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			LCSaveMultimediaUpload.SaveMultimediaUpload saveMultimediaUpload = LCSaveMultimediaUpload.SaveMultimediaUpload.parseFrom(packet.getContent());
			LCMediaType.MediaType types = saveMultimediaUpload.getTypes();
			
			
			BytesArray bytesArray = new BytesArray();
			bytesArray.append(Convert.intTobytes(types.getNumber(), 1));
			bytesArray.append(Convert.intTobytes(saveMultimediaUpload.getChannels(), 1));
			bytesArray.append(Convert.intTobytes(saveMultimediaUpload.getEvents().getNumber(), 1));
			
			bytesArray.append(Convert.hexStringToBytes(DateUtils.format(saveMultimediaUpload.getBeginDate(), DateUtils.DateFormat.YYMMDDHHMMSS)));
			bytesArray.append(Convert.hexStringToBytes(DateUtils.format(saveMultimediaUpload.getEndDate(), DateUtils.DateFormat.YYMMDDHHMMSS)));
			bytesArray.append(Convert.intTobytes(saveMultimediaUpload.getIsDelete()?1:0, 1));
			Packet outpacket = new Packet();
			outpacket.setCommand(Constant.JTProtocol.SaveMultimediaUpload);
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArray.get());
		//	return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
            return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}
