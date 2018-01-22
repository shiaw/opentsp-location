package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCDeleteRegularData;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCDownCommonRes;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCOutRegionToLimitSpeedDel.OutRegionToLimitSpeedDel;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.LCConstant.LCMessageType;
import com.navinfo.opentsp.platform.location.kit.Packet;

import javax.annotation.Resource;
import java.util.List;

@RPAnno(id="2318")
public class RP_2318_OutRegionToLimitSpeedDel extends RPCommand {

	@Resource
	AreaCache areaCache;

	@Resource
	RuleCache ruleCache;

	@Override
	public int processor(Packet packet) {
		try {
			//本地删除缓存
			OutRegionToLimitSpeedDel orlsDel = OutRegionToLimitSpeedDel.parseFrom(packet.getContent());
			long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
			
			List<Long> areaIds =orlsDel.getAreaIdentifysList();
			for(Long areaId : areaIds){
//				AreaCache.getInstance().removeAreaEntity(terminalId, areaId, AreaType.circle);
//				RuleCache.getInstance().getRuleEntity(terminalId).removeRuleForArea(areaId, AreaType.circle);
				areaCache.removeAreaEntity(terminalId, areaId, AreaType.circle);
				ruleCache.getRuleEntity(terminalId).removeRuleForArea(areaId, AreaType.circle);
			}
			
//			super.commonResponses(4,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);

			//构建DA数据
			LCDeleteRegularData.DeleteRegularData.Builder regularDelBuilder = LCDeleteRegularData.DeleteRegularData.newBuilder();
			regularDelBuilder.setTerminalId(terminalId);
			regularDelBuilder.setRegularCode(LCRegularCode.RegularCode.outregionToLSpeed);
			regularDelBuilder.addAllRegularIdentify(orlsDel.getAreaIdentifysList());

			Packet _out_packet = new Packet(true);
			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
			_out_packet.setCommand(AllCommands.DataAccess.DeleteRegularData_VALUE);
			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
			_out_packet.setContent(regularDelBuilder.build().toByteArray());
			super.writeToDataAccess(_out_packet);
			
			super.commonResponses(4,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);

			//3001应答
			log.info("[DP->RP] 返回通用应答. 0x3001");
			Packet _3001_packet = new Packet(true);
			LCDownCommonRes.DownCommonRes.Builder resBuilder = LCDownCommonRes.DownCommonRes.newBuilder();
			resBuilder.setSerialNumber(packet.getSerialNumber());
			resBuilder.setResponseId(AllCommands.Terminal.OutRegionToLimitSpeedDel_VALUE);
			resBuilder.setResult(ResponseResult.success);
			
			_3001_packet.setCommand(AllCommands.Terminal.DownCommonRes_VALUE);
			_3001_packet.setProtocol(LCMessageType.TERMINAL);
			_3001_packet.setContent(resBuilder.build().toByteArray());
			_3001_packet.setSerialNumber(packet.getSerialNumber());
			_3001_packet.setUniqueMark(packet.getUniqueMark());
			_3001_packet.setFrom(NodeHelper.getNodeCode());
			_3001_packet.setTo(100310);
			super.write(_3001_packet);
		    //返回1100
//			log.info("[DP->RP] 返回通用应答. 0x1100");
//			Packet _CommonRes_packet = new Packet(true);
//			ServerCommonRes.Builder _CommonRes_packetbuilder = ServerCommonRes.newBuilder();
//			_CommonRes_packetbuilder.setSerialNumber(packet.getSerialNumber());
//			_CommonRes_packetbuilder.setResponseId(AllCommands.Terminal.OutRegionToLimitSpeedDel_VALUE);
////			_CommonRes_packetbuilder.setResults(PlatformResponseResult.valueOf(result));
//			_CommonRes_packetbuilder.setResults(PlatformResponseResult.success);
//
//			
//			_CommonRes_packet.setCommand(AllCommands.Platform.ServerCommonRes_VALUE);
//			_CommonRes_packet.setProtocol(LCMessageType.PLATFORM);
//			_CommonRes_packet.setContent(_CommonRes_packetbuilder.build().toByteArray());
//			// 将唯一标识设置为当前节点标识
//			_CommonRes_packet.setSerialNumber(packet.getSerialNumber());
//			_CommonRes_packet.setUniqueMark(packet.getUniqueMark());
//			_CommonRes_packet.setFrom(NodeHelper.getNodeCode());
//			_CommonRes_packet.setTo(100310);
//			super.write(_CommonRes_packet);
			
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
		return 0;
	}

}
