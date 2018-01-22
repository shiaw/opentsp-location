package com.navinfo.opentsp.platform.dsa.cache;

import com.navinfo.opentsp.platform.dsa.rmiclient.RMIConnectCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TerminalsCache {

    private Logger logger = LoggerFactory.getLogger(TerminalsCache.class);
    private volatile List<Long> allTerminals = new ArrayList<Long>();

    private Object mux = new Object();
    @Autowired
    private RMIConnectCache rMIConnectCache;
    /*
     * 初始化方法，从da拉取数据，并初始化到本地缓存
     */
    private void init() {
        try {
            logger.info("开始加载[终端缓存]...");
            long ss = System.currentTimeMillis();
            allTerminals = rMIConnectCache.getAllTerminalInfo();
            if (allTerminals.size() > 0) {
                logger.info("加载[终端缓存]成功,终端个数为:{}[耗时:{}]", allTerminals.size(),
                        (System.currentTimeMillis() - ss) / 1000L);
            } else {
                logger.warn("加载[终端缓存]出错，加载结果为空，请检查[耗时:{}]", (System.currentTimeMillis() - ss) / 1000L);
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public List<Long> getAllTerminals() {
        if (allTerminals.size() < 1) {
            synchronized (mux) {
                if (allTerminals.size() < 1) {
                    init();
                }
            }
        }

        return allTerminals;
    }

    public void reload() {
        allTerminals.clear();
        getAllTerminals();
    }

}
