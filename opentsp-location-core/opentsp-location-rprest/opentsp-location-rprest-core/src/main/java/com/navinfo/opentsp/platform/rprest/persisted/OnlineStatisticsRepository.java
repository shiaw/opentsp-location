package com.navinfo.opentsp.platform.rprest.persisted;

import com.navinfo.opentsp.platform.rprest.entity.OnlineStatisticsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wanliang on 2017/5/19.
 */
public interface OnlineStatisticsRepository  extends MongoRepository<OnlineStatisticsEntity,String> {

    public OnlineStatisticsEntity findByDay(String day);
}
