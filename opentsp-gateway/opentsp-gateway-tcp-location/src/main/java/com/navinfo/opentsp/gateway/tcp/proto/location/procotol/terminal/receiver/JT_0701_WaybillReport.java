package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;


import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.LCResultCode;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCWaybillReport;

import java.io.UnsupportedEncodingException;

/**
 * 电子运单上报3009
 * @author admin
 *
 */
@LocationCommand(id = "0701")
public class JT_0701_WaybillReport extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		
		byte[] content = packet.getContent();
		int contentLength = Convert.byte2Int(ArraysUtils.subarrays(content, 0, 4),
                4);
		LCWaybillReport.WaybillReport.Builder builder = LCWaybillReport.WaybillReport.newBuilder();
		
		try {
			String WaybillContent = new String (ArraysUtils.subarrays(content, 4,  contentLength),"gbk");
			builder.setWaybillContent(WaybillContent);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		Packet _out_packet = new Packet();
		_out_packet.setCommand(AllCommands.Terminal.WaybillReport_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_out_packet.setUniqueMark(packet.getUniqueMark());
		_out_packet.setSerialNumber(packet.getSerialNumber());
		_out_packet.setContent(builder.build().toByteArray());;
		//super.writeToDataProcessing(_out_packet);
		//this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), 0x0800, LCResultCode.JTTerminal.SUCCESS);
		PacketResult packetResult=new PacketResult();
		packetResult.setKafkaPacket(_out_packet);
		packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), 0x0800, LCResultCode.JTTerminal.SUCCESS));
		return packetResult;
	}
}
