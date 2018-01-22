package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.remote.LCWirelessUpdate;

import java.io.UnsupportedEncodingException;
@LocationCommand(id = "2405")
public class DP_2405_WirelessUpdate extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.WirelessUpdate_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalControl);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
		
		try {
			LCWirelessUpdate.WirelessUpdate wirelessUpdate = LCWirelessUpdate.WirelessUpdate.parseFrom(packet.getContent());
			StringBuffer sb = new StringBuffer();
			//：“URL 地址;拨号点名称;拨号用户名;拨号密码;地址;TCP 端口;UDP 端口;制造商ID; 硬件版本;固件版本; 连接到指定服务器时限”，
			sb.append(wirelessUpdate.getUrlAddress()).append(";");
			sb.append(wirelessUpdate.getDialName()).append(";");
			sb.append(wirelessUpdate.getUsername()).append(";");
			sb.append(wirelessUpdate.getPassword()).append(";");
			sb.append(wirelessUpdate.getServerIp()).append(";");
			sb.append(wirelessUpdate.getTcpPort()).append(";");
			sb.append(wirelessUpdate.getUdpPort()).append(";");
			sb.append(wirelessUpdate.getProductId()).append(";");
			sb.append(wirelessUpdate.getHardwareVersion()).append(";");
			sb.append(wirelessUpdate.getFirmwareVersion()).append(";");
			sb.append(wirelessUpdate.getConnectTime());
			logger.error(sb.toString());
			Packet outpacket = new Packet();
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setCommand(Constant.JTProtocol.TerminalControl);
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(ArraysUtils.arraycopy(new byte[]{1}, sb.toString().getBytes("GBK")));
			//return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
