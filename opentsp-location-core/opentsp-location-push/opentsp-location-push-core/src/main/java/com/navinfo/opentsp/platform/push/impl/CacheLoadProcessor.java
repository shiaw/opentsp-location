package com.navinfo.opentsp.platform.push.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/8/10
 * @modify
 * @copyright opentsp
 */
@Component
public class CacheLoadProcessor implements BeanPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(CacheLoadProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        if(o instanceof TerminalServiceImpl){
            LOG.info("执行加载终端数据.....................");
          //  ((TerminalServiceImpl) o).loadTerminal();
        }
        return o;
    }
}
