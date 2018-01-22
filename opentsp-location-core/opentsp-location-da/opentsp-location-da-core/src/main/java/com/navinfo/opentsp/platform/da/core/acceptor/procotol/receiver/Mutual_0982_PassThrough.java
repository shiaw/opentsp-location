package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import  com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import  com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCPassThrough.PassThrough;
import  com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import  com.navinfo.opentsp.platform.da.core.persistence.TermianlDynamicManage;
import  com.navinfo.opentsp.platform.da.core.persistence.application.TermianlDynamicManageImpl;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsTransferEntity;
@DaRmiNo(id = "0982")
public class Mutual_0982_PassThrough extends Dacommand {
	final static TermianlDynamicManage terminalDynamicManage = new TermianlDynamicManageImpl();

	@Override
	public Packet processor(Packet packet) {
		try {
			PassThrough passThrough = PassThrough
					.parseFrom(packet.getContent());
			GpsTransferEntity entity=new GpsTransferEntity();
			entity.setContent(passThrough.getThroughContent().toByteArray());
			entity.setIsUp(passThrough.getIsUp()==true?1:0);
			entity.setSaveDate(System.currentTimeMillis()/1000);
			entity.settId(passThrough.getTerminalId());
			List<GpsTransferEntity> transferEntity=new ArrayList<GpsTransferEntity>();
			transferEntity.add(entity);
			terminalDynamicManage.savePassThrough(transferEntity);
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