package com.navinfo.opentsp.platform.rprest.persisted;

import com.navinfo.opentsp.platform.rprest.entity.OnlineCarCity;
import com.navinfo.opentsp.platform.rprest.entity.OnlineCarCountry;
import com.navinfo.opentsp.platform.rprest.entity.OnlineCarProvinceCity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OnlineCarProvinceCityRepository{

    private static final Logger log = LoggerFactory.getLogger(OnlineCarCountryRepository.class);

    @Value("${opentsp.carsInDistrictOnline.count:6}")
    private Integer count;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(OnlineCarProvinceCity provinceCity){
        mongoTemplate.save(provinceCity);
    }

    /*
        获取省级车辆在线数
     */
    public Integer getOnlineCarProvinceNum(String date,Integer districtCode) {
        Integer number = 0;
        Criteria criteria = new Criteria();
        criteria.and("date").is(date);
        criteria.and("districtCode").is(districtCode);
        Query query = new Query();
        query.addCriteria(criteria);
        OnlineCarProvinceCity onlineCarProvinceCity = this.mongoTemplate.findOne(query, OnlineCarProvinceCity.class);
        if(onlineCarProvinceCity!=null){
            number = onlineCarProvinceCity.getNumber();
        }
        return number;
    }

    /*
        获取市级车辆在线数
     */
    public Integer getOnlineCarCityNum(String date,Integer districtCode) {
        Integer number = 0;
        Criteria criteria = new Criteria();
        criteria.and("date").is(date);
        criteria.and("cityList.districtCode").is(districtCode);
        Query query = new Query();
        query.addCriteria(criteria);
        OnlineCarProvinceCity onlineCarProvinceCity = this.mongoTemplate.findOne(query, OnlineCarProvinceCity.class);
        if(onlineCarProvinceCity != null){
            List<OnlineCarCity> onlineCarCityList = onlineCarProvinceCity.getCityList();
            for(OnlineCarCity onlineCarCity : onlineCarCityList){
                if(districtCode == onlineCarCity.getDistrictCode()){
                    number = onlineCarCity.getNumber();
                }
            }
        }
        return number;
    }

    public List<OnlineCarProvinceCity> findOnlineTop(String queryDay, Integer districtType, Integer districtCode){
        try{
            log.debug("进入查询在线车辆排行，参数：queryDay="+queryDay+",districtType="+districtType+",districtCode="+districtCode);
            if(null!=queryDay&&null!=districtType&&null!=districtCode){
                Query query = new Query();
                Criteria criteria = Criteria.where("date").is(queryDay);
                if(districtType==0){
                    log.debug("查询类型(全国) "+districtType);
                }else{
                    if(districtType==1){
                        log.debug("查询类型(省) "+districtType);
                    }else{
                        log.debug("查询类型(市) "+districtType);
                    }
                    criteria.andOperator(Criteria.where("districtCode").is(districtCode));
                }
                query.addCriteria(criteria);
                query.limit(count);
                query.with(new Sort(Sort.Direction.DESC, "number"));
                return mongoTemplate.find(query,OnlineCarProvinceCity.class);
            }else{
                return null;
            }
        }catch(Exception e){
            log.error("在线车辆数排行error",e);
        }
        return null;
    }
}
