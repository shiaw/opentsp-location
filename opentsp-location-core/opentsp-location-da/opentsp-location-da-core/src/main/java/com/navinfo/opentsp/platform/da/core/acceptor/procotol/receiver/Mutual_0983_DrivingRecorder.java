package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCDrivingRecorder.DrivingRecorder;
import  com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import  com.navinfo.opentsp.platform.da.core.persistence.TermianlDynamicManage;
import  com.navinfo.opentsp.platform.da.core.persistence.application.TermianlDynamicManageImpl;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDrivingRecorderEntity;
@DaRmiNo(id = "0983")
public class Mutual_0983_DrivingRecorder extends Dacommand {
	final static TermianlDynamicManage terminalDynamicManage = new TermianlDynamicManageImpl();

	@Override
	public Packet processor(Packet packet) {
		try {
			DrivingRecorder drivingRecorder = DrivingRecorder
					.parseFrom(packet.getContent());
			GpsDrivingRecorderEntity gpsDrivingRecorderEntity=new GpsDrivingRecorderEntity();
			gpsDrivingRecorderEntity.setContent(drivingRecorder.getRecorderContent().toByteArray());
			gpsDrivingRecorderEntity.setSaveDate(System.currentTimeMillis()/1000);
			gpsDrivingRecorderEntity.settId(drivingRecorder.getTerminalId());
			gpsDrivingRecorderEntity.setType(drivingRecorder.getRecorderCode().getNumber());
			terminalDynamicManage.saveDrivingRecorder(gpsDrivingRecorderEntity);
			super.commonResponsesForPlatform(packet.getFrom(), 
					packet.getSerialNumber(), packet.getCommand(),
					PlatformResponseResult.success);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}
;