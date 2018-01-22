package com.navinfo.opentsp.platform.location.entity;

import com.mongodb.DBObject;

import java.util.Comparator;

/**
 * Created by wanliang on 2017/4/11.
 */
public class GpsDataComparator  implements Comparator {

    public int compare(Object o1, Object o2) {
        long gpsTime1=(long) ((DBObject)o1).get("gpsTime");
        long gpsTime2=(long) ((DBObject)o2).get("gpsTime");
        if (gpsTime1 < gpsTime2){
            return -1;
        }
        if (gpsTime1 > gpsTime2){
            return 1;
        }
        return 0;
    }
}