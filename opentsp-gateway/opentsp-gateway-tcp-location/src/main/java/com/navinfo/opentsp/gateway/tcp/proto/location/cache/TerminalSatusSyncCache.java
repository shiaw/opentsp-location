package com.navinfo.opentsp.gateway.tcp.proto.location.cache;

import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalStateSync;
import com.navinfo.opentsp.gateway.tcp.proto.location.redis.Contants;
import com.navinfo.opentsp.gateway.tcp.proto.location.redis.IRedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author：修伟 on 2017/9/21 0021.
 */
@Component
public class TerminalSatusSyncCache {
    private static Logger logger = LoggerFactory.getLogger(TerminalSatusSyncCache.class);
    // redis 通用dao
    @Autowired
    private IRedisService redisService;

    private static Map<String,TerminalStateSync> cache = new ConcurrentHashMap<String,TerminalStateSync>();

    private static TerminalSatusSyncCache instance = new TerminalSatusSyncCache();

    public static TerminalSatusSyncCache getInstance(){
        return instance;
    }

    public TerminalStateSync get(String key){
        TerminalStateSync sync = cache.get(key);
        if (sync == null){
            sync = redisService.getHashValue(Contants.TerminalStatusSync,key,TerminalStateSync.class);
            if (sync!=null){
                cache.put(key,sync);
            }
        }
        return sync;
    }

    public void add(String key,TerminalStateSync sync){
        cache.put(key,sync);
    }

    public void delete(String key){
        cache.remove(key);
    }

    public Map<String,TerminalStateSync> getCache(){
        return cache;
    }

    /**
     * 从redis加载所有积分里程油耗到内存中
     * */
    public void reload(){
        Map<String, TerminalStateSync> map = redisService.getAllKeys(Contants.TerminalStatusSync, TerminalStateSync.class);
        if (map!=null && map.size()>0){
            Set<Map.Entry<String, TerminalStateSync>> entries = map.entrySet();
            int succCount = 0;
            int errorCount = 0;
            for (Map.Entry<String,TerminalStateSync> syncEntry : entries){
                try{
                    add(syncEntry.getKey(),syncEntry.getValue());
                    succCount++;
                }
                catch(Exception e){
                    errorCount++;
                }
            }
            logger.info("系统启动时从redis加载积分里程油耗成功{}条,失败{}条",succCount,errorCount);
        }
    }


}
