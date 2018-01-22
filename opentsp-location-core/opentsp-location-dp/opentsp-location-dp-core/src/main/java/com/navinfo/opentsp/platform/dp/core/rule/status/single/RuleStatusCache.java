package com.navinfo.opentsp.platform.dp.core.rule.status.single;

import com.navinfo.opentsp.platform.dp.core.common.Constant;
import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.redis.JacksonUtil;
import com.navinfo.opentsp.platform.dp.core.rule.RuleStatus;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 规则状态缓存<br>
 * key=规则编码_终端ID;value=规则状态
 * @author yinsh
 *
 */
@Component("ruleStatusCache")
public class RuleStatusCache {
	@Resource
	private IRedisService redisService;
	

	private RuleStatusCache(){}

	public void addRuleStatus(int ruleCode , long terminalId , RuleStatus ruleStatus){
		redisService.hset(Constant.RULE_SATUS_CACHE_KEY, ruleCode + "_" + terminalId, ruleStatus);
	}
	
	public void delKey(int ruleCode , long terminalId){
		redisService.delete(Constant.RULE_SATUS_CACHE_KEY, ruleCode + "_" + terminalId);
	}

	public <T> T getRuleStatus(int ruleCode , long terminalId){
		String result = redisService.hget(Constant.RULE_SATUS_CACHE_KEY, ruleCode + "_" + terminalId);
		if(result != null) {
			return (T) JacksonUtil.readValue(result, RuleStatus.class);
		}
		return null;
	}

	public void alarmHandle(long terminalId) {
		this.handle(RegularCode.inOutArea_VALUE, terminalId);
		this.handle(RegularCode.routeDriverTime_VALUE, terminalId);
		this.handle(RegularCode.driverNotCard_VALUE, terminalId);
		this.handle(RegularCode.doorOpenOutArea_VALUE, terminalId);
		this.handle(RegularCode.drivingBan_VALUE, terminalId);
	}
	private void handle(int ruleCode , long terminalId){
		RuleStatus ruleStatus = redisService.getHashValue(Constant.RULE_SATUS_CACHE_KEY, ruleCode + "_" + terminalId, RuleStatus.class);
		if(ruleStatus != null)
			ruleStatus.alarmHandle();
	}
}
