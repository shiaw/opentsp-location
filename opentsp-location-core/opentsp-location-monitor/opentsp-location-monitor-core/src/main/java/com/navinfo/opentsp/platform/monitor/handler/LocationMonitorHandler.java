package com.navinfo.opentsp.platform.monitor.handler;

import com.navinfo.opentsp.platform.location.kit.LocationMonitor;
import com.navinfo.opentsp.platform.monitor.LocationMonitorCommand;
import com.navinfo.opentsp.platform.monitor.cache.LocationMonitorCache;
import com.navinfo.opentsp.platform.monitor.dto.LocationMonitorDto;
import com.navinfo.opentsp.platform.monitor.dto.LocationMonitorItem;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyue on 2017/6/14.
 */
@Component
public class LocationMonitorHandler extends AbstractCommandHandler<LocationMonitorCommand, LocationMonitorCommand.Result> {
    private static final Logger logger = LoggerFactory.getLogger(LocationMonitorHandler.class);
    public LocationMonitorHandler() {
        super(LocationMonitorCommand.class, LocationMonitorCommand.Result.class);
    }

    @Override
    public LocationMonitorCommand.Result handle(LocationMonitorCommand locationMonitorCommand) {
        try {
            LocationMonitorCache cache = LocationMonitorCache.getInstance();
            if (null != cache && null != cache.getCache()) {
                LocationMonitorDto data = new LocationMonitorDto();
                Map<String, List<LocationMonitorItem>> taLocationMonitorData = new HashMap<String, List<LocationMonitorItem>>();
                Map<String, List<LocationMonitorItem>> dpLocationMonitorData = new HashMap<String, List<LocationMonitorItem>>();
                for (Map.Entry<String, LocationMonitor> entry : cache.getCache().entrySet()) {
                    LocationMonitor locationMonitor = entry.getValue();
                    String type = locationMonitor.getAppName();
                    List<LocationMonitorItem> talist = new ArrayList<LocationMonitorItem>();
                    List<LocationMonitorItem> dplist = new ArrayList<LocationMonitorItem>();
                    if (type.equals("TA")) {
                        LocationMonitorItem item1 = new LocationMonitorItem();
                        item1.setName("T");
                        item1.setValue(locationMonitor.getInTaCount());
                        item1.setDirect(false);
                        LocationMonitorItem item2 = new LocationMonitorItem();
                        item2.setName("T");
                        item2.setValue(locationMonitor.getBackTerminalCount());
                        item2.setDirect(true);
                        LocationMonitorItem item3 = new LocationMonitorItem();
                        item3.setName("KAFKA");
                        item3.setValue(locationMonitor.getOutTaCount());
                        item3.setDirect(true);
                        talist.add(item1);
                        talist.add(item2);
                        talist.add(item3);
                        taLocationMonitorData.put(entry.getKey(), talist);
                    } else if (type.equals("DP")) {
                        LocationMonitorItem item1 = new LocationMonitorItem();
                        item1.setName("KAFKA");
                        item1.setValue(locationMonitor.getInDpCount());
                        item1.setDirect(false);
                        LocationMonitorItem item2 = new LocationMonitorItem();
                        item2.setName("RP");
                        item2.setValue(locationMonitor.getOutRpCount());
                        item2.setDirect(true);
                        LocationMonitorItem item3 = new LocationMonitorItem();
                        item3.setName("DA");
                        item3.setValue(locationMonitor.getOutDaCount());
                        item3.setDirect(true);
                        dplist.add(item1);
                        dplist.add(item2);
                        dplist.add(item3);
                        dpLocationMonitorData.put(entry.getKey(), dplist);
                    }
                    data.setTaLocationMonitorData(taLocationMonitorData);
                    data.setDpLocationMonitorData(dpLocationMonitorData);
                }
                LocationMonitorCommand.Result result = new LocationMonitorCommand.Result();
                result.setData(data);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("读取统计图表数据出现异常！");
        }
        return null;
    }

}
