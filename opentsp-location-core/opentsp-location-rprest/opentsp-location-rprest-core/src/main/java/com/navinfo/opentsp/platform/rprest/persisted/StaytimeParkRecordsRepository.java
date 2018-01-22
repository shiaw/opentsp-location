package com.navinfo.opentsp.platform.rprest.persisted;

import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCStaytimeParkRecoreds;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCStaytimePark;
import com.navinfo.opentsp.platform.rprest.entity.StaytimePark;
import com.navinfo.opentsp.platform.rprest.entity.StaytimeParkRecords;
import com.navinfo.opentsp.platform.rprest.wstesttool.BasicWSTT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Component
@Repository
public class StaytimeParkRecordsRepository
{
    private static final Logger log = LoggerFactory.getLogger(StaytimeParkRecordsRepository.class);

    public StaytimeParkRecords getStaytimeParkRecords(List<Long> terminalID, List<Long> areaIds, long startDate, long endDate) {
        try {
            byte[] bytes = BasicWSTT.getStaytimeParkRecords(terminalID, areaIds, startDate, endDate);
            if (bytes != null) {
                LCStaytimeParkRecoreds.StaytimeParkRecoreds records = LCStaytimeParkRecoreds.StaytimeParkRecoreds.parseFrom(bytes);
                StaytimeParkRecords staytimeParkRecords = new StaytimeParkRecords();
                staytimeParkRecords.setStatusCode(records.getStatusCode());
                staytimeParkRecords.setTotalRecords(records.getTotalRecords());
                List list = new ArrayList();
                for (LCStaytimePark.StaytimePark staytimePark : records.getDataListList()) {
                    StaytimePark entity = new StaytimePark();
                    entity.setTerminalId(staytimePark.getTerminalId());
                    entity.setAreaId(staytimePark.getAreaId());
                    entity.setBeginDate(staytimePark.getBeginDate());
                    entity.setEndDate(staytimePark.getEndDate());
                    entity.setContinuousTime(staytimePark.getContinuousTime());
                    entity.setBeginLat(staytimePark.getBeginLat());
                    entity.setBeginLng(staytimePark.getBeginLng());
                    entity.setEndLat(staytimePark.getEndLat());
                    entity.setEndLng(staytimePark.getEndLng());
                    list.add(entity);
                }
                staytimeParkRecords.setDataList(list);
                return staytimeParkRecords;
            }
        } catch (Exception e) {
            log.error("查询区域停留时长webservice接口报错！");
            e.printStackTrace();
        }
        return null;
    }
}