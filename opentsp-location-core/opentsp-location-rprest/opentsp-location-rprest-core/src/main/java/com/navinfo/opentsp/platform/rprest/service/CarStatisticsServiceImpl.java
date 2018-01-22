package com.navinfo.opentsp.platform.rprest.service;

import com.navinfo.opentsp.platform.rprest.entity.OnlineHour;
import com.navinfo.opentsp.platform.rprest.entity.OnlineStatisticsEntity;
import com.navinfo.opentsp.platform.rprest.persisted.CurrentcarCountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wanliang on 2017/5/19.
 */
@Service
public class CarStatisticsServiceImpl {
    private static final Logger LOG = LoggerFactory.getLogger(CarStatisticsServiceImpl.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CurrentcarCountRepository currentcarCountRepository;
    public Map carOnlineStatis30Day() throws Exception {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String yesterday = sdf.format(cal.getTime());
        Date begin = sdf.parse(yesterday);
        List<Map<String,Object>> resultList = currentcarCountRepository.queryList(begin, now);
        long todayArr[]=new long[24];
        long yesterdayArr[]=new long[24];
        if (resultList.size()<24){
            throw new Exception("resultList小于24条");
        }
        Map dataMap=new HashMap();
        for (int i=0;i<24;i++){
            yesterdayArr[i] = (long) resultList.get(i).get("car_count");
        }

        for (int i=0;i<resultList.size()-24;i++){
            todayArr[i] = (long) resultList.get(i+24).get("car_count");
        }


        /*SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat simpleDateFormatHour=new SimpleDateFormat("HH");
        String day=simpleDateFormat.format(new Date());
        Calendar theCa = Calendar.getInstance();
         theCa.setTime(new Date());
        theCa.add(theCa.DATE, -1);
        Date befor30Date= theCa.getTime();
        LOG.info("查询昨天的在线车辆统计，开始日期：{},结束日期：{}",simpleDateFormat.format(befor30Date),day);
        Query query=new Query(Criteria.where("day").gte(simpleDateFormat.format(befor30Date)).andOperator(Criteria.where("day").lte(day)));
        List<OnlineStatisticsEntity> onlineStatisticsEntityList= mongoTemplate.find(query,OnlineStatisticsEntity.class);
        Map<String,int[]> dataMap=new HashMap<>();
        int dayArr[]=new int[24];
        int avgArr[]=new int[24];
        int totalArr[][]=new int[24][onlineStatisticsEntityList.size()];
        int onlineArr[][]=new int[24][onlineStatisticsEntityList.size()];
        int todayArr[]=new int[24];
        int onlineCount=0;
        for(OnlineStatisticsEntity onlineStatisticsEntity: onlineStatisticsEntityList){
             if(day.equals(onlineStatisticsEntity.getDay())){
                 LOG.info("获取当天在线数据");
                 int todayNum=0;
                 for (OnlineHour onlineHour:onlineStatisticsEntity.getData()) {
                     todayArr[todayNum]=onlineHour.getOnline();
                     todayNum++;
                 }
             }
              int hourCount=0;
              for(OnlineHour onlineHour:onlineStatisticsEntity.getData()){
                  if(hourCount>=24){
                      break;
                  }
                  totalArr[hourCount][onlineCount]=onlineHour.getTotal();
                  onlineArr[hourCount][onlineCount]=onlineHour.getOnline();
                  hourCount++;
              }
            onlineCount++;
//            dayArr[onlineCount]=totalSum/count;
//            avgArr[onlineCount]=onlineSum/count;
        }
        for(int i=0;i<24;i++){
            int totalSum=0;
            int onlineSum=0;
            int tcount=0;
            int ocount=0;
            for(int j=0;j<totalArr[i].length;j++){
                totalSum+=totalArr[i][j];
                onlineSum+= onlineArr[i][j];
                if(totalArr[i][j]!=0) {
                    tcount++;
                }
                if(onlineArr[i][j]!=0) {
                    ocount++;
                }
        }
           if(tcount!=0) {
               dayArr[i] = totalSum / tcount;
           }
            if(ocount!=0) {
                avgArr[i] = onlineSum / ocount;
            }

        }*/

        dataMap.put("today",todayArr);
        dataMap.put("yesterday",yesterdayArr);
        return dataMap;
    }
}
