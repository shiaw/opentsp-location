//package com.navinfo.opentsp.platform.dp.core.acceptor.receiver;
//
//import com.google.protobuf.InvalidProtocolBufferException;
//import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
//import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
//import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult;
//import com.navinfo.opentsp.platform.location.protocol.terminal.query.LCCallNameRes.CallNameRes;
//import com.lc.dp.acceptor.tal.protocol.TACommand;
//import com.navinfo.opentsp.platform.dp.core.cache.CallNameResCache;
//import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
//import com.navinfo.opentsp.platform.dp.core.rule.RegularHandler;
//import com.navinfo.opentsp.platform.location.kit.Packet;
//
//public class TA_3050_CallNameRes extends TACommand {
//	@Override
//	public int processor(Packet packet) {
//		try {
//			CallNameRes callNameRes = CallNameRes.parseFrom(packet.getContent());
//			if(callNameRes.getResult().getNumber() == ResponseResult.success_VALUE){
//				LCLocationData.LocationData locationData = callNameRes.getData();
//				GpsLocationDataEntity dataEntity = new GpsLocationDataEntity(locationData , packet.getUniqueMark() , packet.getSerialNumber() ,AllCommands.Terminal.CallNameRes_VALUE);
//				CallNameResCache.addCallNameRes(packet.getFrom(), packet.getSerialNumber(), callNameRes.getSerialNumber());
//
//				// 需要通过一致性hash算法,将终端数据分布到上层RP节点
//				RegularHandler.handler(dataEntity);
//
//			}else{
//				super.writeToRequestProcessing(packet);
//			}
//
//		} catch (InvalidProtocolBufferException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return 0;
//	}
//
//}
