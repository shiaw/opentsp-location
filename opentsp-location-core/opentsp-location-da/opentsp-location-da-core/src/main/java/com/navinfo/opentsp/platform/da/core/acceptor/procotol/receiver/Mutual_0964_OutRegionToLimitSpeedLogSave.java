package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import  com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import  com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLimitSpeedSave.OutRegionToLimitSpeedSave;
import com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import com.navinfo.opentsp.platform.da.core.persistence.LogManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.LogManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcOutRegionLimitSpeedLogDBEntity;
@DaRmiNo(id = "0963")
public class Mutual_0964_OutRegionToLimitSpeedLogSave extends Dacommand {
	final static LogManage logManage=new LogManageImpl();
	@Override
	public Packet processor(Packet packet) {
		try {
			OutRegionToLimitSpeedSave save = OutRegionToLimitSpeedSave.parseFrom(packet.getContent());
			LcOutRegionLimitSpeedLogDBEntity entity = new LcOutRegionLimitSpeedLogDBEntity();
			entity.setTerminal_id(save.getTerminalId());
			entity.setOriginal_area_id(save.getAreaIdentify());
			entity.setLimit_date((int)save.getLimitDate());
			entity.setLimit_speed(save.getLimitSpeed());
			if(save.getSigns()){
				entity.setSign(1);
			}else {
				entity.setSign(0);
			}
			logManage.saveOutRegionLimitSpeedLog(entity);
			
			super.commonResponsesForPlatform(packet.getFrom(), packet.getCommand(), packet.getSerialNumber(), PlatformResponseResult.success);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}
}
