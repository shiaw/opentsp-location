package com.navinfo.opentsp.platform.da.core.webService.manage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;

import com.navinfo.opentsp.platform.da.core.persistence.SynchronousManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.SynchronousManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcSynchronizationLogDBEntity;

/**
 *
 * 数据缓存方法
 * @author ss
 *
 */
public class SynchronousDataCache {
    private static SynchronousManage service=new SynchronousManageImpl();
    //缓存需要同步的数据
    private static Map<Integer,List<LcSynchronizationLogDBEntity>> dataCache=new ConcurrentHashMap<Integer,List<LcSynchronizationLogDBEntity>>();
    private static int maxSynchronizationLogId=1;

    //修改maxSynchronizationLogId的值
    public static synchronized int updateMaxSynchronizationLogId() {
        maxSynchronizationLogId=maxSynchronizationLogId+1;
        return 	maxSynchronizationLogId;
    }

    public static void initMaxSynchronizationLogId() throws Exception{
        maxSynchronizationLogId =service.getMaxRecordValue();
    }
    // 添加

    public static  void add(int distric, LcSynchronizationLogDBEntity dataModel) {
        List<LcSynchronizationLogDBEntity> districList=dataCache.get(distric);
        if(CollectionUtils.isNotEmpty(districList)){
            districList=new ArrayList<LcSynchronizationLogDBEntity>();
            dataCache.put(distric, districList);
        }
        if(districList != null){
            districList.add(dataModel);
        }

    }

    // 查询
    public static List<LcSynchronizationLogDBEntity> get(int distric) {
        return dataCache.get(distric);
    }

    // 删除
    public static void remove(String distric,LcSynchronizationLogDBEntity dataModel) {
        List<LcSynchronizationLogDBEntity> districList=dataCache.get(distric);
        if(CollectionUtils.isNotEmpty(districList)){
            districList.remove(dataModel);
        }
    }

    // 缓存size
    public static int size() {
        return dataCache.size();
    }


}
