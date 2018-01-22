package com.navinfo.tasktracker.nilocation.mapper;


import com.navinfo.tasktracker.nilocation.entity.AreaVehiclesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper
public interface AreaVehiclesMapper
{
	List<AreaVehiclesEntity> getAreaVehiclesList();
}
