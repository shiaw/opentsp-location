package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;

@RPAnno(id="2152")
public class RP_2152_CallListener extends RPCommand {

	@Override
	public int processor(Packet packet) {
		packet.setTo(packet.getFrom());
		super.writeToTermianl(packet);
		super.terminalOperateLog(packet.getFrom(), AllCommands.Terminal.CallListener_VALUE, packet.getContent());
		return 0;
	}

}
