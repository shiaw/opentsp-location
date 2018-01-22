package com.navinfo.opentsp.platform.dp.core.handler;

import com.navinfo.opentsp.platform.dp.core.acceptor.da.DACommand;
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
 * @author yinsh
 * @version 1.0
 * @date 2016-09-19
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class DaProtocolDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(DaProtocolDispatcher.class);

    private final Map<String, DACommand> handlers;

    @Autowired
    public DaProtocolDispatcher(List<DACommand> handlerList) {
        Map<String, DACommand> map = new HashMap<>();
        for (DACommand handler : handlerList) {
            Class<? extends DACommand> clazz =handler.getClass();
            if (!clazz.isAnnotationPresent(DAAnno.class)) {
                continue;
            }
            DAAnno daAnno = clazz.getAnnotation(DAAnno.class);
            String key = daAnno.id();
            if(!StringUtils.isEmpty(daAnno.version())){
                key = daAnno.version()+"_"+daAnno.id();
            }
            logger.info("{} loading handler : {}",key, handler.getClass());
            map.put(key, handler);
        }
         logger.info("load proto count:{}",map.size());
        handlers = Collections.unmodifiableMap(map);
    }

    public DACommand getHandler(String cmdId) {
        return handlers.get(cmdId);
    }

}
