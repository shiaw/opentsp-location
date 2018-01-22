package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDrivingBan.DrivingBan;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCDeleteRegularData.DeleteRegularData;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCDrivingBanSettingCancel.DrivingBanSettingCancel;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.LCConstant.LCMessageType;
import com.navinfo.opentsp.platform.location.kit.Packet;

import javax.annotation.Resource;
import java.util.List;

@RPAnno(id="2315")
public class RP_2315_DrivingBanSettingCancel extends RPCommand {


	@Resource
	RuleCache ruleCache;

	@Override
	public int processor(Packet packet) {
		try {
//			RuleEntity entity = RuleCache.getInstance().getRuleEntity(packet.getFrom());
			RuleEntity entity = ruleCache.getRuleEntity(packet.getFrom());
			DrivingBanSettingCancel banSettingCancel = DrivingBanSettingCancel.parseFrom(packet.getContent());
			DeleteRegularData.Builder builder = DeleteRegularData.newBuilder();
			builder.setRegularCode(RegularCode.drivingBan);
			builder.setTerminalId(packet.getFrom());
			if(banSettingCancel.getIsDeleteAll()){
				if(entity != null){
					List<DrivingBan> bans = entity.getDrivingBanRule();
					if(bans != null){
						for (DrivingBan drivingBan : bans) {
							builder.addRegularIdentify(drivingBan.getBanIdentify());
						}
					}
				}
			}else{
				builder.addAllRegularIdentify(banSettingCancel.getBanIdentifyList());
			}
			
			if( entity != null ){
				entity.removeDrivingBanRule(banSettingCancel.getIsDeleteAll(), banSettingCancel.getBanIdentifyList());
			}
			
			super.commonResponses(4,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);
			
			if( builder.build().getRegularIdentifyCount() > 0 ){
				Packet pk = new Packet(true);
				pk.setCommand(AllCommands.DataAccess.DeleteRegularData_VALUE);
				pk.setProtocol(LCMessageType.PLATFORM);
				pk.setUniqueMark(NodeHelper.getNodeUniqueMark());
				pk.setFrom(NodeHelper.getNodeCode());
				pk.setContent(builder.build().toByteArray());
				super.writeToDataAccess(pk);				
			}
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
