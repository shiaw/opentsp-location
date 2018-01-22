package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.persistence.LogManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.LogManageImpl;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTerminalOperateLogUpdate;

@DaRmiNo(id = "0962")
public class Mutual_0962_TerminalOperateLogUpdate extends Dacommand {
	final static LogManage logManage=new LogManageImpl();
	@Override
	public Packet processor(Packet packet) {
		try {
			LCTerminalOperateLogUpdate.TerminalOperateLogUpdate terminalOperateLogUpdate= LCTerminalOperateLogUpdate.TerminalOperateLogUpdate.parseFrom(packet.getContent());
			logManage.terminalOperateLogUpdate(terminalOperateLogUpdate.getTerminalId(),terminalOperateLogUpdate.getFunctionCode(),terminalOperateLogUpdate.getResults());
			super.commonResponsesForPlatform(packet.getFrom(), packet.getCommand(), packet.getSerialNumber(), LCPlatformResponseResult.PlatformResponseResult.success);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}
}
