package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.send;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.LCResultCode;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCTerminalRegisterRes;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@LocationCommand(id = "8100")
public class JT_8100_TerminalRegistrationResponse extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		try {
            PacketResult result=new PacketResult();
            if (packet.getResultCode() == ResultCode.OK.code()) {
                Map resultMap = packet.getPushArguments();
                String authCode = (String) resultMap.get("authCoding");
                //注册失败
                if(StringUtils.isEmpty(authCode)){
                    log.info("注册失败，未找到相应终端信息["+packet.getUniqueMark()+"].");
                    return null;
                }
                byte[] authCodeByte =authCode.getBytes("GBK");
                int capacity = 2+1+authCodeByte.length;
                Packet outpacket = new Packet(true,capacity);
                outpacket.setCommand(Constant.JTProtocol.TerminalRegistrationResponse);
                outpacket.setUniqueMark(packet.getUniqueMark());
                outpacket.appendContent(Convert.longTobytes(packet.getSerialNumber(), 2));
                outpacket.appendContent(LCResultCode.JTTerminal.SUCCESS);
                outpacket.appendContent(authCodeByte);
                log.info("注册成功，向终端["+packet.getUniqueMark()+"]发送鉴权码["+authCode+"].");
                result.setTerminalPacket(outpacket);
                return  result;
            }else{
                log.info("注册失败，终端["+packet.getUniqueMark()+"].");
            }
        //   LCTerminalRegisterRes.TerminalRegisterRes registerRes = LCTerminalRegisterRes.TerminalRegisterRes.parseFrom(packet.getContent());
			 //registerRes.getAuthCoding().getBytes("GBK");

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
