package com.navinfo.opentsp.platform.rprest.scheduler;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.platform.configuration.OpentspQueues;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCMileageConsumption;
import com.navinfo.opentsp.platform.rprest.cache.*;
import com.navinfo.opentsp.platform.rprest.dto.GetLocationDataDto;
import com.navinfo.opentsp.platform.rprest.entity.OnlineHour;
import com.navinfo.opentsp.platform.rprest.entity.OnlineStatisticsEntity;
import com.navinfo.opentsp.platform.rprest.service.OnlineCarDataService;
import com.navinfo.opentsp.platform.rprest.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wanliang on 2017/5/19.
 */
@Component
public class RpRestScheduler {
    private static final Logger log = LoggerFactory.getLogger(RpRestScheduler.class);
    public static long packetCount=0;
    private final RedisTemplate<String, Object> redisTemplate;
    @Autowired
    MessageChannel messageChannel;

    @Autowired
    private RedisTerminalInfoStorage redisTerminalInfoStorage;

    @Autowired
    private RedisOnlineStatusStorage redisOnlineStatusStorage;

    @Autowired
    private RedisLastLocationStorage redisLastLocationStorage;

    @Autowired
    private TerminalRuleCache terminalRuleCache;

    private final String PAKCET_3002_COUNT_KEY="totalMileageAndPackage";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private VehiclePassInAreaCache vehiclePassInAreaCache;
    @Autowired
    private OnlineCarDataService onlineCarDataService;
    @Autowired
    private RedisMileageStorage mileageStorage;

    @Value("${opentsp.3002.packtet.count.start:0}")
    private long packet3002StartCount;

    private final String queueName;

    @Autowired
    public RpRestScheduler(RedisConnectionFactory factory,
                           @Qualifier(OpentspQueues.PERSONAL)
                                   Queue queue) {
        this.redisTemplate = new RedisTemplate<>();
        this.redisTemplate.setConnectionFactory(factory);
        this.redisTemplate.afterPropertiesSet();
        this.queueName=queue.getName();
    }

    @Scheduled(cron = "${opentsp.rprest.scheduler.clear.cron:0 0 0 * * ?}")
    public void clearPacketMilCache(){
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put(PAKCET_3002_COUNT_KEY,"packageNum", 0l);
        hashOperations.put(PAKCET_3002_COUNT_KEY,"mileage", 0l);
       // hashOperations.put(PAKCET_3002_COUNT_KEY,"driveNum", 0);
       // hashOperations.put(PAKCET_3002_COUNT_KEY,"onlineNum", 0);
    }

    @Scheduled(cron = "${opentsp.rprest.scheduler.packetCount.cron:0 0/1 * * * ?}")
    public void synPacketCount() {
        HashOperations hashOperations = redisTemplate.opsForHash();
        Long val = (Long) hashOperations.get("totalMileageAndPackage","packageNum");
        //第一次，初始化数据
        /*if (val == null || val == 0l) {
            long count = packet3002StartCount + packetCount;
            packetCount = 0;
            hashOperations.put(PAKCET_3002_COUNT_KEY,"packageNum", count);
            log.info("执行数据存储操作：当前包数：{}", count);
        } else {*/
        if (val == null){
            val = 0l;
        }
        long count = val + packetCount;
        packetCount = 0;
        hashOperations.put(PAKCET_3002_COUNT_KEY,"packageNum", count);
        log.info("执行数据存储操作：当前包数：{}", count);
       // }

    }

