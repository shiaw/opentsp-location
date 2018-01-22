package com.navinfo.opentsp.platform.da.core.persistence.entity.extend;

import java.io.Serializable;

public class TerminalOnlineModel implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4802709405089526309L;

	/* 终端基本信息 */
	private TerminalInfoModel terminalInfoModel;
	/* 最新位置信息 */
	private byte[] locationData;

	public TerminalInfoModel getTerminalInfoModel() {
		return terminalInfoModel;
	}

	public void setTerminalInfoModel(TerminalInfoModel terminalInfoModel) {
		this.terminalInfoModel = terminalInfoModel;
	}

	public byte[] getLocationData() {
		return locationData;
	}

	public void setLocationData(byte[] locationData) {
		this.locationData = locationData;
	}

}
