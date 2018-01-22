package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCVehiclePassInArea.VehiclePassInArea;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCVehiclePassTimes.VehiclePassTimes;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCRegularDataSave.RegularDataSave;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;

@RPAnno(id="2505")
public class RP_2505_VehiclePassInArea extends RPCommand {
	
	@Override
	public int processor(Packet packet) {
		try {
			VehiclePassInArea vehiclePassInArea = VehiclePassInArea.parseFrom(packet.getContent());
			
			VehiclePassTimes.Builder vehiclePassTimesBuilder = VehiclePassTimes.newBuilder();
			vehiclePassTimesBuilder.setAreaId(vehiclePassInArea.getAreaInfo().getAreaIdentify());
			
			RegularData.Builder RegularDataBuilder = RegularData.newBuilder();		
			RegularDataBuilder.setRegularCode(RegularCode.vehiclePassStatistic);
			RegularDataBuilder.setLastModifyDate(System.currentTimeMillis()/1000);
			RegularDataBuilder.setType(RegularType.statistic);
			RegularDataBuilder.setPassTimes(vehiclePassTimesBuilder);
			
			RegularDataSave.Builder regularDataSaveBuilder = RegularDataSave.newBuilder();
			regularDataSaveBuilder.addDatas(RegularDataBuilder.build());
			regularDataSaveBuilder.addInfos(vehiclePassInArea.getAreaInfo());
			
			Packet _out_packet = new Packet(true);
			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
			_out_packet.setCommand(AllCommands.DataAccess.RegularDataSave_VALUE);
			_out_packet.setContent(regularDataSaveBuilder.build().toByteArray());
			
			int result= super.writeToDataAccess(_out_packet);
			if(result==0){
				this.commonResponses(1,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);
			}else{
				this.commonResponses(1,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.failure_VALUE);
			}
			
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return 0;
	}
	
}
