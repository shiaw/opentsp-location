package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.RuleStatusCache;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCUpCommonRes.UpCommonRes;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;

import javax.annotation.Resource;

@RPAnno(id="2001")
public class RP_2001_UpCommonRes extends RPCommand {

	@Resource
	private RuleStatusCache ruleStatusCache;

	@Override
	public int processor(Packet packet) {
		try {
			UpCommonRes commonRes = UpCommonRes.parseFrom(packet.getContent());
			if (commonRes.getResponseId() == AllCommands.Terminal.ReportLocationData_VALUE
					&& commonRes.getResult().getNumber() == ResponseResult.alarmHandle_VALUE) {
//				RuleStatusCache.getInstance().alarmHandle(packet.getFrom());
				ruleStatusCache.alarmHandle(packet.getFrom());
			}
			super.writeToTermianl(packet);
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
