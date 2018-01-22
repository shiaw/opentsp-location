package com.navinfo.opentsp.platform.monitor.persisted;

import com.navinfo.opentsp.platform.monitor.LastestMonitorCommand;
import com.navinfo.opentsp.platform.monitor.common.WsService;
import com.navinfo.opentsp.platform.monitor.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by machi on 2017/6/27.
 */
@Service
public class LastestMonitorRepository {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${opentsp.monitor.timeOut:90000}")
    private long timeOut;

    private long currenttime = 0;

    public LastestMonitorCommand.Result query(String terminalId) {
        LastestMonitorCommand.Result result = new LastestMonitorCommand.Result();
        result.setResultCode(10003);
        result.setMessage("未调用方法");
        try {
            WsService wsService = new WsService();
            List<Long> terminalIds = new ArrayList<Long>();
            terminalIds.add(Long.parseLong(terminalId));
            int flag = wsService.getTerminalRegular(terminalIds);
            if (flag == 1) {
                logger.debug("接口调用正常，验证末次位置存储时间");
                List<ServiceInstance> list = discoveryClient.getInstances("OPENTSP-LOCATION-DA");
                logger.debug("da-core数量：" + list.size());

                // jedis = RedisClusters.getInstance().getJedis();
                StringBuffer sb = new StringBuffer();
                sb.append("da在eureka注册的数量：" + list.size() + "台   ---   ");
                for (ServiceInstance si : list) {
//                    ValueOperations<String, Object> value = redisTemplate.opsForValue();
                    String redisTime = redisTemplate.opsForValue().get(si.getHost());   //jedis.get(si.getHost());
                    currenttime = System.currentTimeMillis();
                    long chao = currenttime - Long.parseLong(redisTime);
                    if (chao > timeOut) {
                        result.setResultCode(10002);
                        sb.append("末次位置存储超时，超时daHost：" + si.getHost() + ",超时时长：" + DateUtils.formatTime(chao - timeOut) + "   ---   ");
                    } else {
                        result.setResultCode(10000);
                        sb.append("末次位置存正常，daHost：" + si.getHost() + ",时长：" + DateUtils.formatTime(chao));
                    }
                }
                result.setMessage(sb.toString());
                return result;
            } else if (flag == 0) {
                logger.debug("webService接口调用失败");
                result.setResultCode(10001);
                result.setMessage("webService接口调用失败");
                return result;
            }
        } catch (Exception e) {
            result.setResultCode(10001);
            result.setMessage("webService接口调用异常");
            logger.error("webService调用失败:  " + e);
        }
        return result;
    }
}
