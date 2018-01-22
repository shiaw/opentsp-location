package com.navinfo.tasktracker.rprest.service;

import com.navinfo.tasktracker.rprest.dao.LcCurrentcarCountMapper;
import com.navinfo.tasktracker.rprest.domain.LcCurrentcarCount;
import com.navinfo.tasktracker.rprest.entity.SuccessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;

/**
 * com.navinfo.tasktracker.rprest.service
 *
 * @author zhangdong
 * @date 2017/11/28
 */
@Service
public class CountCarsService {

    private static final Logger logger = LoggerFactory.getLogger(CountCarsService.class);

    @Autowired
    private LcCurrentcarCountMapper lcCurrentcarCountMapper;
    @Value("${queryCarOnlineCountsurl}")
    private String queryCarOnlineCountsurl;
    @Autowired
    private RestTemplate restTemplate;

    public SuccessResponse queryCarOnlineCounts() throws Exception {

        SuccessResponse<Map> re = restTemplate.getForObject(queryCarOnlineCountsurl, SuccessResponse.class);
        if (re.getResultCode() != 200) {
            logger.error(queryCarOnlineCountsurl + "failed, response:" + re);
            throw new Exception(queryCarOnlineCountsurl + "failed");
        }

        LcCurrentcarCount lcCurrentcarCount = new LcCurrentcarCount();
        lcCurrentcarCount.setCarCount(Long.valueOf(re.getData().get("onLineCounts").toString()));
        lcCurrentcarCount.setInsertTime(new Date());
        lcCurrentcarCountMapper.insertSelective(lcCurrentcarCount);
        return new SuccessResponse(200,"OK");
    }
}
