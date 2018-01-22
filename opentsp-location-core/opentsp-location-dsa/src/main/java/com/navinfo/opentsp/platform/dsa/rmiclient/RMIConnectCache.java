package com.navinfo.opentsp.platform.dsa.rmiclient;

import com.navinfo.opentsp.platform.dsa.utils.DateUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.GpsDataEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.GpsDetailedEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TermianlDataService;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TermianlRuleAndParaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.*;

/**
 * DSA和DA的RMI连接管理
 *
 * @author hk
 */
@Component
public class RMIConnectCache {

    protected static Logger logger = LoggerFactory.getLogger(RMIConnectCache.class);
    private static RMIConnectCache _instance = null;
    @Autowired
    private DARmiClient rmiclient;

    // TODO test
    public List<Long> getAllTerminalInfo() throws RemoteException {
        List<Long> tids = new ArrayList<Long>();
        List<TerminalInfo> allTerminalInfo = ((TermianlRuleAndParaService) rmiclient.rmiBalancerRequest(RmiConstant.QUERY_TER_RULES)).getAllTerminalInfo();
        for (TerminalInfo terminal : allTerminalInfo) {
            tids.add(terminal.getTerminalId());
        }
        return tids;
    }

    public Map<Long, List<LocationData>> getLastLocationData(List<Long> tids, int type) throws RemoteException {
        TermianlDataService service = (TermianlDataService) rmiclient.rmiBalancerRequest(RmiConstant.QUERY_TER_GPS);
        return service.getLastLocationData(tids, type);
    }

    public Map<Long, List<LocationData>> getSegLocationData(List<Long> tids, int segment) throws RemoteException {
        TermianlDataService service = (TermianlDataService) rmiclient.rmiBalancerRequest(RmiConstant.QUERY_TER_GPS);
        return service.queryGpsDataSegementThisDay(tids, segment);
    }

    public Map<Long, List<LocationData>> getHistoryLocationData(List<Long> tids, Long start, Long end)
            throws RemoteException {
        TermianlDataService service = (TermianlDataService) rmiclient.rmiBalancerRequest(RmiConstant.QUERY_TER_GPS);
        return service.queryGpsDataFromRedis(tids, start, end);
    }

    // 4 test
    public Map<Long, GpsDataEntity> mockData() {
        Map<Long, GpsDataEntity> test = new HashMap<Long, GpsDataEntity>();
        GpsDataEntity en = new GpsDataEntity();
        en.settId(1);
        en.setTotal(2);
        try {
            en.setDay(DateUtils.date2String(new Date(), DateUtils.ALL));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<GpsDetailedEntity> dataList = new ArrayList<GpsDetailedEntity>();
        GpsDetailedEntity entity = new GpsDetailedEntity();
        entity.set_id(1);
        entity.setData(new byte[]{});
        en.setDataList(dataList);
        test.put(1L, en);
        return test;
    }
}
