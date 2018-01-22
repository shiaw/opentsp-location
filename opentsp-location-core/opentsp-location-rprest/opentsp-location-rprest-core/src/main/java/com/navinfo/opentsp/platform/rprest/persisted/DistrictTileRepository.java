package com.navinfo.opentsp.platform.rprest.persisted;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class DistrictTileRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * 根据瓦片ID集合查询
     * @param tileIds 瓦片ID集合
     * @return MAP<瓦片ID,城市行政区域编码>
     */
    public Map<String,Integer> findIn(Set<String> tileIds){
        String sql = "select TILE_ID,DISTRICT_ID from lc_district_and_tile_mapping where TILE_ID in (:tileIds)";
        List<Map<String,Object>> resultList = null;
        Map<String,Integer> resultMap = new HashMap<>();
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("tileIds",tileIds);
        try{
            resultList = jdbcTemplate.queryForList(sql,paramMap);
            for(Map<String,Object> map : resultList){
                resultMap.put(map.get("TILE_ID").toString(),Integer.parseInt(map.get("DISTRICT_ID").toString()));
            }
        }catch(Exception e){
            LOG.info("查询数据库失败");
            e.printStackTrace();
            return resultMap;
        }
        return resultMap;
    }
}