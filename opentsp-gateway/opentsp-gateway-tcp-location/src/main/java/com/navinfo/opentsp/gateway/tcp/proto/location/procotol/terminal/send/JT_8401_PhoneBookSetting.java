package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.send;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCPhoneBookSetting;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
@LocationCommand(id = "8401")
public class JT_8401_PhoneBookSetting extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		try {
			LCPhoneBookSetting.PhoneBookSetting phoneBookSetting = 
					LCPhoneBookSetting.PhoneBookSetting.parseFrom(packet.getContent());
			int type = phoneBookSetting.getTypes().ordinal();
			List<LCPhoneBookSetting.PhoneBook> list = phoneBookSetting.getBooksList();
			List<byte[]> phones = new ArrayList<byte[]>();
			int capacity = 2;
			for(LCPhoneBookSetting.PhoneBook phone : list) {
				int permission = phone.getPerminssion().getNumber();
				String phoneNumber = phone.getPhoneNumber();
				byte[] phoneNumberBytes = phoneNumber.getBytes("GBK");
				String connector = phone.getConnector();
				byte[] connectorBytes = connector.getBytes("GBK");
				phones.add(Convert.longTobytes(permission, 1));
				phones.add(Convert.longTobytes(phoneNumberBytes.length, 1));
				phones.add(phoneNumberBytes);
				phones.add(Convert.longTobytes(connectorBytes.length, 1));
				phones.add(connectorBytes);
				
				capacity = capacity + 3 + phoneNumberBytes.length + connectorBytes.length;
			}
			
			Packet outPacket = new Packet(capacity);
			outPacket.setCommand(0x8401);
			outPacket.setSerialNumber(packet.getSerialNumber());
			outPacket.setUniqueMark(packet.getUniqueMark());
			outPacket.appendContent(Convert.longTobytes(type, 1));
			outPacket.appendContent(Convert.longTobytes(list.size(), 1));
			for(byte[] obj : phones) {
				outPacket.appendContent(obj);
			}

			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outPacket);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
}
