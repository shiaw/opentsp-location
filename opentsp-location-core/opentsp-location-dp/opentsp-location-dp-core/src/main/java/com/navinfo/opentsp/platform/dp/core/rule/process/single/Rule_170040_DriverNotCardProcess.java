package com.navinfo.opentsp.platform.dp.core.rule.process.single;

import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRegularProcess;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRuleAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;

/**
 * 规则编码：driverNotCard	170040	驾驶员未刷卡报警
 * @author Administrator
 *
 */
@SingleRuleAnno(ruleId = RegularCode.driverNotCard_VALUE)
public class Rule_170040_DriverNotCardProcess extends SingleRegularProcess {

	@Override
	public GpsLocationDataEntity process(RuleEntity rule, GpsLocationDataEntity dataEntity) {
		return null;
	}

}
