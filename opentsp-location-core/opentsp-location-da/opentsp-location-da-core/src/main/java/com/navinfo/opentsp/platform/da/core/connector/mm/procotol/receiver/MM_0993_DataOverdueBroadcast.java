package com.navinfo.opentsp.platform.da.core.connector.mm.procotol.receiver;

import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.location.kit.Packet;
import  com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import  com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDataOverdueBroadcast.DataOverdueBroadcast;
import  com.navinfo.opentsp.platform.da.core.cache.alarm.AlarmQueryManager;
import  com.navinfo.opentsp.platform.da.core.connector.mm.procotol.MMCommand;
import  com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.DASQueryKeyServiceImp;

public class MM_0993_DataOverdueBroadcast extends MMCommand {
	private DASQueryKeyServiceImp dsaDasQueryKeyServiceImp = new DASQueryKeyServiceImp();
	
	@Override
	public int processor(Packet packet) {
		try {
			DataOverdueBroadcast overdueInfo = DataOverdueBroadcast.parseFrom(packet
					.getContent());
			List<String> querykeys = overdueInfo.getKeyList();
			for (String key : querykeys) {
				AlarmQueryManager.getInstance().remove(key);
				dsaDasQueryKeyServiceImp.removeQueryKey(key);
			}
			
			super.commonResponses(packet.getFrom(), packet.getSerialNumber(),
					packet.getCommand(), PlatformResponseResult.success);
			
		
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
