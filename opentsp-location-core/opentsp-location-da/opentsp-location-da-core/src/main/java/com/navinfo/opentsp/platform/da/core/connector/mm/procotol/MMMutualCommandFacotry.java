package com.navinfo.opentsp.platform.da.core.connector.mm.procotol;

import com.navinfo.opentsp.platform.location.kit.Command;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MMMutualCommandFacotry {
	private static Logger logger = LoggerFactory
			.getLogger(MMMutualCommandFacotry.class);

	public static final Object processor(Packet packet) {
		Command command = MMCommandCache.getInstance().getCommand(
				packet.getCommandForHex());
		if (command != null) {
			command.processor(packet);
		} else {
			logger.error("指令[ " + packet.getCommandForHex() + " ]未找到匹配的协议解析类 . ");
		}
		return null;
	}
}
