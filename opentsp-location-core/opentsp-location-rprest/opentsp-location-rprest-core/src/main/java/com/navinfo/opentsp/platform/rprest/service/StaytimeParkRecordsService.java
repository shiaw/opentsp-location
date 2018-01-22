package com.navinfo.opentsp.platform.rprest.service;

import com.navinfo.opentsp.platform.rprest.entity.StaytimeParkRecords;
import com.navinfo.opentsp.platform.rprest.persisted.StaytimeParkRecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaytimeParkRecordsService
{

    @Autowired
    private StaytimeParkRecordsRepository staytimeParkRecordsRepository;

    public StaytimeParkRecords getStaytimeParkRecords(List<Long> terminalID, List<Long> areaIds, long startDate, long endDate)
    {
        return this.staytimeParkRecordsRepository.getStaytimeParkRecords(terminalID, areaIds, startDate, endDate);
    }
}
