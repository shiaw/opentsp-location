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
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCUpgradeType;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCUpgradeType.UpgradeType;
import com.navinfo.opentsp.platform.location.protocol.terminal.remote.LCTerminalUpgradePackage;
import com.navinfo.opentsp.platform.location.protocol.terminal.remote.LCTerminalUpgradePackage.TerminalUpgradePackage;


import java.io.UnsupportedEncodingException;
@LocationCommand(id = "2407")
public class DP_2407_TerminalUpgradePackage extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(AllCommands.Terminal.TerminalUpgradePackage_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalUpgradePackage);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			TerminalUpgradePackage terminalUpgradePackage = TerminalUpgradePackage.parseFrom(packet.getContent());
			BytesArray bytesArray = new BytesArray();
			UpgradeType upgradeType=terminalUpgradePackage.getTypes();
			switch (upgradeType.getNumber()) {
				case UpgradeType.beidou_VALUE:
					bytesArray.append(Convert.longTobytes(UpgradeType.beidou_VALUE, 1));
					break;
				case UpgradeType.cardReader_VALUE:
					bytesArray.append(Convert.longTobytes(UpgradeType.cardReader_VALUE, 1));
					break;
				case UpgradeType.terminal_VALUE:
					bytesArray.append(Convert.longTobytes(UpgradeType.terminal_VALUE, 1));
					break;
				default:
					break;
			}
			try {
				bytesArray.append(terminalUpgradePackage.getManufacturerId().getBytes("gbk"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
//			version	版本号
			String version = terminalUpgradePackage.getVersion();
			bytesArray.append(Convert.longTobytes(version.getBytes("gbk").length, 1));
			bytesArray.append(version.getBytes("gbk"));
//			upgradeData	升级包数据
			ByteString upgradeData = terminalUpgradePackage.getUpgradeData();
			bytesArray.append(Convert.longTobytes(upgradeData.toByteArray().length, 4));
			bytesArray.append(upgradeData.toByteArray());
			Packet outpacket = new Packet();
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setCommand(Constant.JTProtocol.TerminalUpgradePackage);
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArray.get());
			//return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null ;
	}

}
