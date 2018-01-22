package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import  com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import  com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCLocationDataSave.LocationDataSave;
import  com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import  com.navinfo.opentsp.platform.da.core.persistence.TermianlDynamicManage;
import  com.navinfo.opentsp.platform.da.core.persistence.application.TermianlDynamicManageImpl;
import  com.navinfo.opentsp.platform.location.kit.Packet;
import  com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import  com.navinfo.opentsp.platform.location.kit.lang.DateUtils.DateFormat;
@DaRmiNo(id = "0980")
public class Mutual_0980_LocationDataSave extends Dacommand {
	final static TermianlDynamicManage terminalDynamicManage = new TermianlDynamicManageImpl();
	int size = 0;
	String startTime = DateUtils.format(System.currentTimeMillis()/1000, DateFormat.HH_MM_SS);

	@Override
	public Packet processor(Packet packet) {
		try {
			LocationDataSave locationDataSave = LocationDataSave
					.parseFrom(packet.getContent());
			terminalDynamicManage.saveGpsData(locationDataSave.getTerminalId(),
					locationDataSave.getLocationData());
			super.commonResponsesForPlatform(packet.getFrom(), 
					packet.getSerialNumber(), packet.getCommand(),
					PlatformResponseResult.success);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}
