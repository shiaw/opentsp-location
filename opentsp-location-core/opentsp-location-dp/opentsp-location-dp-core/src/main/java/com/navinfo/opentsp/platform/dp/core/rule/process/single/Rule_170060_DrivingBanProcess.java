package com.navinfo.opentsp.platform.dp.core.rule.process.single;

import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRuleAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllAlarm.AlarmAddition;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.common.LCTerminalStatus.TerminalStatus;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDrivingBan.DrivingBan;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRegularProcess;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.RuleStatusCache;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.Rule_170060_Status;
import com.navinfo.opentsp.platform.dp.core.rule.tools.Help;

import javax.annotation.Resource;
import java.util.List;

/**
 * 规则编码：drivingBan	170060	禁驾报警
 * @author Administrator
 *
 */
@SingleRuleAnno(ruleId = RegularCode.drivingBan_VALUE)
public class Rule_170060_DrivingBanProcess extends SingleRegularProcess {
	@Resource
	private RuleStatusCache ruleStatusCache;

	private final int SPEED_THREHOLD = 5;
	@Override
	public GpsLocationDataEntity process(RuleEntity rule,
										 GpsLocationDataEntity gpsData) {
		List<DrivingBan> drivingBan = rule.getDrivingBanRule();
		// 是否有规则
		if (drivingBan == null) {
			return gpsData;
		}
		long terminalId = gpsData.getTerminalId();
		// 获取缓存状态
		Rule_170060_Status status = ruleStatusCache.getRuleStatus(RegularCode.drivingBan_VALUE, terminalId);
		if (status == null) {
			status = new Rule_170060_Status();
			ruleStatusCache.addRuleStatus(RegularCode.drivingBan_VALUE, terminalId, status);
		}
		if ((gpsData.getStatus() & TerminalStatus.acc_VALUE) != 0 && (gpsData.getSpeed() > SPEED_THREHOLD)){//ACC ON 并且有速度
			for(DrivingBan ban : drivingBan){
				boolean isDateLegal = Help.dateIsLegal( gpsData.getGpsDate(),
	                    ban.getStartDate(),
	                    ban.getEndDate(),
	                    ban.getIsEveryDay());
				if(isDateLegal){
					Integer counts = status.getBanCounts(ban.getBanIdentify());
					if(counts != null && counts.intValue() > 0){
						gpsData.updateAlarm(2, AlarmAddition.drivingBan_VALUE);
						counts++;
						status.putBanCounts(ban.getBanIdentify(), counts);
					}else{
						status.putBanCounts(ban.getBanIdentify(), 1);
					}
				}else{
					status.reset(ban.getBanIdentify());
				}
			}
		}else{
			status.reset();
		}
		return gpsData;
	}
}
