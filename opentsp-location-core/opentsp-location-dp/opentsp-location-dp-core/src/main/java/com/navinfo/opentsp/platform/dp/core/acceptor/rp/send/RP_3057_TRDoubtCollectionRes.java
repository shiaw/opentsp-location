package com.navinfo.opentsp.platform.dp.core.acceptor.rp.send;

import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.kit.Packet;

@RPAnno(id="3057")
public class RP_3057_TRDoubtCollectionRes extends RPCommand {

	@Override
	public int processor(Packet packet) {
		this.write(packet);
		return 0;
	}

}
