package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;


import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.persistence.RegularDataAreaAndDataManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.RegularDataAreaAndDataManageImpl;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCRegularDataSave;
@DaRmiNo(id = "0933")
public class Mutual_0933_RegularDataSave extends Dacommand {
	final static RegularDataAreaAndDataManage manager = new RegularDataAreaAndDataManageImpl();

	@Override
	public Packet processor(Packet packet) {
		try {
			LCRegularDataSave.RegularDataSave rds=LCRegularDataSave.RegularDataSave.parseFrom(packet.getContent());
			manager.saveRegularAndAreaInfo(rds);
			return commonResponsesForPlatform(packet.getFrom(), packet.getCommand(), packet.getSerialNumber(), LCPlatformResponseResult.PlatformResponseResult.success);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return commonResponsesForPlatform(packet.getFrom(), packet.getCommand(), packet.getSerialNumber(), LCPlatformResponseResult.PlatformResponseResult.failure);
		}
	}
}
