package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.send;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCCommandType;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotography;
@LocationCommand(id = "8801")
public class JT_8801_TakePhotography extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
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
			outPacket.setCommand(0x8801);
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
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
