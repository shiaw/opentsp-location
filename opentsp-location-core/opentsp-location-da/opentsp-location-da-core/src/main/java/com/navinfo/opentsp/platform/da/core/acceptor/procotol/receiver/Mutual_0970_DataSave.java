package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import   com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import   com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDataSave.DataSave;
import  com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import  com.navinfo.opentsp.platform.da.core.persistence.TermianlDynamicManage;
import  com.navinfo.opentsp.platform.da.core.persistence.application.TermianlDynamicManageImpl;
@DaRmiNo(id = "0970")
public class Mutual_0970_DataSave extends Dacommand {
	final static TermianlDynamicManage terminalDynamicManage = new TermianlDynamicManageImpl();

	@Override
	public Packet processor(Packet packet) {
		try {
			DataSave dataSave = DataSave
					.parseFrom(packet.getContent());
			terminalDynamicManage.saveLinkExceptionData(dataSave.getNodeNumber(), dataSave.getNodeNumberTo(), dataSave.getDataType(), dataSave.getDataValue().toByteArray());
			super.commonResponsesForPlatform(packet.getFrom(), 
					packet.getSerialNumber(), packet.getCommand(),
					PlatformResponseResult.success);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}
