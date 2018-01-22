package com.navinfo.opentsp.platform.rprest.service;

import com.mongodb.BasicDBList;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCVehiclePassInAreaInfo;
import com.navinfo.opentsp.platform.rprest.cache.TerminalRuleCache;
import com.navinfo.opentsp.platform.rprest.cache.VehiclePassInAreaCache;
import com.navinfo.opentsp.platform.rprest.dto.ServiceMapDto;
import com.navinfo.opentsp.platform.rprest.dto.ServiceStatisticsDto;
import com.navinfo.opentsp.platform.rprest.kit.DateUtils;
import com.navinfo.opentsp.platform.rprest.kit.VehiclePassInAreaEntity;
import com.navinfo.opentsp.platform.rprest.persisted.ServiceStatisticsRepository;
import org.hibernate.validator.constraints.ModCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by HOUQL on 2017/5/23.
 */
@Service
public class StationStaticService {
    private static final Logger log = LoggerFactory.getLogger(ServiceStatisticsRepository.class);
    @Autowired
    protected ServiceStatisticsRepository serviceStatisticsRepository;
    @Autowired
    protected TerminalRuleCache terminalRuleCache;
    @Autowired
    protected VehiclePassInAreaCache vehiclePassInAreaCache;

    @PostConstruct
    public void init() {
        terminalRuleCache.reload();
        vehiclePassInAreaCache.reload();
    }

    public List<ServiceStatisticsDto> GetVehiclePassInArea(int district, int type, String startDate, String endDate) {
        List<ServiceStatisticsDto> listResult = new ArrayList<>();
        terminalRuleCache.getAreaInfoForStatistic();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long starttimeLong = 0;
        long endtimeLong = 0;
        try {
            Date star = sf.parse(startDate + " 00:00:00");
            starttimeLong = star.getTime() / 1000;
            Date end = sf.parse(endDate + " 23:59:59");
            endtimeLong = end.getTime() / 1000;
        } catch (Exception e) {
            // TODO: handle exception
        }
        Map<Long, VehiclePassInAreaEntity> map = serviceStatisticsRepository.GetServiceStatistics(district, type, starttimeLong, endtimeLong, "GetVehiclePassInArea");
        if (map != null && map.size() > 0) {
            log.info("区域：" + district + "下共查询到" + map.size() + "条数据");
        }
//在这里根据次数进行排序，返回次数多的数据
        List<Entry<Long, VehiclePassInAreaEntity>> list =
                new ArrayList<Map.Entry<Long, VehiclePassInAreaEntity>>(map.entrySet());
//        最后通过Collections.sort(List l, Comparator c)方法来进行排序，
// 代码如下：

        Collections.sort(list, new Comparator<Map.Entry<Long, VehiclePassInAreaEntity>>() {
            public int compare(Map.Entry<Long, VehiclePassInAreaEntity> o1,
                               Map.Entry<Long, VehiclePassInAreaEntity> o2) {
                return (o2.getValue().getTimes() - o1.getValue().getTimes());
            }
        });
        int index = 0;
        for (Entry<Long, VehiclePassInAreaEntity> maping : list) {
            if (index > 5) break;
            ServiceStatisticsDto serviceStatisticsDto = new ServiceStatisticsDto();
            serviceStatisticsDto.setDistrict(maping.getKey().intValue());
            serviceStatisticsDto.setNumber(maping.getValue().getTimes());
            listResult.add(serviceStatisticsDto);
            index++;
        }
        return listResult;
    }


