package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDeleteAreaInfo.DeleteAreaInfo;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCDeletePolygonAreaAlarm.DeletePolygonAreaAlarm;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.cache.TerminalCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.TerminalEntity;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;

import javax.annotation.Resource;
import java.util.List;

@RPAnno(id="2310")
public class RP_2310_DeletePolygonAreaAlarm extends RPCommand {


	@Resource
	AreaCache areaCache;

	@Resource
	RuleCache ruleCache;

	@Resource
	TerminalCache terminalCache;

	@Override
	public int processor(Packet packet) {
		try {
			
			log.info("[DP: PolygonAreaAlarm] 多边形区域规则Delete操作. 删除缓存与AreaCache中的多边形形区域对象， RuleCache中对应的规则对象.");
			//缓存删除
			DeletePolygonAreaAlarm dpaa = DeletePolygonAreaAlarm.parseFrom(packet.getContent());
			long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
			List<Long> list = dpaa.getAreaIdentifyList();
			for(long areaId : list) {
//				AreaCache.getInstance().removeAreaEntity(terminalId, areaId, AreaType.polygon);
//				RuleCache.getInstance().getRuleEntity(terminalId).removeRuleForArea(areaId, AreaType.polygon);
				areaCache.removeAreaEntity(terminalId, areaId, AreaType.polygon);
				ruleCache.getRuleEntity(terminalId).removeRuleForArea(areaId, AreaType.polygon);
			}
			
			//
			log.info("[DP: PolygonAreaAlarm] 对变形区域规则Delete操作. 给终端发送通用应答 [0x3001]");
			super.commonResponses(4,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);
			
			
			log.info("[DP: PolygonAreaAlarm] 多边形区域规则Delete操作. 给DA发送Dlete消息 [0x0931]");
			//构建DA数据
			DeleteAreaInfo.Builder builder = DeleteAreaInfo.newBuilder();
			builder.setTerminalId(terminalId);
			builder.addAllAreaIdentify(list);
			
			Packet _out_packet = new Packet(true);
			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
			_out_packet.setCommand(AllCommands.DataAccess.DeleteAreaInfo_VALUE);
			_out_packet.setContent(builder.build().toByteArray());
			super.writeToDataAccess(_out_packet);
		    
//		    TerminalEntity entity = TerminalCache.getInstance().getTerminal(packet.getFrom());
		    TerminalEntity entity = terminalCache.getTerminal(packet.getFrom());
		    if( entity != null ){
		    	if( entity.isRegularInTerminal() ){
				    packet.setTo(packet.getFrom());
				    super.writeToTermianl(packet);
		    	}
		    }
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}

}
