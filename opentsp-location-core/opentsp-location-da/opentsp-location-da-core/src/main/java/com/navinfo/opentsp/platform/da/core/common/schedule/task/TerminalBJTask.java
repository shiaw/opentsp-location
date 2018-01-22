package com.navinfo.opentsp.platform.da.core.common.schedule.task;

import com.navinfo.opentsp.platform.da.core.cache.TerminalMilOilTypeCache;
import com.navinfo.opentsp.platform.da.core.common.schedule.ITask;
import com.navinfo.opentsp.platform.da.core.common.schedule.TaskStatus;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerMilOilTypeDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerMilOilTypeDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcTerMilOilTypeDaoImpl;

import java.util.List;
import java.util.TimerTask;

/**
 * Created by 修伟 on 2017/7/28 0028.
 */
public class TerminalBJTask extends TimerTask implements ITask {

    private LcTerMilOilTypeDao dao = new LcTerMilOilTypeDaoImpl();

    @Override
    public void setExecuteCycle(long cycle) {

    }

    @Override
    public long getLastExecuteTime() {
        return 0;
    }

    @Override
    public TaskStatus getStatus() {
        return null;
    }

    @Override
    public void run() {
        try {
            //加载终端标记（终端里程油耗类型表数据）
            List<LcTerMilOilTypeDBEntity> list = dao.queryData();
            log.error("共加载终端标记：【 "+list.size()+" 】条");
            if (list.size()>0){
                for (LcTerMilOilTypeDBEntity entity : list){
                    TerminalMilOilTypeCache.getInstance().addCache(entity);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
