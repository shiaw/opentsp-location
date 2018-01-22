package com.navinfo.opentsp.platform.da.core.common.schedule;

import com.navinfo.opentsp.platform.da.core.cache.TerminalIdCache;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalInfoDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcTerminalInfoDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/9/12
 * @modify 定时获取终端
 * @copyright opentsp
 */
@Component
@Configurable
@EnableScheduling
public class GetTerminalIdsTask {



    private static final Logger log = LoggerFactory.getLogger(GetTerminalIdsTask.class);



    @Scheduled(cron = "${opentsp.getTerminalIds.schedule.cron:0 0/1 * * * ?}")
    public void getTerminalIds() {
        try {
            MySqlConnPoolUtil.startTransaction();
            long startTime = System.currentTimeMillis();
            LcTerminalInfoDao lcTerminalInfoDao = new LcTerminalInfoDaoImpl();
            List<Long> terminalIds = lcTerminalInfoDao.getTerminalIds();
            if (terminalIds != null) {
                TerminalIdCache.getInstance().add(terminalIds);
                log.error("定时获取终端ID列表,获取{}条，耗时{}",terminalIds.size(),System.currentTimeMillis()-startTime);
            }
        }catch (Exception e){
            log.error("定时获取终端ID列表{}",e.getMessage());
        }finally {
            MySqlConnPoolUtil.close();
        }
    }

}
