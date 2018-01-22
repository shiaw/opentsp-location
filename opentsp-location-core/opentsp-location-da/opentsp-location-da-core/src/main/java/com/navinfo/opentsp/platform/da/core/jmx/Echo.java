package com.navinfo.opentsp.platform.da.core.jmx;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCMileageAndOilDataRes.MileageAndOilData;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempGpsData;

public class Echo implements EchoMBean {
    private final static Logger log = LoggerFactory.getLogger(Echo.class);

    @Override
    public String delMileageAndOilDataStatisticCache() {
        log.info("Echo.delMileageAndOilDataStatisticCache");
        try {
            TempGpsData.delMileageAndOilDataStatisticCache();
            return "SUCCESS";
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return "FAILED";
        }

    }

    @Override
    public String getMileageAndOilDataByTerminalId(Long terminalId) {
        MileageAndOilData data = TempGpsData.mileageAndOilDataCache.get(terminalId);
        if (data != null) {
            return "mileage=" + data.getMileage() + ",oil=" + data.getOil();
        }
        return "null";
    }

    @Override
    public String getAllMileageAndOilData() {
        Map<Long, MileageAndOilData> map = TempGpsData.mileageAndOilDataCache;
        if (map != null) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<Long, MileageAndOilData> entry : map.entrySet()) {
                Long t = entry.getKey();
                MileageAndOilData data = entry.getValue();
                if (data != null) {
                    float oil = data.getOil();
                    long mileage = data.getMileage();
                    builder.append("[terminalId=" + t + ",oil=" + oil + ",mileage=" + mileage + "],");
                }
            }
            return builder.toString();
        }
        return "null";
    }
}
