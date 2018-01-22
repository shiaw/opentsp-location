package com.navinfo.opentsp.platform.da.core.cache.alarm;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleBreakdown.VehicleBreakdown;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import com.navinfo.opentsp.platform.da.core.persistence.TermianlDynamicManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.TermianlDynamicManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDetailedEntityDB;


/**
 * ************************
 * 多终端报警概要分页缓存
 *
 * @author jin_s
 */
public class GpsDataCache {
    private static Logger logger = LoggerFactory.getLogger(GpsDataCache.class);
    public int totalRecords;
    /**
     * 报警数据缓存
     */
    private List<GpsDetailedEntityDB> gpsDataCache = new ArrayList<GpsDetailedEntityDB>();

    public boolean queryGpsData(List<Long> terminalIds,
                                long beginTime, long endTime, boolean isFilterAlarm, boolean isAllAlarm,
                                long alarms, boolean isFilterBreakdown, long breakdownCode) {
        TermianlDynamicManage service = new TermianlDynamicManageImpl();
        List<GpsDetailedEntityDB> gpsDatas = null;
        for (long terminalId : terminalIds) {
            gpsDatas = service.queryGpsData(terminalId, beginTime, endTime);
            if (gpsDatas != null && gpsDatas.size() > 0) {
                if (!isFilterAlarm) {
                    gpsDataCache.addAll(gpsDatas);
                    totalRecords += gpsDataCache.size();
                } else if (isFilterBreakdown) {
                    //判断是否为查询故障报警
                    List<GpsDetailedEntityDB> breakdownGps = new ArrayList<GpsDetailedEntityDB>();
                    for (GpsDetailedEntityDB gpsDetailedEntity : gpsDatas) {
                        try {
                            LocationData locationData = LocationData.parseFrom(gpsDetailedEntity.getData());
                            for (VehicleBreakdown bd : locationData.getBreakdownAddition().getBreakdownList()) {
                                long breakCode = (bd.getBreakdownSPNValue() * 1000) + bd.getBreakdownFMIValue();
                                if (0 == breakdownCode && 0 != breakCode) {
                                    breakdownGps.add(gpsDetailedEntity);
                                    break;
                                } else if (0 != breakdownCode && breakCode == breakdownCode) {
                                    breakdownGps.add(gpsDetailedEntity);
                                    break;
                                }
                            }
                        } catch (InvalidProtocolBufferException e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                    gpsDataCache.addAll(breakdownGps);
                    totalRecords += breakdownGps.size();
                } else {
                    List<GpsDetailedEntityDB> filterGpsDate = new ArrayList<GpsDetailedEntityDB>();
                    for (GpsDetailedEntityDB gpsDetailedEntity : gpsDatas) {
                        try {
                            LocationData locationData = LocationData.parseFrom(gpsDetailedEntity.getData());
                            if (isAllAlarm) {
                                if (locationData.getAlarm() > 0) {
                                    filterGpsDate.add(gpsDetailedEntity);
                                }
                            } else {
                                if ((locationData.getAlarm() & alarms) > 0) {
                                    filterGpsDate.add(gpsDetailedEntity);
                                }
                            }
                        } catch (InvalidProtocolBufferException e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                    gpsDataCache.addAll(filterGpsDate);
                    totalRecords += filterGpsDate.size();
                }
            }
        }
        return true;
    }

    public List<GpsDetailedEntityDB> getGpsData(List<Long> terminalIds, long beginTime,
                                                long endTime, boolean isFilterAlarm, boolean isAllAlarm,
                                                long alarms, CommonParameter comParameter, String queryKey) {

        if (totalRecords > 0) {
            int startIndex = (comParameter.getPageIndex() - 1) * comParameter.getPageSize();
            if (startIndex <= totalRecords) {
                int lastIndex = comParameter.getPageIndex() * comParameter.getPageSize() <= totalRecords ? comParameter.getPageIndex() * comParameter.getPageSize() : totalRecords;
                if (lastIndex >= 0) {
                    return gpsDataCache.subList(startIndex, lastIndex);
                }
            }
        }
        return null;
    }
}