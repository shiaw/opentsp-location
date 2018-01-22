package com.navinfo.opentsp.platform.rprest.entity;

import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCStaytimePark;

import java.util.List;

/**
 * Created by ZhangYue on 2017/7/12.
 */
public class StaytimeParkRecords {
    private int statusCode;
    private int totalRecords;
    private List<StaytimePark> dataList;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public List<StaytimePark> getDataList() {
        return dataList;
    }

    public void setDataList(List<StaytimePark> dataList) {
        this.dataList = dataList;
    }
}
