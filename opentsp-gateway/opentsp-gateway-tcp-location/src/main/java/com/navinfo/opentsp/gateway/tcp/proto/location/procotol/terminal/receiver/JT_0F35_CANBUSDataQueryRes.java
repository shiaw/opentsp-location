package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;


import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQueryRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataReport;

public class JT_0F35_CANBUSDataQueryRes extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		byte[] content = packet.getContent();
		int responsesSerialNumber = Convert.byte2Int(ArraysUtils.subarrays(content, 0, 2), 2);//应答流水号
		int dataItems = Convert.byte2Int(ArraysUtils.subarrays(content, 2, 2), 2);//数据项个数
		long firstReceiveTime = Convert.byte2Long(ArraysUtils.subarrays(content, 4, 5), 5);//CAN数据接收时间 ,第一条数据的接收时间，hh-mm-ss-msms

		AnswerEntry answerEntry = AnswerCommandCache.getInstance().getAnswerEntry(packet.getUniqueMark(), responsesSerialNumber, true);
		
		LCCANBUSDataReport.CANBUSDataReport.Builder cANBUSDataReport = LCCANBUSDataReport.CANBUSDataReport.newBuilder();
		byte[] data = ArraysUtils.subarrays(content, 9);
		for (int i = 0; i < dataItems; i++) {
			byte[] canId = ArraysUtils.subarrays(data,i * 12 + 0, 4);
			byte[] canContent = ArraysUtils.subarrays(data,i * 12 + 4, 8);
		//	CANBUSDataHandler.handle(cANBUSDataReport, Convert.byte2Int(canId, 4), canContent);
		}
		
		LCCANBUSDataQueryRes.CANBUSDataQueryRes.Builder cANBUSDataQueryRes = LCCANBUSDataQueryRes.CANBUSDataQueryRes.newBuilder();
		cANBUSDataQueryRes.setSerialNumber(responsesSerialNumber);
		cANBUSDataQueryRes.setCanData(cANBUSDataReport);
		
		Packet _out_packet = new Packet(); 
		_out_packet.setCommand(LCAllCommands.AllCommands.Terminal.CANBUSDataQueryRes_VALUE);//0x3135
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_out_packet.setUniqueMark(packet.getUniqueMark());
		_out_packet.setSerialNumber(packet.getSerialNumber());
		_out_packet.setContent(cANBUSDataQueryRes.build().toByteArray());
		super.sendCommandStatus(packet.getUniqueMark(),responsesSerialNumber,_out_packet);
        super.writeKafKaToDP(_out_packet, TopicConstants.POSRAW);
		PacketResult packetResult=new PacketResult();
		packetResult.setKafkaPacket(_out_packet);
		return packetResult;
	}
}
