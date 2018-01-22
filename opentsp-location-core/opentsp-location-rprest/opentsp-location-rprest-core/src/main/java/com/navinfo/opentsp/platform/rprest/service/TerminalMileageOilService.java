package com.navinfo.opentsp.platform.rprest.service;

import com.navinfo.opentsp.platform.rprest.entity.TerminalMileageOilEntity;
import com.navinfo.opentsp.platform.rprest.persisted.TerminalMileageOilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangyue on 2017/6/20.
 */
@Service
public class TerminalMileageOilService {
    @Autowired
    private TerminalMileageOilRepository terminalMileageOilRepository;

    public TerminalMileageOilEntity query(long terminalId){
        return  terminalMileageOilRepository.query(terminalId);
    }

    public int update(Long terminalId,Integer mileageType,Integer oilType){
        return  terminalMileageOilRepository.update(terminalId,mileageType,oilType);
    }

}
