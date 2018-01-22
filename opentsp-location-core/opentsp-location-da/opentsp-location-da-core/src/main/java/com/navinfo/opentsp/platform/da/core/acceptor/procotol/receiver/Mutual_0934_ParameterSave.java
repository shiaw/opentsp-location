package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.common.TerminalSettingKey;
import com.navinfo.opentsp.platform.da.core.persistence.OptResult;
import com.navinfo.opentsp.platform.da.core.persistence.TerminalInfoManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.TerminalInfoManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalParaDBEntity;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCParameterSave;

@DaRmiNo(id = "0934")
public class Mutual_0934_ParameterSave extends Dacommand {
	final static TerminalInfoManage terminalInfoManage = new TerminalInfoManageImpl();

	@Override
	public Packet processor(Packet packet) {
		try {
			LCParameterSave.ParameterSave trs = LCParameterSave.ParameterSave.parseFrom(packet.getContent());
			LcTerminalParaDBEntity para = new LcTerminalParaDBEntity();
			para.setLast_update_time(System.currentTimeMillis() / 1000);
			para.setPara_code(trs.getCommandCode());
			para.setPara_content(trs.getParaContent().toByteArray());
			para.setTerminal_id(trs.getTerminalId());
			OptResult opt= new OptResult();
			int relationCode = TerminalSettingKey.GetCommandCode(trs.getCommandCode());
			if (relationCode == -1) {
				opt = terminalInfoManage.saveOrUpdateTerminalParameter(para);
			}
			else{
				opt=terminalInfoManage.deleteTerminalParameter(trs.getTerminalId(), relationCode);
			}
			if (opt.getStatus() > 0) {
				super.commonResponsesForPlatform(packet.getFrom(), packet.getCommand(), packet.getSerialNumber(),
						LCPlatformResponseResult.PlatformResponseResult.success);
			} else {
				super.commonResponsesForPlatform(packet.getFrom(), packet.getCommand(), packet.getSerialNumber(),
						LCPlatformResponseResult.PlatformResponseResult.failure);

			}
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}
}
