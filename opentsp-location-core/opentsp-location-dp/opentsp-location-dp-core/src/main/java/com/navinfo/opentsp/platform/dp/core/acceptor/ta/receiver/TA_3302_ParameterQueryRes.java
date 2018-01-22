//package com.navinfo.opentsp.platform.dp.core.acceptor.receiver;
//
//import com.google.protobuf.InvalidProtocolBufferException;
//import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCParameterQueryRes;
//import com.lc.dp.acceptor.tal.protocol.TACommand;
//import com.navinfo.opentsp.platform.location.kit.Packet;
//
//public class TA_3302_ParameterQueryRes extends TACommand {
//
//	@Override
//	public int processor(Packet packet) {
//		try {
//
//			LCParameterQueryRes.ParameterQueryRes parameterQueryRes = LCParameterQueryRes.ParameterQueryRes.parseFrom(packet.getContent());
//			parameterQueryRes.getCollision();
//
//		} catch (InvalidProtocolBufferException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		super.writeToRequestProcessing(packet);
//		return 0;
//	}
//
//}
