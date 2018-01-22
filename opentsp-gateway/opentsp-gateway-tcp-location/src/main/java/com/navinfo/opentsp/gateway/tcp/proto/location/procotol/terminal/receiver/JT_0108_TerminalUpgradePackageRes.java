
package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;


import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCUpgradeResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCUpgradeResult.UpgradeResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCUpgradeType;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCUpgradeType.UpgradeType;
import com.navinfo.opentsp.platform.location.protocol.terminal.remote.LCTerminalUpgradePackageRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.remote.LCTerminalUpgradePackageRes.TerminalUpgradePackageRes;

public class JT_0108_TerminalUpgradePackageRes extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		log.debug("收到终端[ "+packet.getUniqueMark()+" ]终端升级结果通知.");
		byte[] bytes = packet.getContent();
		int types = Convert.byte2Int(ArraysUtils.subarrays(bytes, 0, 1), 1);
		int reuslt = Convert.byte2Int(ArraysUtils.subarrays(bytes, 1, 1), 1);
		byte [] nkey=ArraysUtils.subarrays(bytes, 4,128);
		TerminalUpgradePackageRes.Builder builder = TerminalUpgradePackageRes.newBuilder();
		switch (types) {
			case UpgradeType.beidou_VALUE:
				builder.setTypes(UpgradeType.beidou);
				break;
			case UpgradeType.cardReader_VALUE:
				builder.setTypes(UpgradeType.cardReader);
				break;
			case UpgradeType.terminal_VALUE:
				builder.setTypes(UpgradeType.terminal);
				break;
			default:
				break;
		}
		switch (reuslt) {
			case 0:
				builder.setResults(UpgradeResult.success);
				break;
			case 1:
				builder.setResults(UpgradeResult.failed);
				break;
			case 2:
				builder.setResults(UpgradeResult.cancel);
				break;
			default:
				break;
		}
		Packet _out_packet = new Packet();
		_out_packet.setCommand(AllCommands.Terminal.TerminalUpgradePackageRes_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_out_packet.setUniqueMark(packet.getUniqueMark());
		_out_packet.setSerialNumber(packet.getSerialNumber());
		_out_packet.setContent(builder.build().toByteArray());;
		PacketResult packetResult=new PacketResult();
		packetResult.setKafkaPacket(_out_packet);
       // super.writeKafKaToDP(_out_packet, TopicConstants.POSRAW);
        return  packetResult;
	}

}
