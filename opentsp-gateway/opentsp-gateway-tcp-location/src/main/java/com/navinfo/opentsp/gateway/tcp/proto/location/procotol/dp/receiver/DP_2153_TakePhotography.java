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
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCCommandType;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotography;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes;

@LocationCommand(id = "2153")
public class DP_2153_TakePhotography extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		//缓存指令信息
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.TakePhotography_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.TakePhotography);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			LCTakePhotography.TakePhotography photography =
					LCTakePhotography.TakePhotography.parseFrom(packet.getContent());
			int channel = photography.getChannels();
			int command = photography.getCommandType().getNumber();
			int photoNum = photography.getPhotoNumber();
			int interval = photography.getTakeInterval();
			int status = photography.getSaveStatus().getNumber();
			int resolution = photography.getResolutions().getNumber();
			int quality = photography.getPhotoQuality();
			int saturation = photography.getSaturation();
			int brightness = photography.getBrightness();
			int contrast = photography.getContrast();
			int chroma = photography.getChroma();
			
			Packet outPacket = new Packet(12);
			outPacket.setCommand(Constant.JTProtocol.TakePhotography);
			outPacket.setSerialNumber(packet.getSerialNumber());
			outPacket.setUniqueMark(packet.getUniqueMark());
			outPacket.appendContent(Convert.intTobytes(channel, 1));

			if(command == LCCommandType.CommandType.stopPhoto_VALUE) {
                outPacket.appendContent(Convert.intTobytes(0, 2));
            } else if(command == LCCommandType.CommandType.videoModel_VALUE) {
                outPacket.appendContent(Convert.intTobytes(0xFFFF, 2));
            } else {
                outPacket.appendContent(Convert.intTobytes(photoNum, 2));
            }
			
			outPacket.appendContent(Convert.intTobytes(interval, 2));
			outPacket.appendContent(Convert.intTobytes(status, 1));
			outPacket.appendContent(Convert.intTobytes(resolution, 1));
			outPacket.appendContent(Convert.intTobytes(quality, 1));
			outPacket.appendContent(Convert.intTobytes(brightness, 1));
			outPacket.appendContent(Convert.intTobytes(contrast, 1));
			outPacket.appendContent(Convert.intTobytes(saturation, 1));
			outPacket.appendContent(Convert.intTobytes(chroma, 1));
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outPacket);
			//if(result!=1){//返回处理失败
				LCTakePhotographyRes.TakePhotographyRes.Builder _3153ResBuilder = LCTakePhotographyRes.TakePhotographyRes.newBuilder();
				_3153ResBuilder.setSerialNumber(packet.getSerialNumber());
				_3153ResBuilder.setResult(LCResponseResult.ResponseResult.failure);
				_3153ResBuilder.setResults(LCPhotoResult.PhotoResult.failed);
				
				
				Packet _out_packet = new Packet();
				_out_packet.setCommand(LCAllCommands.AllCommands.Terminal.TakePhotographyRes_VALUE);
				_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
				_out_packet.setUniqueMark(packet.getUniqueMark());
				_out_packet.setSerialNumber(packet.getSerialNumber());
				_out_packet.setContent(_3153ResBuilder.build().toByteArray());
				packetResult.setKafkaPacket(_out_packet);
				//super.writeToDataProcessing(_out_packet);
//			}

			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}
