package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.RuleStatusCache;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.Rule_170160_Status;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCDeleteRegularData;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
//import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@RPAnno(id="2320")
public class RP_2320_InAreaTriggerActivationOrLockNotifyDel extends RPCommand {

	@Resource
	AreaCache areaCache;

	@Resource
	RuleCache ruleCache;

	@Resource
	private Rule_170160_Status rule_170160_status;


	@Override
	public int processor(Packet packet) {
		long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());

//		AreaCache.getInstance().removeAreaEntity(terminalId);
//		RuleCache.getInstance().removeRuleEntity(terminalId);
		areaCache.removeAreaEntity(terminalId);
		ruleCache.removeRuleEntity(terminalId);
		List<Long> values = new ArrayList<Long>();

		//构建DA数据
		LCDeleteRegularData.DeleteRegularData.Builder regularDelBuilder = LCDeleteRegularData.DeleteRegularData.newBuilder();
		regularDelBuilder.setTerminalId(terminalId);
		regularDelBuilder.setRegularCode(LCRegularCode.RegularCode.inAreaTriggerActivationOrLockNotify);
		regularDelBuilder.addAllRegularIdentify(values);

		Packet _out_packet = new Packet(true);
		_out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
		_out_packet.setCommand(AllCommands.DataAccess.DeleteRegularData_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
		_out_packet.setContent(regularDelBuilder.build().toByteArray());
		super.writeToDataAccess(_out_packet);
//		RuleStatusCache.getInstance().delKey(RegularCode.inAreaTriggerActivationOrLockNotify_VALUE, terminalId);
		rule_170160_status.del(terminalId);
		super.commonResponses(4,packet.getFrom(), packet.getSerialNumber(), packet.getCommand(), PlatformResponseResult.success_VALUE);
		//super.terminalOperateLog(packet.getFrom(), AllCommands.Terminal.TerminalShutdown_VALUE, packet.getContent());
		return 0;
	}
}
