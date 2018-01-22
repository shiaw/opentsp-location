package com.navinfo.opentsp.platform.dp.core.rule.handler.single;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: zhanhk
 * Date: 16/10/21
 * Time: 上午9:36
 */
@Component
public class SingleRegularDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(SingleRegularDispatcher.class);

    private final Map<Integer, SingleRegularProcess> handlers;

    @Autowired
    public SingleRegularDispatcher(List<SingleRegularProcess> handlerList) {
        Map<Integer, SingleRegularProcess> map = new HashMap<>();
        for (SingleRegularProcess handler : handlerList) {
            Class<? extends SingleRegularProcess> clazz =handler.getClass();
            if (!clazz.isAnnotationPresent(SingleRuleAnno.class)) {
                continue;
            }
            SingleRuleAnno anno = clazz.getAnnotation(SingleRuleAnno.class);
            int ruleId = anno.ruleId();
            logger.info("{} loading handler : {}",ruleId, handler.getClass());
            map.put(anno.ruleId(), handler);
        }
        handlers = Collections.unmodifiableMap(map);
    }

    public SingleRegularProcess getCommonRegular(int cmdId) {
        return handlers.get(cmdId);
    }
}
