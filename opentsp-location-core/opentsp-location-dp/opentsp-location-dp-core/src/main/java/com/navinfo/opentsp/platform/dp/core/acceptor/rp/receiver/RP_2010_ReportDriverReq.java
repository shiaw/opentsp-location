package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.kit.Packet;

@RPAnno(id="2010")
public class RP_2010_ReportDriverReq extends RPCommand {

	@Override
	public int processor(Packet packet) {
		//转发TA层
		packet.setTo(packet.getFrom());
		super.writeToTermianl(packet);
		return 0;
	}

}
