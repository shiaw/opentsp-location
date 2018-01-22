package com.navinfo.opentsp.platform.rprest.persisted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CurrentcarCountRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    public List<Map<String,Object>> queryList(Date begin, Date end) throws Exception {
        String sql = "select car_count,insert_time from lc_currentcar_count where insert_time BETWEEN :begin AND :end ORDER BY insert_time";
        List<Map<String,Object>> resultList = null;
        Map<String,Integer> resultMap = new HashMap<>();
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("begin",begin);
        paramMap.put("end",end);
        try{
            resultList = jdbcTemplate.queryForList(sql,paramMap);
        }catch(Exception e){
            LOG.error("查询数据库失败", e);
            throw new Exception("查询数据库失败");
        }
        return resultList;
    }
}