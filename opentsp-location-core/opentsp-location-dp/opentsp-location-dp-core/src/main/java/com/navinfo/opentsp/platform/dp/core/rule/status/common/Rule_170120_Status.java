package com.navinfo.opentsp.platform.dp.core.rule.status.common;

import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.redis.JacksonUtil;
import com.navinfo.opentsp.platform.dp.core.rule.entity.OilStatus;
import com.navinfo.opentsp.platform.dp.core.rule.status.RuleEum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;

@Component("rule_170120_Status")
public class Rule_170120_Status implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Resource
    private IRedisService redisService;// redis 通用dao
    
    public void addOilStatus(Long terminalId, OilStatus status)
    {
        redisService.hset(RuleEum.R170120.getMapKey(), terminalId, JacksonUtil.toJSon(status));
    }
    
    public void delOilStatusByTerminalId(Long terminalId)
    {
        redisService.delete(RuleEum.R170120.getMapKey(), terminalId);
    }
    
    public Map<Long, OilStatus> getOilStatus()
    {
        return redisService.getAllKeys(RuleEum.R170120.getMapKey(), RuleEum.R170120.getClassType());
    }
    
    public OilStatus getOilStatus(long terminalId)
    {
        return (OilStatus)redisService.getHashValue(RuleEum.R170120.getMapKey(),
            terminalId,
            RuleEum.R170120.getClassType());
    }
}
