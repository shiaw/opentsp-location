package com.navinfo.opentsp.platform.da.core.acceptor.procotol.receiver;

import com.navinfo.opentsp.platform.da.core.persistence.CollectionCheckManager;
import com.navinfo.opentsp.platform.da.core.persistence.LcNodeManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.CollectionCheckManagerImpl;
import com.navinfo.opentsp.platform.da.core.persistence.application.LcNodeManageImpl;
import com.navinfo.opentsp.platform.da.core.rmi.DaRmiNo;
import com.navinfo.opentsp.platform.da.core.rmi.Dacommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Mutual_0733_CheckCollection extends Dacommand {
	final static LcNodeManage lcNodeManage = new LcNodeManageImpl();


	@Override
	public Packet processor(Packet packet) {
		CollectionCheckManager collectionManager=new CollectionCheckManagerImpl();
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd");
		Date date=new Date();
		String currentDate=sf.format(date);
		collectionManager.checkCollection(currentDate);
		logger.info("Collection检测完成，回复通用应答...");
		return super.commonResponsesForPlatform(packet.getFrom(),
				packet.getSerialNumber(), packet.getCommand(),
				LCPlatformResponseResult.PlatformResponseResult.success);

	}

   	public static void main(String[] args) {
	   new Mutual_0733_CheckCollection().processor(new Packet(true));
	}

	/**
	 *定时创建mongo集合
	 */
	@Scheduled(cron = "${opentsp.da.schedule.create.collections.cron:0 5 0 * * ?}")
	public void createCollections(){
		new Mutual_0733_CheckCollection().processor(new Packet(true));
		logger.error("创建createCollections结束");
	}
}
