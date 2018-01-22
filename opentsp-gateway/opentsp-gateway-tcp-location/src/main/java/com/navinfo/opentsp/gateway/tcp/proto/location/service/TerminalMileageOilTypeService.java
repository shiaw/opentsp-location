package com.navinfo.opentsp.gateway.tcp.proto.location.service;

import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalMileageOilType;

import java.util.List;

public interface TerminalMileageOilTypeService {

    /**
     * 增量获取车辆里程、能耗标记
     * @param minTime
     * @return
     */
    List<TerminalMileageOilType> getMileageOil(Long minTime);
}
