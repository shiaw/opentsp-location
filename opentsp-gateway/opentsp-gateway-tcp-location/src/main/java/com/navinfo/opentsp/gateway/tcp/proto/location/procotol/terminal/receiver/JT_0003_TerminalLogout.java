package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;


import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.LCResultCode;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.auth.TerminalLogoutCommand;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import org.springframework.beans.factory.annotation.Autowired;

@LocationCommand(id = "0003")
public class JT_0003_TerminalLogout extends TerminalCommand {

    @Autowired
    private  MessageChannel messageChannel;

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		Packet _out_packet = new Packet();
		_out_packet.setCommand(LCAllCommands.AllCommands.Terminal.TerminalLogout_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_out_packet.setUniqueMark(packet.getUniqueMark());
		_out_packet.setSerialNumber(packet.getSerialNumber());
		//super.writeToDataProcessing(_out_packet);
        TerminalLogoutCommand logoutCommand=new TerminalLogoutCommand();
        logoutCommand.setDeviceId(packet.getUniqueMark());
        messageChannel.send(logoutCommand);
        connection.close();
		PacketResult packetResult=new PacketResult();
		packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(),
				Constant.JTProtocol.TerminalLogout,
				LCResultCode.JTTerminal.SUCCESS));
		//具体业务待定,暂回复通用应答
		return  packetResult;
		//终端注销,回收链路资源
	//	TerminalInfo terminalInfo = TerminalManage.getInstance().getTerminal(packet.getUniqueMark());
	//	MutualSessionManage.getInstance().destroyLink(terminalInfo.getSessionId());
	//	return 0;
	}

}
