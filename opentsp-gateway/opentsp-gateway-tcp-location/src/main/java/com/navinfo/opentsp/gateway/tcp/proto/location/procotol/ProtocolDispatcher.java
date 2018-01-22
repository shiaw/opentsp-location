package com.navinfo.opentsp.gateway.tcp.proto.location.procotol;

import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
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
public class ProtocolDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolDispatcher.class);

    private final Map<String, Command> handlers;

    @Autowired
    public ProtocolDispatcher(List<Command> handlerList) {
        Map<String, Command> map = new HashMap<>();
        for (Command handler : handlerList) {
            Class<? extends Command> clazz =handler.getClass();
            if (!clazz.isAnnotationPresent(LocationCommand.class)) {
                continue;
            }
            LocationCommand locationCommand = clazz.getAnnotation(LocationCommand.class);
           String key=locationCommand.id();
            if(!StringUtils.isEmpty(locationCommand.version())){
                key=locationCommand.version()+"_"+locationCommand.id();
            }
            logger.info("{} loading handler : {}",key, handler.getClass());

            map.put(key, handler);
        }
         logger.info("load proto count:{}",map.size());
        handlers = Collections.unmodifiableMap(map);
    }

    public Command getHandler(String cmdId) {
        return handlers.get(cmdId);
    }

}
