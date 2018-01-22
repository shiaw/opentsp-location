package com.navinfo.opentsp.platform.dp.core.handler;

import com.navinfo.opentsp.platform.dp.core.acceptor.rp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016-08-10
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class RpProtocolDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(RpProtocolDispatcher.class);

    private final Map<String, RPCommand> handlers;

    @Autowired
    public RpProtocolDispatcher(List<RPCommand> handlerList) {
        Map<String, RPCommand> map = new HashMap<>();
        for (RPCommand handler : handlerList) {
            Class<? extends RPCommand> clazz =handler.getClass();
            if (!clazz.isAnnotationPresent(RPAnno.class)) {
                continue;
            }
            RPAnno rpAnno = clazz.getAnnotation(RPAnno.class);
            String key = rpAnno.id();
            if(!StringUtils.isEmpty(rpAnno.version())){
                key = rpAnno.version()+"_"+rpAnno.id();
            }
            logger.info("{} loading handler : {}",key, handler.getClass());
            map.put(key, handler);
        }
         logger.info("load proto count:{}",map.size());
        handlers = Collections.unmodifiableMap(map);
    }

    public RPCommand getHandler(String cmdId) {
        return handlers.get(cmdId);
    }

}
