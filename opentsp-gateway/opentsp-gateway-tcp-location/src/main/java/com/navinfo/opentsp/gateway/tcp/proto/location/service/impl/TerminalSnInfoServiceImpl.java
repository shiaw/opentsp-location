package com.navinfo.opentsp.gateway.tcp.proto.location.service.impl;

import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TerminalSnInfoCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.mapper.TerminalSnInfoMapper;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalSnInfo;
import com.navinfo.opentsp.gateway.tcp.proto.location.service.TerminalSnInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 修伟 on 2017/9/25 0025.
 */
@Service
public class TerminalSnInfoServiceImpl implements TerminalSnInfoService{

    @Autowired
    TerminalSnInfoMapper mapper;

    @Override
    public List<TerminalSnInfo> getAll() {
        List<TerminalSnInfo> list = mapper.getAll();
        if (list.size()>0){
            return list;
        }
        return null;
    }

    @Override
    public int insert(TerminalSnInfo record) {

        return mapper.insert(record);
    }

    @Override
    public int updateByPrimaryKey(TerminalSnInfo record) {
        return mapper.updateByPrimaryKey(record);
    }
}
