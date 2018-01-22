package com.navinfo.opentsp.platform.rprest.service;

import com.navinfo.opentsp.platform.rprest.CarsDistrictLastestDaysNumsCommand;
import com.navinfo.opentsp.platform.rprest.dto.CarsInDistrictLastestDaysNumDto;
import com.navinfo.opentsp.platform.rprest.persisted.OnlineCarCountryRepository;
import com.navinfo.opentsp.platform.rprest.persisted.OnlineCarProvinceCityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/5/22.
 */
@Component
public class CarsInDistrictLastestDaysNumService {
    /**
     * CarsInDistrictLastestDaysNum Service LOG
     */
    protected static final Logger logger = LoggerFactory.getLogger(CarsInDistrictLastestDaysNumService.class);

    /**
     * CarsInDistrictLastestDaysNum Repository Instance
     */
    @Autowired
    private OnlineCarCountryRepository onlineCarCountryRepository;
    @Autowired
    private OnlineCarProvinceCityRepository onlineCarProvinceCityRepository;

    public CarsInDistrictLastestDaysNumDto getData(Integer districtType, Integer districtCode) {

        CarsInDistrictLastestDaysNumDto data =new CarsInDistrictLastestDaysNumDto();
        data.setDistrict(districtCode);
        try{
            Integer[] numbers = new Integer[7];
            ArrayList<String> pastDaysList = getPastDates(7);
            if(pastDaysList != null && pastDaysList.size()!=0){
                for(String date : pastDaysList){
                    Integer number = 0;
                    if(0 == districtType){
                        //全国
                        number = onlineCarCountryRepository.getOnlineCarCountryNum(date);
                    }else if(1 == districtType){
                        //省
                        number = onlineCarProvinceCityRepository.getOnlineCarProvinceNum(date,districtCode);
                    }else if(2 == districtType){
                        //市
                        number = onlineCarProvinceCityRepository.getOnlineCarCityNum(date,districtCode);
                    }
                    numbers[pastDaysList.indexOf(date)] =  number;
                }
            }
            data.setNumber(numbers);
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }
    /**
     * 获取过去任意天内的日期数组
     * @param intervals      intervals天内
     * @return              日期数组
     */
    public static ArrayList<String> getPastDates(int intervals) {
        ArrayList<String> pastDaysList = new ArrayList<String>();

        for (int i = intervals-1; i >= 0; i--) {
            pastDaysList.add(getPastDate(i));

        }
        return pastDaysList;
    }
    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String result = format.format(today);
        return result;
    }

}
