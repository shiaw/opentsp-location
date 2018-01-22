package com.navinfo.opentsp.gateway.tcp.proto.location.cache;

import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalSnInfo;
import com.navinfo.opentsp.gateway.tcp.proto.location.service.impl.TerminalSnInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 修伟 on 2017/9/25 0025.
 */
@Component
public class TerminalSnInfoCache {
    private static Map<Long,TerminalSnInfo> cache = new ConcurrentHashMap<Long,TerminalSnInfo>();

    private static TerminalSnInfoCache instance = new TerminalSnInfoCache();

    public static TerminalSnInfoCache getInstance(){
        return instance;
    }

    @Autowired
    TerminalSnInfoServiceImpl impl;

    public void add(TerminalSnInfo info){
        if (cache.containsKey(info.getTerminalId())){
            TerminalSnInfo snInfo = cache.get(info.getTerminalId());
            if (snInfo.getSn().equals(info.getSn()) && snInfo.getEcuid().equals(info.getEcuid())&&snInfo.getVan().equals(info.getVan())&&snInfo.getVehicle().equals(info.getVehicle())){
                //不更新
            }else{
                //更新数据库
                impl.updateByPrimaryKey(info);
            }
        }else{
            cache.put(info.getTerminalId(),info);
            impl.insert(info);
        }
    }

    public void reload(){
        List<TerminalSnInfo> list = impl.getAll();
        if (list!=null&&list.size()>0){
            for (TerminalSnInfo info : list){
                cache.put(info.getTerminalId(),info);
            }
        }
    }
}
