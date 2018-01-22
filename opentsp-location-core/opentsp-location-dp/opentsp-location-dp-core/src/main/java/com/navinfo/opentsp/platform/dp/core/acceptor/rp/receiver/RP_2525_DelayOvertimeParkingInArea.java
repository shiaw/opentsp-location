package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDelayOvertimePark.DelayOvertimePark;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDelayOvertimeParkingInArea;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCRegularDataSave.RegularDataSave;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;

@RPAnno(id="2525")
public class RP_2525_DelayOvertimeParkingInArea extends RPCommand {

	@Override
	public int processor(Packet packet) {
		try {
			LCDelayOvertimeParkingInArea.DelayOvertimeParkingInArea delayOvertimeParkingInArea = LCDelayOvertimeParkingInArea.DelayOvertimeParkingInArea.parseFrom(packet.getContent());
			log.error("收到" + packet.getUniqueMark()+ "DelayOvertimeParkingInArea请求"+ delayOvertimeParkingInArea.getDelayTime());

			DelayOvertimePark.Builder delayOvertimeParkBuilder = DelayOvertimePark.newBuilder();
			delayOvertimeParkBuilder.setDelayDate(delayOvertimeParkingInArea.getDelayTime());
			// 添加延时缓存
			// TODO: 16/9/5
			//DelayTimeCache.getInstance().addDelayTimeCache(packet.getUniqueMark(),delayOvertimeParkingInArea.getDelayTime()+ System.currentTimeMillis()/1000);

			Boolean re = saveToDA(packet,delayOvertimeParkBuilder);
			if(re){
				super.commonResponses(4, packet.getFrom(),packet.getSerialNumber(), packet.getCommand(),PlatformResponseResult.success_VALUE);
			}else{
				super.commonResponses(4, packet.getFrom(),packet.getSerialNumber(), packet.getCommand(),PlatformResponseResult.failure_VALUE);
			}

		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}

		return 0;
	}
	
	private Boolean saveToDA(Packet packet, DelayOvertimePark.Builder delayOvertimeParkBuilder){
		Boolean re =true;
		RegularData.Builder regularDataBuilder = RegularData.newBuilder();
		regularDataBuilder.setTerminalId(Convert.uniqueMarkToLong(packet.getUniqueMark()));
		regularDataBuilder.setRegularCode(RegularCode.delayOvertimePark);
		regularDataBuilder.setLastModifyDate(System.currentTimeMillis()/1000);
		regularDataBuilder.setType(RegularType.individual);
		regularDataBuilder.setDelayPark(delayOvertimeParkBuilder.build());
		
		RegularDataSave.Builder regularDataSaveBuilder = RegularDataSave.newBuilder();
		regularDataSaveBuilder.addDatas(regularDataBuilder.build());
		
		Packet _packet = new Packet(true);
		_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_packet.setTo(Convert.uniqueMarkToLong(packet.getUniqueMark()));
		_packet.setFrom(NodeHelper.getNodeCode());
//		_packet.setUniqueMark(packet.getUniqueMark());
		_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_packet.setCommand(AllCommands.DataAccess.RegularDataSave_VALUE);
		_packet.setContent(regularDataSaveBuilder.build().toByteArray());

		try {
			super.writeToDataAccess(_packet);
		}catch (Exception e) {
			re = false;
		}
		return re;
	}

}
