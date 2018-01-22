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
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCPhoneBookSetting;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
@LocationCommand(id = "2304")
public class DP_2304_PhoneBookSetting extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.PhoneBookSetting_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.SetTerminalPhotoBookSetting);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
		
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
			outPacket.setCommand(Constant.JTProtocol.SetTerminalPhotoBookSetting);
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
			//return super.writeToTerminal(outPacket);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
