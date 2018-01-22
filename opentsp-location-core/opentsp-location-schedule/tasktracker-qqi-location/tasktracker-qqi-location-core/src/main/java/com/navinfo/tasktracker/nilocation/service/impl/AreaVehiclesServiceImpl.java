package com.navinfo.tasktracker.nilocation.service.impl;

import com.navinfo.tasktracker.nilocation.entity.AreaVehiclesEntity;
import com.navinfo.tasktracker.nilocation.mapper.AreaVehiclesMapper;
import com.navinfo.tasktracker.nilocation.service.AreaVehiclesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 修伟 on 2017/9/25 0025.
 */
@Service
public class AreaVehiclesServiceImpl implements AreaVehiclesService {

    @Autowired
    AreaVehiclesMapper areaVehiclesMapper;

    @Override
    public List<AreaVehiclesEntity> getAreaVehiclesList() {
        List<AreaVehiclesEntity> list = areaVehiclesMapper.getAreaVehiclesList();
        if (list.size()>0){
            return list;
        }
        return null;
    }

}