    public List<ServiceMapDto> GetVehicleNearByStation( int type, String startDate, String endDate) {
        List<ServiceMapDto> listResult = new ArrayList<>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long starttimeLong = 0;
        long endtimeLong = 0;
        try {
            Date star = sf.parse(startDate + " 00:00:00");
            starttimeLong = star.getTime() / 1000;
            Date end = sf.parse(endDate + " 23:59:59");
            endtimeLong = end.getTime() / 1000;
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {


            boolean dataInRedisRequired = false;
            long todayEnd = DateUtils.getTodayEnd();
            long todayBegin = DateUtils.getTodayBegin();
            Map<Long, VehiclePassInAreaEntity> map = new HashMap<Long, VehiclePassInAreaEntity>();
            //历史数据从mongo中获取
            if (starttimeLong < todayBegin) {
                map = serviceStatisticsRepository.GetServiceStatistics(-1, type, starttimeLong, endtimeLong, "GetVehicleNearByStation");
                if (map != null && map.size() > 0) {
//                    log.info("区域：" + district + "下共查询到" + map.size() + "条数据");
                }
            } else {
                dataInRedisRequired = true;
            }
            if (endtimeLong > todayBegin) dataInRedisRequired = true;
            Map<Integer, LCVehiclePassInAreaInfo.VehiclePassInAreaInfo> todayPass = new HashMap<>();
            if (dataInRedisRequired) {//查询今天服务站周边数据
            todayPass = vehiclePassInAreaCache.getPassAreaInfoForStatistic();
//                todayPass = getPassTimesByDistrict(district, type, todayPassAll);
            }
            //将历史数据与今天数据进行合并。
            if (map.size() > 0) {
                for (Entry<Long, VehiclePassInAreaEntity> entry : map.entrySet()) {
                    //先遍历历史数据
                    if (todayPass.containsKey(entry.getKey().intValue())) {
                        VehiclePassInAreaEntity vehiclePassInAreaEntity = entry.getValue();
                        LCVehiclePassInAreaInfo.VehiclePassInAreaInfo passInAreaInfo = todayPass.get(entry.getKey().intValue());
                        vehiclePassInAreaEntity.setTimes(vehiclePassInAreaEntity.getTimes() + passInAreaInfo.getVehicleInfoList().size());
                    }
                }
            }
            if (todayPass.size() > 0) {
                Integer tempKey = 0;

                for (Entry<Integer, LCVehiclePassInAreaInfo.VehiclePassInAreaInfo> entry : todayPass.entrySet()) {
                    tempKey = entry.getKey().intValue();
//                    //先遍历今天数据
//                    if (type == 0) {
//                        //如果查询全国数据，则返回各省份的统计数据。此时历史数据区域编码是全国，
//                        // 需要将今天服务站周边次数映射到省份。
//                        if (areaProvinceMap.containsKey(entry.getKey())) {
//                            tempKey = areaProvinceMap.get(entry.getKey()).intValue();
//                        }
//
//                    }
                    if (map.containsKey(tempKey)) {
                        VehiclePassInAreaEntity vehiclePassInAreaEntity = map.get(tempKey);
                        LCVehiclePassInAreaInfo.VehiclePassInAreaInfo passInAreaInfo = entry.getValue();
                        vehiclePassInAreaEntity.setTimes(vehiclePassInAreaEntity.getTimes() + passInAreaInfo.getVehicleInfoList().size());
                    } else {
                        VehiclePassInAreaEntity vehiclePassInAreaEntity = new VehiclePassInAreaEntity();
                        LCVehiclePassInAreaInfo.VehiclePassInAreaInfo passInAreaInfo = entry.getValue();
                        vehiclePassInAreaEntity.setTimes(vehiclePassInAreaEntity.getTimes() + passInAreaInfo.getVehicleInfoList().size());
                        map.put(new Long(tempKey), vehiclePassInAreaEntity);
                    }
                }
            }
            //在这里根据次数进行排序，返回次数多的数据
            List<Entry<Long, VehiclePassInAreaEntity>> list =
                    new ArrayList<Map.Entry<Long, VehiclePassInAreaEntity>>(map.entrySet());
//        最后通过Collections.sort(List l, Comparator c)方法来进行排序，
// 代码如下：

            Collections.sort(list, new Comparator<Map.Entry<Long, VehiclePassInAreaEntity>>() {
                public int compare(Map.Entry<Long, VehiclePassInAreaEntity> o1,
                                   Map.Entry<Long, VehiclePassInAreaEntity> o2) {
                    return (o2.getValue().getTimes() - o1.getValue().getTimes());
                }
            });

            int totalDays = 0;
            //计算查询的天数
            Map<String, List<String>> listDates = DateUtils.splitWithDays(starttimeLong, endtimeLong);
            for (Entry<String, List<String>> entry : listDates.entrySet()) {
                totalDays += entry.getValue().size();
            }
            totalDays = totalDays == 0 ? 1 : totalDays;

            Map<Integer, LCAreaInfo.AreaInfo> areaInfoMap = terminalRuleCache.getAreaInfoForStatistic();
            Map<Integer, Integer> areaProvinceMap = terminalRuleCache.getAreaProvinceMap();
            Map<Integer, ServiceMapDto> provinceTimeMap = new ConcurrentHashMap<>();
            for (Entry<Long, VehiclePassInAreaEntity> maping : list) {
                Integer tempKey = 0;
                ServiceMapDto serviceStatisticsDto = new ServiceMapDto();
                if (type == 0) {
                    //如果查询全国数据，则返回各省份的统计数据。此时历史数据区域编码是全国，
                    // 需要将今天服务站周边次数映射到省份。
                    if (areaProvinceMap.containsKey(maping.getKey().intValue())) {
                        tempKey = areaProvinceMap.get(maping.getKey().intValue());
                    }
                    serviceStatisticsDto.setDistrict(tempKey);
                    if (provinceTimeMap.containsKey(tempKey)) {
                        serviceStatisticsDto = provinceTimeMap.get(tempKey);
                    }
                    serviceStatisticsDto.setNumber(serviceStatisticsDto.getNumber() + maping.getValue().getTimes());
                    provinceTimeMap.put(tempKey, serviceStatisticsDto);

                } else {
                    serviceStatisticsDto.setDistrict(maping.getKey().intValue());
                    serviceStatisticsDto.setNumber(maping.getValue().getTimes() / totalDays);
                    if (areaInfoMap.containsKey(maping.getKey().intValue())) {
                        LCAreaInfo.AreaInfo areaInfo = areaInfoMap.get(maping.getKey().intValue());
                        serviceStatisticsDto.setLat(areaInfo.getDatas(0).getLatitude() / Math.pow(10, 6));
                        serviceStatisticsDto.setLng(areaInfo.getDatas(0).getLongitude() / Math.pow(10, 6));
                    }
                    listResult.add(serviceStatisticsDto);
                }
            }
            if (type == 0) {
                for (Entry<Integer, ServiceMapDto> dtoEntry : provinceTimeMap.entrySet()) {
                    ServiceMapDto serviceStatisticsDto = dtoEntry.getValue();
                    serviceStatisticsDto.setNumber(serviceStatisticsDto.getNumber() / totalDays);
                    listResult.add(serviceStatisticsDto);
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            log.error(e.getMessage());
        }
        return listResult;
    }

    private Map<Integer, LCVehiclePassInAreaInfo.VehiclePassInAreaInfo> getPassTimesByDistrict(int districtCode, int type, Map<Integer, LCVehiclePassInAreaInfo.VehiclePassInAreaInfo> passTimesAll) {
        if (districtCode == 0 && type == 0) {
            return passTimesAll;
        }
        Map<Integer, LCVehiclePassInAreaInfo.VehiclePassInAreaInfo> passInAreaInfoMap=new ConcurrentHashMap<>();
        if (type == 1 || type == 2) {
            Map<Integer, List<Integer>> districtMap = terminalRuleCache.getDistrictAreaMap();
//                如果查询省、市下服务站，需要开启模糊匹配。省：匹配左边两个字符，如100000；市，则匹配左侧四个字符。
            String filterDistrict = "";
            if (type == 1) {
                filterDistrict = String.valueOf(districtCode).substring(0, 2);
            }
            if (type == 2) {
                filterDistrict = String.valueOf(districtCode).substring(0, 4);
            }
            BasicDBList areaList = new BasicDBList();
            List<Integer> areaIDList = new ArrayList<>();
            //遍历查找省市，查找该省市下的所有服务站
            for (Entry<Integer, List<Integer>> districtEntry : districtMap.entrySet()) {
                String disctictCode = districtEntry.getKey().toString();
                if (disctictCode.startsWith(filterDistrict)) {
                    areaIDList.addAll(districtEntry.getValue());
                }
            }
            for (Integer areaID:areaIDList){
                if(passTimesAll.containsKey(areaID)) {
                    passInAreaInfoMap.put(areaID,passTimesAll.get(areaID));
                }
            }
        }
        return passInAreaInfoMap;
    }
}
