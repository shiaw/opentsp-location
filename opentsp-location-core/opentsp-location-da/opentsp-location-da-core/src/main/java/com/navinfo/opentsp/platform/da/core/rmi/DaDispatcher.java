package com.navinfo.opentsp.platform.da.core.rmi;

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
 * @author wzw
 * 2016年9月20日14:13:30
 */
@Component
public class DaDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(DaDispatcher.class);

    private final Map<String, Dacommand> handlers;

    @Autowired
    public DaDispatcher(List<Dacommand> handlerList) {
        Map<String, Dacommand> map = new HashMap<>();
        for (Dacommand handler : handlerList) {
            Class<? extends Dacommand> clazz = handler.getClass();
            if (!clazz.isAnnotationPresent(DaRmiNo.class)) {
                continue;
            }
            DaRmiNo daAnno = clazz.getAnnotation(DaRmiNo.class);
            String key = daAnno.id();
            if (!StringUtils.isEmpty(daAnno.version())) {
                key = daAnno.version() + "_" + daAnno.id();
            }
            logger.info("{} loading handler : {}", key, handler.getClass());
            map.put(key, handler);
        }
        logger.info("load proto count:{}", map.size());
        handlers = Collections.unmodifiableMap(map);
    }

    public Dacommand getHandler(String cmdId) {
        return handlers.get(cmdId);
    }

}
