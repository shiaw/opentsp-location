package com.navinfo.opentsp.platform.dp.core.rule.status.common;

import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.redis.JacksonUtil;
import com.navinfo.opentsp.platform.dp.core.rule.status.RuleEum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;

@Component("rule_170110_Status")
public class Rule_170110_Status implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    

    @Resource
    private IRedisService redisService;// redis 通用dao
    
    public Map<Long, Rule_170110_StatusEntry> getBreakdownStatus()
    {
        return redisService.getAllKeys(RuleEum.R170110.getMapKey(), RuleEum.R170110.getClassType());
    }
    
    public Rule_170110_StatusEntry getRule_170110_StatusEntry(long terminalID)
    {
        String jsonResult = redisService.getHashValue(RuleEum.R170110.getMapKey(), terminalID, String.class);
        if (!"".equals(jsonResult) && jsonResult != null)
        {
            return (Rule_170110_StatusEntry)JacksonUtil.readValue(jsonResult, RuleEum.R170110.getClassType());
        }
        else
        {
            return new Rule_170110_StatusEntry();
        }
    }
    
    public void addBreakdownStatus(Rule_170110_StatusEntry entry)
    {
        redisService.hset(RuleEum.R170110.getMapKey(), entry.getTerminalID(), JacksonUtil.toJSon(entry));
    }
    
    public void deleteBreakdownStatus(long terminalID)
    {
        redisService.delete(RuleEum.R170110.getMapKey(), terminalID);
    }
    
    public static class Rule_170110_StatusEntry implements Serializable
    {
        public Rule_170110_StatusEntry()
        {
            super();
        }
        
        long terminalID;
        
        String breakdownCodeMd5;
        
        public long getTerminalID()
        {
            return terminalID;
        }
        
        public void setTerminalID(long terminalID)
        {
            this.terminalID = terminalID;
        }
        
        public String getBreakdownCodeMd5()
        {
            return breakdownCodeMd5;
        }
        
        public void setBreakdownCodeMd5(String breakdownCodeMd5)
        {
            this.breakdownCodeMd5 = breakdownCodeMd5;
        }
        
    }
}
