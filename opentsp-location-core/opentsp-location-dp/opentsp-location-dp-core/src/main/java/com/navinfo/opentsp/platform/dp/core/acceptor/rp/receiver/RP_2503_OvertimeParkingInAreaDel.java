package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCDeleteRegularData;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCOvertimeParkingInAreaDel.OvertimeParkingInAreaDel;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCommonCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCommonCache;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;

import javax.annotation.Resource;
import java.util.List;

@RPAnno(id="2503")
public class RP_2503_OvertimeParkingInAreaDel extends RPCommand {



	@Resource
	AreaCommonCache areaCommonCache;

	@Resource
	RuleCommonCache ruleCommonCache;

	@Override
	public int processor(Packet packet) {
		try {
			//本地删除缓存
			OvertimeParkingInAreaDel overtimeParkingInAreaDel = OvertimeParkingInAreaDel.parseFrom(packet.getContent());
			List<Long> areaIds = overtimeParkingInAreaDel.getAreaIdentifysList();
			for(Long areaId : areaIds){
//				AreaCommonCache.getInstance().removeAreaEntity(areaId);
//				RuleCommonCache.getInstance().removeRuleEntityByAreaId(170090, areaId);
				areaCommonCache.removeAreaEntity(areaId);
				ruleCommonCache.removeRuleEntityByAreaId(170090, areaId);
				//构建DA数据
				if(overtimeParkingInAreaDel.getSaveSign()){
					
					LCDeleteRegularData.DeleteRegularData.Builder delBuilder = LCDeleteRegularData.DeleteRegularData.newBuilder();
					delBuilder.setTerminalId(0);
					delBuilder.setRegularCode(LCRegularCode.RegularCode.overtimePark);
					delBuilder.addRegularIdentify(areaId);
					
					Packet _out_packet = new Packet(true);
					_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
					_out_packet.setCommand(AllCommands.DataAccess.DeleteAreaInfo_VALUE);
					_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
					_out_packet.setContent(delBuilder.build().toByteArray());
					super.writeToDataAccess(_out_packet);
					
					//返回1100
					this.commonResponses(1,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);
					
					//返回1100
//					log.info("[DP->RP] 返回通用应答. 0x1100");
//					Packet _CommonRes_packet = new Packet(true);
//					ServerCommonRes.Builder _CommonRes_packetbuilder = ServerCommonRes.newBuilder();
//					_CommonRes_packetbuilder.setSerialNumber(packet.getSerialNumber());
//					_CommonRes_packetbuilder.setResponseId(AllCommands.Terminal.MessageBroadcastInArea_VALUE);
////					_CommonRes_packetbuilder.setResults(PlatformResponseResult.valueOf(result));
//					_CommonRes_packetbuilder.setResults(PlatformResponseResult.success);
//
//					
//					_CommonRes_packet.setCommand(AllCommands.Platform.ServerCommonRes_VALUE);
//					_CommonRes_packet.setProtocol(LCMessageType.PLATFORM);
//					_CommonRes_packet.setContent(_CommonRes_packetbuilder.build().toByteArray());
//					// 将唯一标识设置为当前节点标识
//					_CommonRes_packet.setSerialNumber(packet.getSerialNumber());
//					_CommonRes_packet.setUniqueMark(packet.getUniqueMark());
//					_CommonRes_packet.setFrom(NodeHelper.getNodeCode());
//					_CommonRes_packet.setTo(packet.getFrom());
//					super.write(_CommonRes_packet);
					
				}
			}
			
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
		return 0;
	}
}
