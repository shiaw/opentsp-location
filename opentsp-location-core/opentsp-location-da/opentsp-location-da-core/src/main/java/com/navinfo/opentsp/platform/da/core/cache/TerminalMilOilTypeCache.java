package com.navinfo.opentsp.platform.da.core.cache;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerMilOilTypeDBEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 修伟 on 2017/7/28 0028.
 */
public class TerminalMilOilTypeCache {
    private final static TerminalMilOilTypeCache instance = new TerminalMilOilTypeCache();
    private static Map<Long,LcTerMilOilTypeDBEntity> cache = new HashMap<>();
    private TerminalMilOilTypeCache(){};
    public static TerminalMilOilTypeCache getInstance(){return instance;}
    public void addCache(LcTerMilOilTypeDBEntity entity){
        cache.put(entity.getTerminal_id(),entity);
    }
    public LcTerMilOilTypeDBEntity getValue(long tid){
        return cache.get(tid);
    }
}
