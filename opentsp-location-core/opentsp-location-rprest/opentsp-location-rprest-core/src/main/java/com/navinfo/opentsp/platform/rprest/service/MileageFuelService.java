package com.navinfo.opentsp.platform.rprest.service;

import com.navinfo.opentsp.platform.rprest.GetFuelCommand;
import com.navinfo.opentsp.platform.rprest.constant.Constant;
import com.navinfo.opentsp.platform.rprest.entity.MileageFuel;
import com.navinfo.opentsp.platform.rprest.kit.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: wzw
 * <p> 油耗里程统计分析service
 * Time: 2017/11/29 9:11
 */
@Service
public class MileageFuelService {

    private static final Logger logger = LoggerFactory.getLogger(FaultInfoService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询油耗里程
     * @param command
     * @return
     */
    public List<MileageFuel> getMileageFuel(GetFuelCommand command){
        logger.info("MileageFuelService getMileageFuel start");

        try {

            //30天最多跨度三个月份(二月最少28天)
            int count = getMonthCount(command.getStart(),command.getEnd());
            //通信号
            long id = command.getId();
            List<MileageFuel> mileageFuelList = new ArrayList<>();

            //时间字符串转换成long型便于检索
            long startTime = DateUtils.getTime(Constant.DATE_PATTERN, command.getStart());
            long endTime = DateUtils.getTime(Constant.DATE_PATTERN,command.getEnd());

            //开始日期转换成Date类型
            Date startDate = DateUtils.parse(Constant.DATE_PATTERN, command.getStart());

            ZoneId zoneId = ZoneId.systemDefault();

            if(count == 1){
                //表名
                String tableName = Constant.MILEAGE_FUEL + command.getStart().substring(0,7);
                mileageFuelList = getMileageFuelList(id,startTime,endTime,tableName);
                logger.info("count = 1 获取mongo数据完毕");
            }
            if(count == 2){
                //第一个月
                Instant startInstant = startDate.toInstant();
                LocalDate startLocalDate = startInstant.atZone(zoneId).toLocalDate();
                //开始日期所在月的最后一天
                LocalDate lastDayOfFirst = startLocalDate.with(TemporalAdjusters.lastDayOfMonth());
                ZonedDateTime zdt = lastDayOfFirst.atStartOfDay(zoneId);
                //转换成毫秒
                long lastDayOfFirstTime = Date.from(zdt.toInstant()).getTime();

                String firstTableName = Constant.MILEAGE_FUEL + command.getStart().substring(0,7);
                mileageFuelList.addAll(getMileageFuelList(id, startTime, lastDayOfFirstTime, firstTableName));
                logger.info("count = 2 获取mongo第一个月数据完毕");

                //第二个月
                //第二个月第一天
                long secondFirstDay = lastDayOfFirstTime + 24*60*60*1000;
                String secondTableName = Constant.MILEAGE_FUEL + command.getEnd().substring(0,7);
                mileageFuelList.addAll(getMileageFuelList(id, secondFirstDay, endTime, secondTableName));
                logger.info("count = 2 获取mongo第二个月数据完毕");
            }
            if(count == 3){
                //第一个月
                Instant startInstant = startDate.toInstant();
                LocalDate startLocalDate = startInstant.atZone(zoneId).toLocalDate();
                //开始日期所在月的最后一天
                LocalDate lastDayOfFirst = startLocalDate.with(TemporalAdjusters.lastDayOfMonth());
                ZonedDateTime zdt = lastDayOfFirst.atStartOfDay(zoneId);
                //转换成毫秒
                long lastDayOfFirstTime = Date.from(zdt.toInstant()).getTime();

                String firstTableName = Constant.MILEAGE_FUEL + command.getStart().substring(0,7);
                mileageFuelList.addAll(getMileageFuelList(id, startTime, lastDayOfFirstTime, firstTableName));
                logger.info("count = 3 获取mongo第一个月数据完毕");
             
                //第二个月
                long secondFirstDay = lastDayOfFirstTime + 24*60*60*1000;
                //第二个月最后一天
                LocalDate secondLastDay = lastDayOfFirst.plusDays(1).with(TemporalAdjusters.lastDayOfMonth());
                ZonedDateTime secondZdt = secondLastDay.atStartOfDay(zoneId);
                //转换成毫秒
                long secondlastDayOfTime = Date.from(secondZdt.toInstant()).getTime();

                String secondTableName = Constant.MILEAGE_FUEL + (DateUtils.format(Constant.DATE_PATTERN,secondFirstDay).substring(0, 7));
                mileageFuelList.addAll(getMileageFuelList(id, secondFirstDay, secondlastDayOfTime, secondTableName));
                logger.info("count = 3 获取mongo第二个月数据完毕");

                //第三个月
                //第三个月第一天
                long thirdFirstDay = secondlastDayOfTime + 24*60*60*1000;
                String thirdTableName = Constant.MILEAGE_FUEL + command.getEnd().substring(0, 7);
                mileageFuelList.addAll(getMileageFuelList(id, thirdFirstDay, endTime, thirdTableName));
                logger.info("count = 3 获取mongo第三个月数据完毕");

            }

            logger.info("MileageFuelService getMileageFuel end");
            return mileageFuelList;
        }catch (Exception e){
            logger.error(e.getMessage() + "MileageFuelService getMileageFuel");
            throw e;
        }
    }

    /**
     * 获取一段时期内包含月份个数
     * @param start 开始日期
     * @param end 结束日期
     * @return
     */
    private int getMonthCount(String start ,String end){
        Date startDate = DateUtils.parse(Constant.DATE_PATTERN, start);
        Date endDate = DateUtils.parse(Constant.DATE_PATTERN, end);

        Instant startInstant = startDate.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate startLocalDate = startInstant.atZone(zoneId).toLocalDate();
        Instant endInstant = endDate.toInstant();
        LocalDate endLocalDate = endInstant.atZone(zoneId).toLocalDate();
        //
        Period period = Period.between(startLocalDate, endLocalDate);
        //包含月份个数
        int count = period.getMonths();
        return count;
    }

    /**
     * 获取一段时间内里程油耗数据（不能跨月）
     * @param id
     * @param startTime
     * @param endTime
     * @param tableName
     * @return
     */
    private List<MileageFuel> getMileageFuelList(long id,long startTime,long endTime,String tableName){
        try{
            Criteria criteria = new Criteria();
            //查询条件
            criteria.andOperator(
                    Criteria.where("id").in(id),
                    Criteria.where("dateUTC").gte(startTime),
                    Criteria.where("dateUTC").lte(endTime));

            Query query = new Query(criteria);
            List<MileageFuel> mileageFuelList = mongoTemplate.find(query, MileageFuel.class, tableName);
            return mileageFuelList;
        }catch (Exception e){
            logger.error(e.getMessage() + "MileageFuelService getMileageFuelList");
            throw e;
        }

    }
}
