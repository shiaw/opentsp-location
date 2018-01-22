package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver.terminalsetting;


import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult;

/**
 *删除圆形区域
 * @author jin_s
 *
 */
@LocationCommand(id = "2010")
public class DP_2010_DriverInfoUpPassThrough extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(AllCommands.Terminal.ReportDriverReq_VALUE);// ************
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.DriverInfoUpPassThrough);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
		//Packet packetRet = TerminalResponse.response(answerEntry.getInternalCommand(), Long.parseLong(answerEntry.getUniqueMark()), LCResponseResult.ResponseResult.success, answerEntry.getSerialNumber());
		//DPFacade.write(packetRet);
		Packet outpacket = new Packet();
		outpacket.setSerialNumber(packet.getSerialNumber());
		outpacket.setCommand(Constant.JTProtocol.DriverInfoUpPassThrough);
		outpacket.setUniqueMark(packet.getUniqueMark());
		outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
		
//	    int result = super.writeToTerminal(outpacket);
//	    if(result == 1){
//			Packet pk = TerminalResponse.response(0, packet.getFrom(), ResponseResult.success, packet.getSerialNumber());
//			super.write(pk);
//	    }
		PacketResult packetResult=new PacketResult();
		packetResult.setTerminalPacket(outpacket);
		return packetResult;
	}

}
