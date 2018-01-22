package com.navinfo.opentsp.platform.rprest.persisted;

import com.navinfo.opentsp.platform.rprest.entity.TerminalMileageOilEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyue on 2017/6/20.
 */
@Repository
public class TerminalMileageOilRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    /**
     * 根据终端ID查询
     * @return TerminalMileageOilEntity
     */
    public TerminalMileageOilEntity query(long terminalId){
        String sql = "select MILEAGE_TYPE,OIL_TYPE from lc_terminal_mileage_oil_type where TERMINAL_ID = :terminalId";
        List<Map<String,Object>> resultList = null;
        TerminalMileageOilEntity entity = null;
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("terminalId",terminalId);
        try{
            resultList = jdbcTemplate.queryForList(sql,paramMap);
            if( null != resultList && 1 == resultList.size()){
                entity = new TerminalMileageOilEntity();
                entity.setTerminalId(terminalId);
                String MILEAGE_TYPE = resultList.get(0).get("MILEAGE_TYPE").toString();
                if(null != MILEAGE_TYPE){
                    entity.setMileageType(Integer.parseInt(MILEAGE_TYPE));
                }
                String OIL_TYPE = resultList.get(0).get("OIL_TYPE").toString();
                if(null != OIL_TYPE){
                    entity.setOilType(Integer.parseInt(OIL_TYPE));
                }
            }
        }catch(Exception e){
            LOG.info("查询数据库失败");
            e.printStackTrace();
            return entity;
        }
        return entity;
    }
    public int update(Long terminalId,Integer mileageType,Integer oilType){
        String sql = "update lc_terminal_mileage_oil_type set MILEAGE_TYPE = :mileageType,OIL_TYPE = :oilType where TERMINAL_ID = :terminalId";
        Map<String,Object> paramMap = new HashMap<>();
        if(!"".equals(mileageType)){
            paramMap.put("mileageType",mileageType);
        }
        if(!"".equals(oilType)){
            paramMap.put("oilType",oilType);
        }
        paramMap.put("terminalId",terminalId);
        int i = 0;
        try{
            i = jdbcTemplate.update(sql,paramMap);
        }catch(Exception e){
            LOG.info("更新数据库失败");
            e.printStackTrace();
            return 200;
        }
        return i;
    }
}
