package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;

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
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCConfirmAlarmMessage;
@LocationCommand(id = "2169")
public class DP_2169_ConfirmAlarmMessage extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.ConfirmAlarmMessage_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.ConfirmAlarmMessage);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);


		try {
			LCConfirmAlarmMessage.ConfirmAlarmMessage confirmAlarmMessage = LCConfirmAlarmMessage.ConfirmAlarmMessage.parseFrom(packet.getContent());

			
			BytesArray bytesArray = new BytesArray();
			//报警消息流水号   isConfirmAll	是否处理该报警类型所有消息true：是；false：否；该字段为true时，流水号字段失效
			bytesArray.append(Convert.intTobytes(confirmAlarmMessage.getIsConfirmAll() == true ? 0 : confirmAlarmMessage.getSerialNumber(), 2));
			long alarmType=0;
			if(confirmAlarmMessage.getEmergency()){
				alarmType=alarmType|1;
			}
			if(confirmAlarmMessage.getDangerous()){
				alarmType=alarmType|8;
			}
			if(confirmAlarmMessage.getInOutArea()){
				alarmType=alarmType|524288;
			}
			if(confirmAlarmMessage.getInOutRoute()){
				alarmType=alarmType|1048576;
			}
			if(confirmAlarmMessage.getSegmentLackOrOverTime()){
				alarmType=alarmType|2097152;
			}
			if(confirmAlarmMessage.getIllegalIgnition()){
				alarmType=alarmType|67108864*2;
			}
			if(confirmAlarmMessage.getIllegalDisplacement()){
				alarmType=alarmType|67108864*4;
			}
	

			bytesArray.append(Convert.longTobytes(alarmType, 4));


		
			Packet outpacket = new Packet();
			outpacket.setCommand(Constant.JTProtocol.ConfirmAlarmMessage);
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArray.get());
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return packetResult;
			//return super.writeToTerminal(outpacket);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}
