package com.navinfo.opentsp.platform.dsa.service;

import com.navinfo.opentsp.platform.dsa.service.interf.Dealer;
import com.navinfo.opentsp.platform.dsa.service.interf.Segment;
import com.navinfo.opentsp.platform.dsa.utils.SegmentUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 分段执行逻辑发起类
 **/
@Component
@Scope("prototype")
public class SegmentService extends RealTimeService {

    @Autowired
    private List<Segment> dealers;
    @Override
    public String getBatchSwitch() {
        return SGBATCHSWITCH;
    }

    @Override
    public String getPackagePath() {
        return SGPACKAGEPATH;
    }

    @Override
    protected int getSegmentByDate(Calendar cal) {
        return SegmentUtils.getSGSeg(cal);
    }

    protected long getStartTime(long endTime) {
        return endTime - 300000;
    }

    protected long getEndTime(int segment) {
        return SegmentUtils.getSGEndTime(segment).getTimeInMillis();
    }

    @Override
    protected Map<Long, List<LocationData>> getData(List<Long> tids, int segment) throws Exception {
        return rMIConnectCache.getSegLocationData(tids, segment);
    }

    @Override
    protected int getLastTime() {
        return 287;
    }

    public List<Dealer> getDealer() {
        List<Dealer> ds = new ArrayList<Dealer>();
        ds.addAll(dealers);
        return ds;
    }
}
