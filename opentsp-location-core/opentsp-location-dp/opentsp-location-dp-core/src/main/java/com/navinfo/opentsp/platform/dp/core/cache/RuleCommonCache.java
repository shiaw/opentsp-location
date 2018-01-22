package com.navinfo.opentsp.platform.dp.core.cache;

import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.Constant;
import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用规则缓存<br>
 * RuleCommonCache，key=规则编码，实体是List<RuleEntity>
 *
 * @author
 */
@Component("ruleCommonCache")
public class RuleCommonCache {
    private static Map<Long, List<RuleEntity>> cache = new ConcurrentHashMap<Long, List<RuleEntity>>();
    private static RuleCommonCache instance = new RuleCommonCache();

    private RuleCommonCache() {
    }

    public static RuleCommonCache getInstance() {
        return instance;
    }

    public void addRuleEntity(RuleEntity ruleEntity) {
        long regularCode = ruleEntity.getRegularCode().getNumber();
        if(cache.containsKey(regularCode)) {
            List<RuleEntity> ruleList = cache.get(regularCode);
            if(ruleList != null) {
                for(Iterator<RuleEntity> it = ruleList.iterator();it.hasNext();) {
                    RuleEntity rule = it.next();
                    if(rule != null) {
                        if(rule.getAreaIdentify() ==  ruleEntity.getAreaIdentify()) {
                            it.remove();
//                        ruleList.remove(rule);
                        }
                    }
                }
                ruleList.add(ruleEntity);
            }else {
                List<RuleEntity> ruleEntityList = new ArrayList<RuleEntity>();
                cache.put(regularCode, ruleEntityList);
                ruleEntityList.add(ruleEntity);
            }
        } else {
            List<RuleEntity> ruleEntityList = new ArrayList<RuleEntity>();
            cache.put(regularCode, ruleEntityList);
            ruleEntityList.add(ruleEntity);
        }
    }

    public void removeRuleEntityByAreaId(long regularCode,long areaId) {
        if(cache.containsKey(regularCode)){
            List<RuleEntity> ruleList = cache.get(regularCode);
            if(ruleList != null) {
                for(Iterator<RuleEntity> it = ruleList.iterator();it.hasNext();) {
                    RuleEntity rule = it.next();
                    if(rule.getAreaIdentify() ==  areaId) {
                        it.remove();
                        ruleList.remove(rule);
                    }
                }
            }
        }
    }


    public  List<RuleEntity> getRuleEntity(long regularCode) {
        return cache.get(regularCode);
    }


    public boolean removeRuleEntity(long regularCode) {
        boolean isRemove = false;
        isRemove = cache.remove(regularCode) != null ;
        cache.put(regularCode, new ArrayList<RuleEntity>());
        return isRemove;
    }
    public int size(){
        return cache.size();
    }

}
