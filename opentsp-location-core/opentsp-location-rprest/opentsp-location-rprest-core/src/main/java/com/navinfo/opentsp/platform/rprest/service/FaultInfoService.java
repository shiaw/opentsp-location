package com.navinfo.opentsp.platform.rprest.service;

import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.location.protocol.common.LCFaultInfo;
import com.navinfo.opentsp.platform.rprest.FaultInfoCommand;
import com.navinfo.opentsp.platform.rprest.entity.FaultData;
import com.navinfo.opentsp.platform.rprest.utils.*;
import com.navinfo.opentsp.platform.rprest.utils.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by 修伟 on 2017/11/10 0010.
 */
@Component
public class FaultInfoService {
    protected static final Logger logger = LoggerFactory.getLogger(FaultInfoService.class);
    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * 存实际数据redis key
     */
    private final String CONCENTRATED_DATA = "lc_fault_data_";//lc_fault_data
    private final int CONCENTRATEDTAB = 7;
    private final int tempMonth = 12;
    private final int tempTime = 10;

    public List<FaultData> queryFaultInfo(FaultInfoCommand command){
        try{
            //查询mongodb结果
            List<FaultData> list = this.queryFault(command);
            if (list!=null){
                return list;
            }
            return null;
        }catch (Exception e){
            logger.error("异常{}" , e.getMessage());
            return null;
        }
    }

    public List<FaultData> queryFault(FaultInfoCommand command){
        try {
            List<String> strList = new ArrayList<>();
            for (Long tid : command.getTerminalId()){
                strList.add("0"+tid);
            }
            long startDate = command.getBeginDate();
            long endDate = command.getEndDate();
            //获取要查询哪个月表
            //添加查询表名
            List list = new ArrayList<>();
            List month = nextTimme(startDate, endDate);
            // 添加本选时间月表
            list.addAll(month);
            //如果是每月的一号
            /*if(getDay(startDate) == 1){
                String upperMonth = upperYearMonth(startDate);
                if(upperMonth == null || "".equals(upperMonth)){
                    return null;
                }
                // 添加本选时间月表
                list.add(CONCENTRATED_DATA + upperMonth);
            }*/

            Criteria criteriaStart = new Criteria();
            criteriaStart.andOperator(
                    Criteria.where("terminalId").in(strList),
                    Criteria.where("gpsTime").gte(startDate),
                    Criteria.where("gpsTime").lte(endDate));

            Query query = new Query(criteriaStart);
            List<DBObject> listFin = new ArrayList<>();
            if(list != null && list.size() > 0) {
                //按月表查询
                for(int i2 = 0 ;i2 < list.size() ;i2++) {
                    List<DBObject> listTemp = mongoTemplate.find(query, DBObject.class, list.get(i2).toString());
                    if(listTemp.size() > 0){
                        listFin.addAll(listTemp);
                    }
                }
            }
            if (listFin != null && listFin.size() > 0){
                List<FaultData> tripList = new ArrayList<>();
                //Map<String,List<LCFaultInfo.FaultInfo>> map = new HashMap<>();
                Map<String,List<String>> map = new HashMap<>();
                for (DBObject dbObject : listFin){
                    String terminalId = (String) dbObject.get("terminalId");
                    byte[] bytes = (byte[]) dbObject.get("data");
                    //LCFaultInfo.FaultInfo data = LCFaultInfo.FaultInfo.parseFrom(bytes);
                    String data = Base64.encode(bytes);
                    if (map.containsKey(terminalId)){
                       // List<LCFaultInfo.FaultInfo> dataList = map.get(terminalId);
                        List<String> dataList = map.get(terminalId);
                        dataList.add(data);
                    } else {
                        //List<LCFaultInfo.FaultInfo> dataList = new ArrayList<>();
                        List<String> dataList = new ArrayList<>();
                        dataList.add(data);
                        map.put(terminalId,dataList);
                    }
                }
                for (Map.Entry<String,List<String>> entry : map.entrySet()){
                    FaultData data = new FaultData();
                    long tid = Long.parseLong(entry.getKey());
                    data.setTerminalId(tid);
                    data.setList(entry.getValue());
                    tripList.add(data);
                }
                return tripList;
            }

        }catch (Exception e){
            logger.error("行程数据查询异常：{}",e);
        }
        return null;
    }
    /**
     * 获取当前天
     */
    public int getDay(long time){
        try {
            //先解析给定的时间
            Date da = new Date(time);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(time);
            return c.get(Calendar.DAY_OF_MONTH);
        }catch (Exception e){
            logger.error(e.getMessage() + "OilingStealOilServiceImpl getDay");
        }
        return -1;
    }
    public List nextTimme(long startTime,long endTime){
        startTime = startTime*1000;
        endTime = endTime*1000;
        int i = 0;
        List listTabl = new ArrayList();
        String end = this.tempYearMonth(endTime, 1);
        int endin = Integer.valueOf(end);
        while (i < CONCENTRATEDTAB){
            i ++;
            String st = this.tempYearMonth(startTime, i);
            int stin = Integer.valueOf(st);

            if((stin) <= endin){
                listTabl.add(CONCENTRATED_DATA + stin);
            }
        }
        return listTabl;
    }
    public String tempYearMonth(long time,int i){
        try {
            //先解析给定的时间
            Date da = new Date(time);
            Calendar c = Calendar.getInstance();
            c.setTime(da);

            int re = c.get(Calendar.MONTH) + i;
            int year = (c.get(Calendar.YEAR));
            if(re > tempMonth){
                re = (re - tempMonth);
                year = c.get(Calendar.YEAR) + 1;
            }
            String s = "";
            if(re < tempTime){
                s = "0" + re;
            }else{
                s = "" + re;
            }

            return year + s;
        }catch (Exception e){
            logger.error(e.getMessage() + "OilingStealOilServiceImpl upperMonth");
            return "";
        }
    }
    public String upperYearMonth(long time){
        try {
            //time = time * 1000;
            //先解析给定的时间
            Date da = new Date(time);
            Calendar c = Calendar.getInstance();
            c.setTime(da);
            c.add(Calendar.MONTH, -1);
            int re = c.get(Calendar.MONTH) + 1;
            String s = "";
            if(re < tempTime){
                s = "0" + re;
            }else{
                s = "" + re;
            }
            return c.get(Calendar.YEAR) + s;
        }catch (Exception e){
            logger.error(e.getMessage() + "OilingStealOilServiceImpl upperMonth");
            return "";
        }
    }
}
