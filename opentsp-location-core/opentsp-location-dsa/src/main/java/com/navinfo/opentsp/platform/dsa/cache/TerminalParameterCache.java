package com.navinfo.opentsp.platform.dsa.cache;

import com.navinfo.opentsp.platform.dsa.rmiclient.DARmiClient;
import com.navinfo.opentsp.platform.dsa.rmiclient.RmiConstant;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TermianlRuleAndParaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TerminalParameterCache {
    private Logger logger = LoggerFactory.getLogger(TerminalParameterCache.class);
    /**
     * key: 终端标识+"_"+内部协议指令号
     */
    private volatile Map<String, Object> paras = new ConcurrentHashMap<>();
    @Autowired
    private DARmiClient rmiclient;

    private void initTerminalParameters() {
        try {
            logger.info("开始加载[终端参数设置数据]...");
            long ss = System.currentTimeMillis();
            TermianlRuleAndParaService currentServer = (TermianlRuleAndParaService) rmiclient.rmiBalancerRequest(RmiConstant.QUERY_TER_RULES);
            // 加载统计中需要填充阀值的终端设置参数
            int[] commands = new int[]{AllCommands.Terminal.OvertimeParking_VALUE};
            Map<String, Object> parameters = currentServer.queryTerminalByParaCode(commands);
            if (null != parameters && parameters.size() > 0) {
                paras = parameters;
                logger.info("加载[终端参数设置数据]完成，共加载条数:{}[耗时:{}]", paras.size(), (System.currentTimeMillis() - ss) / 1000L);
            } else {
                logger.warn("加载[终端参数设置数据]出错，加载结果为空，请检查[耗时:{}]", (System.currentTimeMillis() - ss) / 1000L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getTerminalParameters() {
        return paras;
    }

    public void reload() {
        paras.clear();
        initTerminalParameters();
    }
}