    /**
     * 定时执行在线车辆数据统计，1小时统计一次
     */
    @Scheduled(cron = "${opentsp.rprest.scheduler.carsonline.cron:0 0 0/1 * * ?}")
    public void onlineNum(){
        long startTime=System.currentTimeMillis();
        List<TerminalInfo> list= redisTerminalInfoStorage.getAll();
        List<DeviceRegistration> deviceRegistrationList=redisOnlineStatusStorage.getAll();
        log.info("当前终端总数：{}", list.size());
        Integer onlineCount=0;
        for(DeviceRegistration deviceRegistration:deviceRegistrationList){
            if(deviceRegistration.getStatus().getValue()== DeviceStatus.ONLINE.getValue()){
                onlineCount++;
            }
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMdd");
        String day=simpleDateFormat.format(new Date());
        Query query=new Query(Criteria.where("day").is(day));
        OnlineStatisticsEntity onlineStatisticsEntity= mongoTemplate.findOne(query,OnlineStatisticsEntity.class);
        //判断当前时间是否需要补全数据
        SimpleDateFormat simpleDateFormatHour=new SimpleDateFormat("HH");
        int hour=Integer.parseInt(simpleDateFormatHour.format(new Date()));
        log.info("当前小时：{}",hour);
        if(onlineStatisticsEntity==null){
            onlineStatisticsEntity=new OnlineStatisticsEntity();
            List<OnlineHour> onlineHours=new ArrayList<>();
            onlineStatisticsEntity.setDay(day);
            if(onlineHours.size()<hour-1){
                int hourCount=hour-1-onlineHours.size();
                for(int i=0 ;i<hourCount;i++) {
                    onlineHours.add(new OnlineHour(0, 0));
                }
            }
            onlineHours.add(new OnlineHour(list.size(),onlineCount));
            onlineStatisticsEntity.setData(onlineHours);
        }else{
            int hourCount=hour-onlineStatisticsEntity.getData().size()-1;
            if(onlineStatisticsEntity.getData().size()< hour-1){
                for(int i=0 ;i<hourCount;i++) {
                    onlineStatisticsEntity.getData().add(new OnlineHour(0, 0));
                }
            }
            onlineStatisticsEntity.getData().add(new OnlineHour(list.size(),onlineCount)) ;
            log.info("追加数据");
        }
        mongoTemplate.save(onlineStatisticsEntity);
        log.info("当前终端在线数：{}，执行耗时：{}ms", deviceRegistrationList.size(),System.currentTimeMillis()-startTime);
    }

    /**
     * 每天凌晨5点读取整理车辆在线数
     */
    @Scheduled(cron = "${opentsp.rprest.scheduler.onlinecar.cron:0 0 5 * * ?}")
    public void onlineCar(){
        onlineCarDataService.getOnlineCarData();
    }
    @Scheduled(cron = "${opentsp.rprest.scheduler.passInArea.cron:0 0/5 * * * ?}")
    public void passInArea(){
        vehiclePassInAreaCache.reload();
        terminalRuleCache.reload();
    }
    @Scheduled(cron = "${opentsp.rprest.scheduler.mileageCan.cron:0 0/1 * * * ?}")
    public void mileageCan(){
        RedisUtil util = new RedisUtil();
        HashOperations hashOperations = redisTemplate.opsForHash();
        Map<byte[], byte[]> data = (Map<byte[], byte[]>) mileageStorage.getAll();
        long currentTime = System.currentTimeMillis()/1000;
        double bzMil = 0;
        for(Map.Entry<byte[],byte[]>  entry : data.entrySet()) {
            double mileage = 0;
            String key = util.byte2string(entry.getKey());
            LCMileageConsumption.MileageConsumption mileageConsumption = null;
            try {
                mileageConsumption = LCMileageConsumption.MileageConsumption.parseFrom(entry.getValue());
                if (mileageConsumption != null){
                    if (DateUtils.differDay(DateUtils.calendar(mileageConsumption.getStartDate()),
                            DateUtils.calendar(currentTime)) == 0){
                        if (mileageConsumption.getMeterMileage()>0){
                            mileage = (double)mileageConsumption.getMeterMileage()/1000;
                        }else if (mileageConsumption.getMileage()>0){
                            mileage = (double)mileageConsumption.getMileage()/1000;
                        }else if (mileageConsumption.getTerminalMileage()>0){
                            mileage = (double)mileageConsumption.getTerminalMileage()/1000;
                        }
                        bzMil+=mileage;
                    }
                }
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        }
        hashOperations.put(PAKCET_3002_COUNT_KEY,"mileage", (long)bzMil);
        log.info("执行数据存储操作：当日行驶总里程：{}KM", bzMil);
    }
    @Scheduled(cron = "${opentsp.rprest.scheduler.totalmileage.cron:0 0/5 * * * ?}")
    public void mileageTotal(){
        RedisUtil util = new RedisUtil();
        List<GetLocationDataDto> list = new ArrayList<>();
        HashOperations hashOperations = redisTemplate.opsForHash();
        long currentDate = System.currentTimeMillis()/1000;
        long mileage = 0l;
        int onlineNum = 0;
        long driverNum = 0l;
        Map<byte[], byte[]> data = (Map<byte[], byte[]>) redisLastLocationStorage.getAll();
        //遍历过程中，处理两个接口的问题，一个是计算里程数和在线数等。一个是5分钟存一次本地缓存。
        for(Map.Entry<byte[],byte[]>  entry : data.entrySet()){
            String key = util.byte2string(entry.getKey());
            LCLocationData.LocationData locationData = null;
            try {
                locationData = LCLocationData.LocationData.parseFrom(entry.getValue());
                //判断末次位置时间点与当前时间点相差，大于5分钟则判车辆不在线
                if ((currentDate -locationData.getGpsDate())/60 < 5){
                    onlineNum++;
                    if (locationData.getSpeed() > 0){
                        driverNum++;
                    }
                }
                /*for (LCVehicleStatusData.VehicleStatusData statusData : locationData.getStatusAddition().getStatusList()){
                    if (statusData.getTypes() == LCStatusType.StatusType.mileage&&statusData.getStatusValue()>0){
                        mileage+=statusData.getStatusValue();
                    }
                }*/

                GetLocationDataDto dto = new GetLocationDataDto();
                dto.setGpsDate(locationData.getGpsDate());
                dto.setLat(((double)locationData.getLatitude())/1000000);
                dto.setLng((double)locationData.getLongitude()/1000000);
                dto.setTid(Long.parseLong(key));
                list.add(dto);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        }
        LastestLocationCache.getInstance().addCache(list);
        //hashOperations.put(PAKCET_3002_COUNT_KEY,"mileage", mileage/1000);
        hashOperations.put(PAKCET_3002_COUNT_KEY,"driveNum", driverNum);
        hashOperations.put(PAKCET_3002_COUNT_KEY,"onlineNum", onlineNum);
        log.info("执行数据存储操作：当前总里程：{}KM,当前车辆在线数：{},当前车辆行驶数：{}", mileage/1000,onlineNum,driverNum);

    }
}
