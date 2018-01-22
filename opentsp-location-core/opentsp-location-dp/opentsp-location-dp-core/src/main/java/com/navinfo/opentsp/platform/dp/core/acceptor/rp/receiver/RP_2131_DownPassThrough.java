package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.ByteString;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCPassThrough.PassThrough;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.Packet;

@RPAnno(id="2131")
public class RP_2131_DownPassThrough extends RPCommand {

	@Override
	public int processor(Packet packet) {
		//转发TA层
		packet.setTo(packet.getFrom());
		super.writeToTermianl(packet);

		//转发DA层
		PassThrough.Builder builder = PassThrough.newBuilder();
		builder.setTerminalId(packet.getFrom());
		builder.setIsUp(false);
		builder.setThroughContent(ByteString.copyFrom(packet.getContent()));
		
		Packet _outpacket_da = new Packet(true);
		_outpacket_da.setCommand(AllCommands.DataAccess.PassThrough_VALUE);
		_outpacket_da.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_outpacket_da.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_outpacket_da.setContent(builder.build().toByteArray());
		super.writeToDataAccess(_outpacket_da);
		return 0;
	}

}
