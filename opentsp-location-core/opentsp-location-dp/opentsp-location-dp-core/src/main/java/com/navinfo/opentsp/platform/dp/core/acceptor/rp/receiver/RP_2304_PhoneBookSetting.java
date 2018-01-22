package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.kit.Packet;

@RPAnno(id="2304")
public class RP_2304_PhoneBookSetting extends RPCommand {

	@Override
	public int processor(Packet packet) {
		return super.writeToTermianl(packet);
	}

}
