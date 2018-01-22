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
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCCollisionAlarm;

@LocationCommand(id = "2263")
public class DP_2263_CollisionAlarm extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
			//缓存指令信息
			AnswerEntry answerEntry = new AnswerEntry();
			answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.CollisionAlarm_VALUE);
			answerEntry.setSerialNumber(packet.getSerialNumber());
			answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);
			answerEntry.setTimeout(ta_commandTimeoutThreshold);
			AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
			try {
				LCCollisionAlarm.CollisionAlarm collisionAlarm= LCCollisionAlarm.CollisionAlarm.parseFrom(packet.getContent());
				BytesArray bytesArray = new BytesArray();
				float acceleration = collisionAlarm.getAcceleration();
				int collisionTime = collisionAlarm.getCollisionTime();
		
				bytesArray.append(Convert.longTobytes(0x5D, 4));
				bytesArray.append(Convert.longTobytes(2, 1));
				bytesArray.append(Convert.intTobytes(collisionTime / 4, 1));
				bytesArray.append(Convert.intTobytes((int) (acceleration * 10), 1));
			

				Packet outpacket = new Packet();
				outpacket.setCommand(Constant.JTProtocol.TerminalSetting);
				outpacket.setUniqueMark(packet.getUniqueMark());
				outpacket.setSerialNumber(packet.getSerialNumber());
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
