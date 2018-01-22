package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.persistence.OptResult;
import com.navinfo.opentsp.platform.da.core.persistence.TerminalInfoManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.TerminalInfoManageImpl;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCUpdateTerminalInfo;

@DaRmiNo(id = "0901")
public class Mutual_0901_UpdateTerminalInfo extends Dacommand {
	final static TerminalInfoManage terminalInfoManage = new TerminalInfoManageImpl();

	@Override
	public Packet processor(Packet packet) {
		try {
			LCUpdateTerminalInfo.UpdateTerminalInfo uti=LCUpdateTerminalInfo.UpdateTerminalInfo.parseFrom(packet.getContent());
			long terminalId=uti.getTerminalId();
			int districtCode=uti.getDistrictCode().getNumber();
			long node_code=uti.getNodeCode();
			long changeTid = uti.getChangeTId();
			OptResult or =terminalInfoManage.updateTerminalInfo(terminalId, districtCode, node_code,changeTid);
			if(or.getStatus()>0){
				super.commonResponsesForPlatform(packet.getFrom(), packet.getCommand(), packet.getSerialNumber(), LCPlatformResponseResult.PlatformResponseResult.success);
			}else{
				super.commonResponsesForPlatform(packet.getFrom(), packet.getCommand(), packet.getSerialNumber(), LCPlatformResponseResult.PlatformResponseResult.failure);
				
			}
		} catch (InvalidProtocolBufferException e) {
//			e.printStackTrace();
			logger.error("",e);
		}
		return null;
	}
}
