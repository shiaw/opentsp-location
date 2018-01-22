package com.navinfo.opentsp.platform.dp.core.acceptor.da.send.kafka;


import com.navinfo.opentsp.platform.dp.core.acceptor.da.DACommand;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.dp.core.handler.SendKafkaHandler;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DA_0980_LocationDataSave extends DACommand {

	@Autowired
	private SendKafkaHandler sendKafkaHandler;

	@Override
	public int processor(Packet packet) {
//		this.writeForCache(packet);
		sendKafkaHandler.writeKafKaToDA(packet);
		return 0;
	}

}
