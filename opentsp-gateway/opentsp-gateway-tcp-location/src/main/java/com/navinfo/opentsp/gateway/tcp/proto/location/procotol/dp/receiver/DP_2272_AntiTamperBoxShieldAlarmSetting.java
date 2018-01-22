package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;


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
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting;
@LocationCommand(id = "2272")
public class DP_2272_AntiTamperBoxShieldAlarmSetting extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		//缓存指令信息
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.AntiTamperBoxShieldAlarmSetting_VALUE);//2272
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);//8103
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
		
		try {
			LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting antiTamperBoxShieldAlarmSetting = LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting.parseFrom(packet.getContent());
			
			BytesArray bytesArray = new BytesArray();
			bytesArray.append(Convert.intTobytes(1, 1));//设置一个参数
			
			bytesArray.append(Convert.intTobytes(0XF00E, 4));//扩展报警位屏蔽字,与附加报警位信息中的报警标志位相对应，相应位为1则相应报警被屏蔽
			bytesArray.append(Convert.intTobytes(8, 1));
			bytesArray.append(antiTamperBoxShieldAlarmSetting.getShieldingSetting().toByteArray());//8个长度 byte
			if(antiTamperBoxShieldAlarmSetting.getShieldingSetting().toByteArray().length < 8) {
				logger.error("2272 防拆盒定制报警设置，扩展报警位屏蔽字需要8个byte，收到为："+antiTamperBoxShieldAlarmSetting.getShieldingSetting().toByteArray().length);
			}
			
			Packet outpacket = new Packet();
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setCommand(Constant.JTProtocol.TerminalSetting);
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArray.get());

			//return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return packetResult;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
}
