package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDeleteAreaInfo.DeleteAreaInfo;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCDeleteCircleAreaAlarm.DeleteCircleAreaAlarm;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.cache.TerminalCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.TerminalEntity;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@RPAnno(id = "2306")
@Component
public class RP_2306_DeleteCircleAreaAlarm extends RPCommand {

	@Resource
	AreaCache areaCache;

	@Resource
	RuleCache ruleCache;

	@Resource
	TerminalCache terminalCache;

	@Override
	public int processor(Packet packet) {
		try {
			//本地删除缓存
			DeleteCircleAreaAlarm dcaa = DeleteCircleAreaAlarm.parseFrom(packet.getContent());
			long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
			List<Long> list = dcaa.getAreaIdentifyList();
			//缓存管理
			for(long areaId : list) {
				areaCache.removeAreaEntity(terminalId, areaId, AreaType.circle);
				ruleCache.getRuleEntity(terminalId).removeRuleForArea(areaId, AreaType.circle);
			}
			super.commonResponses(4,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);
			//构建DA数据
			DeleteAreaInfo.Builder builder = DeleteAreaInfo.newBuilder();
			builder.setTerminalId(terminalId);
			builder.addAllAreaIdentify(list);
			
			Packet _out_packet = new Packet(true);
			_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
			_out_packet.setCommand(AllCommands.DataAccess.DeleteAreaInfo_VALUE);
			_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
			_out_packet.setContent(builder.build().toByteArray());
			super.writeToDataAccess(_out_packet);
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
