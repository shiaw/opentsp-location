package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.persistence.LogManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.LogManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalOperationLogDBEntity;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTerminalOperateLogSave;

@DaRmiNo(id = "0961")
public class Mutual_0961_TerminalOperateLogSave extends Dacommand {
	final static LogManage logManage=new LogManageImpl();
	@Override
	public Packet processor(Packet packet) {
		try {
			LCTerminalOperateLogSave.TerminalOperateLogSave terminalOperateLogSave= LCTerminalOperateLogSave.TerminalOperateLogSave.parseFrom(packet.getContent());
			LcTerminalOperationLogDBEntity lcTerminalOperationLogDBEntity=new LcTerminalOperationLogDBEntity();
			if(terminalOperateLogSave.getResults()==true){
				lcTerminalOperationLogDBEntity.setOperator_result(1);
			}else {
				lcTerminalOperationLogDBEntity.setOperator_result(0);
			}
			lcTerminalOperationLogDBEntity.setTerminal_id(terminalOperateLogSave.getTerminalId());
			lcTerminalOperationLogDBEntity.setOperation_content(terminalOperateLogSave.getLogContent().toByteArray());
			lcTerminalOperationLogDBEntity.setOperation_time((int)terminalOperateLogSave.getLogDate());
			lcTerminalOperationLogDBEntity.setOperation_type(terminalOperateLogSave.getFunctionCode());
			logManage.terminalOperateLogSave(lcTerminalOperationLogDBEntity);
			super.commonResponsesForPlatform(packet.getFrom(),  packet.getCommand(), packet.getSerialNumber(), LCPlatformResponseResult.PlatformResponseResult.success);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}
}
