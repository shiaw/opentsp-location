package com.navinfo.opentsp.gateway.tcp.proto.location.service.impl;

import com.navinfo.opentsp.gateway.tcp.proto.location.mapper.TerminalMileageOilTypeMapper;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalMileageOilType;
import com.navinfo.opentsp.gateway.tcp.proto.location.service.TerminalMileageOilTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalMileageOilTypeServiceImpl implements TerminalMileageOilTypeService {
    private static final Logger log = LoggerFactory
            .getLogger(TerminalMileageOilTypeServiceImpl.class);

    @Autowired
    TerminalMileageOilTypeMapper terminalMileageOilTypeMapper;

    @Override
    public List<TerminalMileageOilType> getMileageOil(Long minTime) {
        try {
            List<TerminalMileageOilType> list = terminalMileageOilTypeMapper.getMileageOil(minTime);
            return list;
        } catch (Exception e) {
            log.error("获取车辆里程、能耗时出粗:" + e);
            return null;
        }
    }
}
