package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.LCResultCode;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCTerminalRegisterRes;

import java.io.UnsupportedEncodingException;
@LocationCommand(id = "2003")
public class DP_2003_TerminalRegisterRes extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		try {
			LCTerminalRegisterRes.TerminalRegisterRes registerRes = LCTerminalRegisterRes.TerminalRegisterRes.parseFrom(packet.getContent());
			byte[] authCodeByte = registerRes.getAuthCoding().getBytes("GBK");
			int capacity = 2+1+authCodeByte.length;
			Packet outpacket = new Packet(true,capacity);
			outpacket.setCommand(Constant.JTProtocol.TerminalRegistrationResponse);
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.appendContent(Convert.longTobytes(registerRes.getSerialNumber(), 2));
			outpacket.appendContent(LCResultCode.JTTerminal.SUCCESS);
			outpacket.appendContent(authCodeByte);
			//修改终端鉴权缓存
			logger.info("修改终端["+packet.getUniqueMark()+"]鉴权缓存,鉴权码["+registerRes.getAuthCoding()+"].");
//			TerminalInfo terminalInfo = TerminalManage.getInstance().getTerminal(packet.getUniqueMark());
//			terminalInfo.setAuthCode(registerRes.getAuthCoding());
//			TerminalManage.getInstance().addTerminal(terminalInfo);
//			logger.info("向终端["+packet.getUniqueMark()+"]发送鉴权码["+registerRes.getAuthCoding()+"].");
			//return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
            return  packetResult;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}
