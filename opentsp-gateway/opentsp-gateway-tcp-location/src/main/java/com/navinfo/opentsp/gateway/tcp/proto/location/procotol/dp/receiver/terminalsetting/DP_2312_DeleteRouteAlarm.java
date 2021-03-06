package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver.terminalsetting;

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
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCDeleteRouteAlarm;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCDeleteRouteAlarm.DeleteRouteAlarm;


import java.util.List;

/**
 * 删除路线
 * @author jin_s
 *
 */
@LocationCommand(id = "2312")
public class DP_2312_DeleteRouteAlarm extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.DeleteRouteAlarm_VALUE);// ************
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.DeleteTerminalRoute);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			DeleteRouteAlarm deleteRouteAlarm = DeleteRouteAlarm.parseFrom(packet.getContent());
			List<Long> areaIdentifyList = deleteRouteAlarm.getAreaIdentifyList();
			
			BytesArray bytesArray = new BytesArray();
	        if(areaIdentifyList!=null&&areaIdentifyList.size()>0){
	        bytesArray.append(Convert.intTobytes(areaIdentifyList.size(), 1));
			  for(long areaIdentify:areaIdentifyList){
				 bytesArray.append(Convert.longTobytes(areaIdentify, 4));
			  }
		     }
			
			Packet outpacket = new Packet();
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setCommand(Constant.JTProtocol.DeleteTerminalRoute);
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArray.get());
			//return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}
