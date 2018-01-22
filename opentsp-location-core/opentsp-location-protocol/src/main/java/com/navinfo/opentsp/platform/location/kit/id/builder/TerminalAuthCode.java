package com.navinfo.opentsp.platform.location.kit.id.builder;

import com.navinfo.opentsp.platform.location.kit.id.ID;

public class TerminalAuthCode implements ID {

	@Override
	public Object next() {
		return System.currentTimeMillis();
	}

}
