package com.navinfo.opentsp.platform.location.service;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.platform.location.entity.GpsDataEntityDB;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * Created by wanliang on 2017/4/10.
 */
public interface LocationDataService {

    /**
     * 存储Gps位置数据<br>
     * 一次存储某个终端一天的数据
     *
     * @param packet
     *            {@link Packet}
     */
    abstract boolean saveGpsData(Packet packet);

}
