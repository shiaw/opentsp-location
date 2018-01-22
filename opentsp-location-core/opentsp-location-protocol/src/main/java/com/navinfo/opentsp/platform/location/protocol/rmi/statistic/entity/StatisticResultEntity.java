package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity;

import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 定义DSA统计计算存储的结果
 * <p>
 * 按照永进的思路处理里程，全局缓存对应redis数据，每次存储redis只更新当前有变化的终端，此处List<Mileages> mileages，只是当前实时计算的结果集     hk
 */
public class StatisticResultEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<Long, List<OvertimeParkAlarm>> overtimeParkAlarms = null;//滞留超时
    private Map<Long, List<StaytimeParkAlarm>> staytimeParkAlarms = null;//区域停留时长
    private Map<Long, StaytimeParkAlarm> staytimeParkAlarmCache = new HashMap<>();//区域停留缓存对象
    private List<Mileages> mileages = null;//里程能耗
    private List<VehiclePassTimesTreeNode> grids = null;//热力图
    private List<VehiclePassTimesRecord> areas = null;//区域车次（服务站、省、市），离线统计，计算轨迹
    private List<VehiclePassTimesRecord> lastestAreas = null;//最新区域车次，计算最新位置
    private Map<Long, List<FaultCodeEntity>> faultCodeEntities = null;//故障码
    private List<VehiclePassInDistrict> districts = null;//最新行政区域车次，计算最新位置
    private Map<Long, List<StagnationTimeoutEntity>> stagnationTimeouts = null;

    public Map<Long, List<StagnationTimeoutEntity>> getStagnationTimeouts() {
        if (stagnationTimeouts == null) {
            stagnationTimeouts = new HashMap<>();
        }
        return stagnationTimeouts;
    }

    public void setStagnationTimeout(Map<Long, List<StagnationTimeoutEntity>> stagnationTimeouts) {
        this.stagnationTimeouts = stagnationTimeouts;
    }

    public List<VehiclePassInDistrict> getDistricts() {
        if (districts == null) {
            districts = new ArrayList<>();
        }
        return districts;
    }

    public void setDistricts(List<VehiclePassInDistrict> districts) {
        this.districts = districts;
    }

    public List<VehiclePassTimesRecord> getLastestAreas() {
        if (lastestAreas == null) {
            lastestAreas = new ArrayList<>();
        }
        return lastestAreas;
    }

    public void setLastestAreas(List<VehiclePassTimesRecord> lastestAreas) {
        this.lastestAreas = lastestAreas;
    }

    public Map<Long, List<OvertimeParkAlarm>> getOvertimeParkAlarms() {
        if (overtimeParkAlarms == null) {
            overtimeParkAlarms = new HashMap<Long, List<OvertimeParkAlarm>>();
        }
        return overtimeParkAlarms;
    }

    public void setOvertimeParkAlarms(Map<Long, List<OvertimeParkAlarm>> overtimeParkAlarms) {
        this.overtimeParkAlarms = overtimeParkAlarms;
    }

    public Map<Long, List<StaytimeParkAlarm>> getStaytimeParkAlarms() {
        if (staytimeParkAlarms == null) {
            staytimeParkAlarms = new HashMap<Long, List<StaytimeParkAlarm>>();
        }
        return staytimeParkAlarms;
    }

    public void setStaytimeParkAlarms(Map<Long, List<StaytimeParkAlarm>> staytimeParkAlarms) {
        this.staytimeParkAlarms = staytimeParkAlarms;
    }

    public List<Mileages> getMileage() {
        if (mileages == null) {
            mileages = new ArrayList<Mileages>();
        }
        return mileages;
    }

    public void setMileage(List<Mileages> mileages) {
        this.mileages = mileages;
    }

    public List<VehiclePassTimesTreeNode> getGrids() {
        if (grids == null) {
            grids = new ArrayList<>();
        }
        return grids;
    }

    public void setGrids(List<VehiclePassTimesTreeNode> grids) {
        this.grids = grids;
    }

    public List<VehiclePassTimesRecord> getAreas() {
        if (areas == null) {
            areas = new ArrayList<>();
        }
        return areas;
    }

    public void setAreas(List<VehiclePassTimesRecord> areas) {
        this.areas = areas;
    }

    public Map<Long, List<FaultCodeEntity>> getFaultCodeEntities() {
        if (faultCodeEntities == null) {
            faultCodeEntities = new HashMap<>();
        }
        return faultCodeEntities;
    }

    public void setFaultCodeEntities(Map<Long, List<FaultCodeEntity>> faultCodeEntities) {
        this.faultCodeEntities = faultCodeEntities;
    }

    public Map<Long, StaytimeParkAlarm> getStaytimeParkAlarmCache() {
        return staytimeParkAlarmCache;
    }

    public void setStaytimeParkAlarmCache(Map<Long, StaytimeParkAlarm> staytimeParkAlarmCache) {
        this.staytimeParkAlarmCache = staytimeParkAlarmCache;
    }
}
