//package com.navinfo.opentsp.platform.dp.core.acceptor;
//
//import com.navinfo.opentsp.common.messaging.Command;
//import com.navinfo.opentsp.platform.location.kit.Packet;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class TAMutualCommandFacotry {
//	private static Logger logger = LoggerFactory
//			.getLogger(TAMutualCommandFacotry.class);
//
//	public static final Object processor(Packet packet) {
//		Command command = TACommandCache.getInstance().getCommand(
//				packet.getCommandForHex());
//		if (command != null) {
//			command.processor(packet);
//		} else {
//			logger.error("指令[ " + packet.getCommandForHex() + " ]未找到匹配的协议解析类 . ");
//		}
//		return null;
//	}
//}
