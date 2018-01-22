package com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.entry;

import java.io.Serializable;

public class TerminalEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final long NOT_SESSION = -1;
	private long terminalId;

	public TerminalEntity() {
		super();
	}

	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}

}
