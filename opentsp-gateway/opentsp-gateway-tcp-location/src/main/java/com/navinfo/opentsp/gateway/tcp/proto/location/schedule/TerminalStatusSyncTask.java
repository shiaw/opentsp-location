package com.navinfo.opentsp.gateway.tcp.proto.location.schedule;

import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TerminalSatusSyncCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalStateSync;
import com.navinfo.opentsp.gateway.tcp.proto.location.redis.Contants;
import com.navinfo.opentsp.gateway.tcp.proto.location.redis.IRedisService;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnections;
import com.navinfo.opentspcore.common.gateway.ClientConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author修伟 on 2017/9/22 0022.
 * @modified by chenjie 2017-12-27
 */
@Component
@Configurable
@EnableScheduling
public class TerminalStatusSyncTask {

    private static final Logger log = LoggerFactory.getLogger(TerminalStatusSyncTask.class);

    @Autowired
    TerminalSatusSyncCache syncCache;

    // redis 通用dao
    @Resource
    private IRedisService redisService;

    @Autowired
    private NettyClientConnections connections;


    @Scheduled(cron = "${terminalstatus.sync.schedule.cron:0 */2 * * * ?}")
    public void saveTerminalStatusSyncCache(){
        long time = System.currentTimeMillis()/1000;
        Map<String,TerminalStateSync> cache = syncCache.getCache();
        if (cache.size()>0){
            int i = 0;
            for (Map.Entry<String,TerminalStateSync> syncEntry : cache.entrySet()){
                String vin = syncEntry.getKey();
                ClientConnection clientConnection = connections.getConnection(vin);
                if(clientConnection == null){
                    syncCache.delete(vin);
                    continue;
                }

                TerminalStateSync sync = syncEntry.getValue();
                if (time-sync.getGpsDate()<120){
                    redisService.hset(Contants.TerminalStatusSync,syncEntry.getKey(), sync);
                    i++;
                }
            }
            log.error("[{}]条积分里程油耗保存到redis.",i);
        }
    }
}
