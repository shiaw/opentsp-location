package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;

import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;

@LocationCommand(id = "8F45")
public class JT_8F45_InfoSynRes extends TerminalCommand {
	private byte[] szKey = new byte[]{0x18,0x07,0x23,0x49,0x65,0x73,(byte)0x91,0x36};
	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		if(packet.getContent() != null){
			packet.setCommand(0x8F45);
			byte[] content  = packet.getContent();
			byte[] captcha = getCaptcha(ArraysUtils.subarrays(content, 1));
			packet.setContent(ArraysUtils.arraycopy(content, captcha));
		//	super.write(packet);
		}
		PacketResult packetResult=new PacketResult();
		packetResult.setTerminalPacket(packet);
		return packetResult;
	}

	
	private byte[] getCaptcha(byte[] rand){
		byte[] captcha = new byte[10];
		for(int i=0;i<8;i++){
			captcha[i] = (byte)(szKey[i]^rand[i%4]);
		}
		captcha[8] = 0;
		captcha[9] = 0;
		return captcha;
	}

}
