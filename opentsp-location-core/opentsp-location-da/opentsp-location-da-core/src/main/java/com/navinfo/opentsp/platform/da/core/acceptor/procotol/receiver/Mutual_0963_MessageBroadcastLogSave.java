package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCMessageBroadcastSave.MessageBroadcastSave;
import  com.navinfo.opentsp.platform.da.core.acceptor.procotol.DalCommand;
import  com.navinfo.opentsp.platform.da.core.persistence.LogManage;
import  com.navinfo.opentsp.platform.da.core.persistence.application.LogManageImpl;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcMessageBroadcastLogDBEntity;
@DaRmiNo(id = "0963")
public class Mutual_0963_MessageBroadcastLogSave extends Dacommand {
	final static LogManage logManage=new LogManageImpl();
	@Override
	public Packet processor(Packet packet) {
		try {
			MessageBroadcastSave save = MessageBroadcastSave.parseFrom(packet.getContent());
			LcMessageBroadcastLogDBEntity entity=new LcMessageBroadcastLogDBEntity();
			entity.setTerminal_id(save.getTerminalId());
			entity.setOriginal_area_id(save.getAreaIdentify());
			entity.setContent(save.getBroadcastContent());
			entity.setBroadcast_time(save.getBroadcastDate());
			if(save.getSigns().getIsBroadcast()){
				entity.setBroadcast_mode(1);
			}else if(save.getSigns().getIsDisplay()){
				entity.setBroadcast_mode(2);
			}else if(save.getSigns().getIsAdvertiseScreen()){
				entity.setBroadcast_mode(3);
			}
			logManage.saveMessageBroadcastLog(entity);
			super.commonResponsesForPlatform(packet.getFrom(), packet.getCommand(), packet.getSerialNumber(), PlatformResponseResult.success);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}
}
