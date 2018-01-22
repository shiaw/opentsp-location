package com.navinfo.opentsp.platform.da.core.cache;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/8/2
 * @modify 缓存下发指令命令UUID
 * @copyright opentsp
 */
@Component
public class TerminalIdCache {

    private static final Logger logger = LoggerFactory.getLogger(TerminalIdCache.class);

    private final static TerminalIdCache instance = new TerminalIdCache();

    private static List<Long> cache = new ArrayList<>();

    private TerminalIdCache() {
    }
    public static TerminalIdCache getInstance() {
        return instance;
    }

    /**
     * 添加数据
     *
     */
    public void add(List<Long> caches){
        cache = caches;
    }

    public List<Long> getAll(){
            return cache;
    }
}
