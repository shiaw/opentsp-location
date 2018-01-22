package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;


import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery;

import static com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.*;
@LocationCommand(id = "2135")
public class DP_2135_CANBUSDataQuery extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		//缓存指令信息
//		AnswerEntry answerEntry = new AnswerEntry();
//		answerEntry.setInternalCommand(AllCommands.Terminal.CANBUSDataQuery_VALUE);//2135
//		answerEntry.setSerialNumber(packet.getSerialNumber());
//		answerEntry.setTerminalCommand(Constant.JTProtocol.CANBUSDataQuery);//8F35
//		answerEntry.setTimeout(ta_commandTimeoutThreshold);
//		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
//		
		try {
			CANBUSDataQuery cANBUSDataQuery = CANBUSDataQuery.parseFrom(packet.getContent());
			
			BytesArray bytesArray = new BytesArray();
			bytesArray.append(Convert.intTobytes(cANBUSDataQuery.getChannelId(), 1));//CAN总线通道  BYTE 01：通道1；02：通道2
			
			bytesArray.append(Convert.intTobytes(cANBUSDataQuery.getInterval() * 1000, 2));//采集时间间隔（单位ms） 如果为0则停止采集。
			
			bytesArray.append(Convert.intTobytes(cANBUSDataQuery.getCollectTime(),2));//采集周期，单位s WORD 
			
			bytesArray.append(Convert.intTobytes(cANBUSDataQuery.getReportInterval(),2));//汇报间隔，单位s（>5s） WORD
			
			bytesArray.append(Convert.longTobytes(cANBUSDataQuery.getReportType(),1));//汇报方式 00：正常；01：压缩
			
			
			Packet outpacket = new Packet();
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setCommand(Constant.JTProtocol.CANBUSDataQuery);
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArray.get());

		//	return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return packetResult;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
}
