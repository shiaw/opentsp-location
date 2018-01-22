package com.navinfo.opentsp.platform.dp.core.rule.handler.common;

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
 * Time: 上午10:26
 */
@Component
public class CommonRegularDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(CommonRegularDispatcher.class);

    private final Map<Integer, CommonRegularProcess> handlers;

    @Autowired
    public CommonRegularDispatcher(List<CommonRegularProcess> handlerList) {
        Map<Integer, CommonRegularProcess> map = new HashMap<>();
        for (CommonRegularProcess handler : handlerList) {
            Class<? extends CommonRegularProcess> clazz =handler.getClass();
            if (!clazz.isAnnotationPresent(CommonRuleAnno.class)) {
                continue;
            }
            CommonRuleAnno anno = clazz.getAnnotation(CommonRuleAnno.class);
            int ruleId = anno.ruleId();
            logger.info("{} loading handler : {}",ruleId, handler.getClass());
            map.put(anno.ruleId(), handler);
        }
        handlers = Collections.unmodifiableMap(map);
    }

    public CommonRegularProcess getCommonRegular(int cmdId) {
        return handlers.get(cmdId);
    }
}