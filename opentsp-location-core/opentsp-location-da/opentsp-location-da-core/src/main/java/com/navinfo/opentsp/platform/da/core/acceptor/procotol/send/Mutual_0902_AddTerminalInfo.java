package com.navinfo.opentsp.platform.da.core.acceptor.procotol.send;


import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;

public class Mutual_0902_AddTerminalInfo extends DalCommand {

	@Override
	public int processor(Packet packet) {
		return super.write(packet);
	}

}
