package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DpProtocolDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(DpProtocolDispatcher.class);

    private final Map<String, PushCommand> handlers;

    @Autowired
    public DpProtocolDispatcher(List<PushCommand> handlerList) {
        Map<String, PushCommand> map = new HashMap<>();
        for (PushCommand handler : handlerList) {
            Class<? extends PushCommand> clazz = handler.getClass();
            if (!clazz.isAnnotationPresent(DPAnno.class)) {
                continue;
            }
            DPAnno rpAnno = clazz.getAnnotation(DPAnno.class);
            String key = rpAnno.id();
            if (!StringUtils.isEmpty(rpAnno.version())) {
                key = rpAnno.version() + "_" + rpAnno.id();
            }
            logger.info("{} loading handler : {}", key, handler.getClass());
            map.put(key, handler);
        }
        logger.info("load proto count:{}", map.size());
        handlers = Collections.unmodifiableMap(map);
    }

    public PushCommand getHandler(String cmdId) {
        return handlers.get(cmdId);
    }

}
