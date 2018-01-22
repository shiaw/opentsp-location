package com.navinfo.opentsp.platform.monitor.dto;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangyue on 2017/6/14.
 */
public class LocationMonitorDto {
    private Map<String,List<LocationMonitorItem>> taLocationMonitorData;//ta位置监控数据
    private Map<String,List<LocationMonitorItem>> dpLocationMonitorData;//dp位置监控数据

    public Map<String, List<LocationMonitorItem>> getTaLocationMonitorData() {
        return taLocationMonitorData;
    }

    public void setTaLocationMonitorData(Map<String, List<LocationMonitorItem>> taLocationMonitorData) {
        this.taLocationMonitorData = taLocationMonitorData;
    }

    public Map<String, List<LocationMonitorItem>> getDpLocationMonitorData() {
        return dpLocationMonitorData;
    }

    public void setDpLocationMonitorData(Map<String, List<LocationMonitorItem>> dpLocationMonitorData) {
        this.dpLocationMonitorData = dpLocationMonitorData;
    }
}
