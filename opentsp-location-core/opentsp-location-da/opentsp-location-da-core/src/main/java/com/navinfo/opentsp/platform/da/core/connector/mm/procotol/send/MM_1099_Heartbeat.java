package com.navinfo.opentsp.platform.da.core.connector.mm.procotol.send;


import com.navinfo.opentsp.platform.da.core.connector.mm.procotol.MMCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;

public class MM_1099_Heartbeat extends MMCommand {

	@Override
	public int processor(Packet packet) {
		return super.broadcast(packet);
	}

}
