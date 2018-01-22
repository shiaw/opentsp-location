package com.navinfo.opentsp.platform.da.core.connector.mm.procotol.receiver;

import java.text.SimpleDateFormat;
import java.util.Date;

import   com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import  com.navinfo.opentsp.platform.da.core.connector.mm.procotol.MMCommand;
import  com.navinfo.opentsp.platform.da.core.persistence.CollectionCheckManager;
import  com.navinfo.opentsp.platform.da.core.persistence.LcNodeManage;
import  com.navinfo.opentsp.platform.da.core.persistence.application.CollectionCheckManagerImpl;
import  com.navinfo.opentsp.platform.da.core.persistence.application.LcNodeManageImpl;
import  com.navinfo.opentsp.platform.location.kit.Packet;

public class MM_0733_CheckCollection extends MMCommand {
	final static LcNodeManage lcNodeManage = new LcNodeManageImpl();

	@Override
	public int processor(Packet packet) {
		CollectionCheckManager collectionManager=new CollectionCheckManagerImpl();
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");
		Date date=new Date();
		String currentDate=sf.format(date);
		PlatformResponseResult result = collectionManager.checkCollection(currentDate);
		return 	super.commonResponses(packet.getFrom(), packet.getSerialNumber(),
				packet.getCommand(), result);

	}
	
}
