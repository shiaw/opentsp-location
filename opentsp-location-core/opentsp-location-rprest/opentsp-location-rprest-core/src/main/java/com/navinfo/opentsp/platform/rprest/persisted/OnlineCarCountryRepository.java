package com.navinfo.opentsp.platform.rprest.persisted;

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
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class OnlineCarCountryRepository {
    private static final Logger log = LoggerFactory.getLogger(OnlineCarCountryRepository.class);

    @Value("${opentsp.carsInDistrictOnline.count:6}")
    private Integer count;
    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(OnlineCarCountry country){
        mongoTemplate.save(country);
    }

    /*
         获取全国车辆在线数
      */
    public Integer getOnlineCarCountryNum(String date){
        Integer number = 0;
        Criteria criteria = new Criteria();
        criteria.and("date").is(date);
        Query query = new Query();
        query.addCriteria(criteria);
        OnlineCarCountry onlineCarCountry = this.mongoTemplate.findOne(query, OnlineCarCountry.class);
        if(onlineCarCountry != null){
            number = onlineCarCountry.getNumber();
        }
        return number;
    }
}
