package com.navinfo.opentsp.platform.dp.core.acceptor.rp.send;

import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.kit.Packet;

@RPAnno(id="3001")
public class RP_3001_DownCommonRes extends RPCommand {

	@Override
	public int processor(Packet packet) {
		
		return 0;
	}

}
