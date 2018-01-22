package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import   com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import   com.navinfo.opentsp.platform.location.protocol.common.LCTerminalStatisticStatus.TerminalStatisticStatus;
import  com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import  com.navinfo.opentsp.platform.da.core.persistence.redis.service.TerminalStatisticStatusService;
import  com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.TerminalStatisticStatusServiceImpl;
@DaRmiNo(id = "0994")
public class Mutual_0994_TerminalStatisticStatus extends Dacommand {

	TerminalStatisticStatusService service = new TerminalStatisticStatusServiceImpl();
	@Override
	public Packet processor(Packet packet) {
		try {
			TerminalStatisticStatus status = TerminalStatisticStatus.parseFrom(packet.getContent());
			int type = status.getType();
			long terminalId = status.getTerminalIdentify();
			PlatformResponseResult saveStatus = service.saveStatus(type, terminalId);
			super.commonResponsesForPlatform(packet.getFrom(), 
					packet.getSerialNumber(), packet.getCommand(),
					saveStatus);
		} catch (InvalidProtocolBufferException e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

}
