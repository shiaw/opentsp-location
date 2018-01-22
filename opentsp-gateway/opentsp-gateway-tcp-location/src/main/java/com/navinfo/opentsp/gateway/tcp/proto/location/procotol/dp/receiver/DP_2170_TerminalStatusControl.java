package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TerminalProtoVersionCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.ForwardTerminal;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.ProtocolDispatcher;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.send.JT_8F10_TerminalLockSetting;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.send.JT_8F20_TermianlBindSetting;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.send.JT_8F40_TerminalStatusControl;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTerminalStatusControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;


import java.util.List;
@LocationCommand(id = "2170")
public class DP_2170_TerminalStatusControl extends DPCommand {

//	@Autowired
//	private JT_8F10_TerminalLockSetting jt_8F10_terminalLockSetting;
//
//	@Autowired
//	private JT_8F20_TermianlBindSetting jt_8F20_termianlBindSetting;
//
//	@Autowired
//	private JT_8F40_TerminalStatusControl jt_8F40_terminalStatusControl;

	@Autowired
	private TerminalProtoVersionCache terminalProtoVersionCache;

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		PacketResult packetResult=null;
		try {
			String protocolType=terminalProtoVersionCache.get(packet.getUniqueMark());
			if("200120".equals(protocolType)){ //华菱协议
				LCTerminalStatusControl.TerminalStatusControl  statusControl = LCTerminalStatusControl.TerminalStatusControl.parseFrom(packet.getContent());
				int controls = statusControl.getControls().getNumber();// 控制标识
				switch (controls) {
					case 0x01:// 加锁标识，参数值：1锁车，0解锁
						packet.setCommand(Constant.JTProtocol.TerminalLockSetting);
						packetResult=new JT_8F10_TerminalLockSetting().processor(connection,packet);
						break;
					case 0x03:// (终端绑定解绑-->激活，关闭)
						packet.setCommand(Constant.JTProtocol.TerminalBindSetting);
						packetResult=new JT_8F20_TermianlBindSetting().processor(connection,packet);
						break;
					default :
				}
			}else if("200110".equals(protocolType)||"200130".equals(protocolType)){  //部标2013（2012行驶记录仪）+ F9
				packet.setProtocol(Integer.parseInt(protocolType));
				packet.setCommand(Constant.JTProtocol.CANRemoveControl);
				packetResult=new JT_8F40_TerminalStatusControl().processor(connection,packet);
			}
			//return super.writeToTerminal(outPacket);
			return packetResult;
		} catch (InvalidProtocolBufferException e1) {
			e1.printStackTrace();
		}

		return null;
	}

	/**
	 *  缓存指令信息
	 * @param jTProtocol  协议号
	 */
	private void answerCommandCacheAddEntry(Packet packet ,int jTProtocol) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry
				.setInternalCommand(LCAllCommands.AllCommands.Terminal.TerminalStatusControl_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(jTProtocol);
		answerEntry.setTimeout(60);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(),
				packet.getSerialNumber(), answerEntry);
	}


	private Packet buildPacket(int capacity, int jTProtocol,Packet packet,byte[] data) {
		Packet outPacket = new Packet(capacity);
		outPacket.setCommand(jTProtocol);
		outPacket.setSerialNumber(packet.getSerialNumber());
		outPacket.setUniqueMark(packet.getUniqueMark());
		outPacket.setTo(Long.parseLong(packet.getUniqueMark()));
		outPacket.appendContent(data);
		return outPacket;
	}



}
