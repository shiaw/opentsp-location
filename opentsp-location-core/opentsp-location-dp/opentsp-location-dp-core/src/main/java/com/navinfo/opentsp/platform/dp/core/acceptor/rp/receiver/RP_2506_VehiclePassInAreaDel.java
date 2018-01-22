package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCVehiclePassInAreaDel.VehiclePassInAreaDel;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCDeleteRegularData.DeleteRegularData;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;

@RPAnno(id="2506")
public class RP_2506_VehiclePassInAreaDel extends RPCommand {
	
	@Override
	public int processor(Packet packet) {
		try {
			VehiclePassInAreaDel vehiclePassInAreaDel = VehiclePassInAreaDel.parseFrom(packet.getContent());
			
			DeleteRegularData.Builder DeleteRegularDataBuilder = DeleteRegularData.newBuilder();
			DeleteRegularDataBuilder.setRegularCode(RegularCode.vehiclePassStatistic);
			for(long identify:vehiclePassInAreaDel.getAreaIdentifysList()){
				DeleteRegularDataBuilder.addRegularIdentify(identify);
			}
			
			Packet _out_packet = new Packet(true);
			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
			_out_packet.setCommand(AllCommands.DataAccess.DeleteRegularData_VALUE);
			_out_packet.setContent(DeleteRegularDataBuilder.build().toByteArray());
			
			int result= super.writeToDataAccess(_out_packet);
			if(result==0){
				this.commonResponses(1,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.failure_VALUE);
			}else{
				this.commonResponses(1,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);
			}
			
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return 0;
	}
	
}
