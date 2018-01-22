package com.navinfo.opentsp.platform.da.core.acceptor.procotol.send;


import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;

public class Mutual_1900_GetTerminalInfosRes extends DalCommand {
	@Override
	public int processor(Packet packet) {
		this.write(packet);
		return 0;
	}

}
