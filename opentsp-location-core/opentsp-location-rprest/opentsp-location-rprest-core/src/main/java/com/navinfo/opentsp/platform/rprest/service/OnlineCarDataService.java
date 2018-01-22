package com.navinfo.opentsp.platform.rprest.service;

import com.navinfo.opentsp.platform.rprest.cache.LastestLocationCache;
import com.navinfo.opentsp.platform.rprest.dto.GetLocationDataDto;
import com.navinfo.opentsp.platform.rprest.entity.OnlineCarCity;
import com.navinfo.opentsp.platform.rprest.entity.OnlineCarCountry;
import com.navinfo.opentsp.platform.rprest.entity.OnlineCarProvinceCity;
import com.navinfo.opentsp.platform.rprest.persisted.DistrictTileRepository;
import com.navinfo.opentsp.platform.rprest.persisted.OnlineCarCountryRepository;
import com.navinfo.opentsp.platform.rprest.persisted.OnlineCarProvinceCityRepository;
import com.navinfo.opentsp.platform.rprest.utils.DistrictCodeUtil;
import com.navinfo.opentsp.platform.rprest.utils.SlippyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by liuwenbin on 2017/5/21.
 */
@Service
public class OnlineCarDataService {

    @Autowired
    private OnlineCarProvinceCityRepository onlineCarProvinceCityRepository;

    @Autowired
    private OnlineCarCountryRepository onlineCarCountryRepository;

    @Autowired
    private DistrictTileRepository districtTileRepository;

    @Value("${opentsp.rprest.service.onlinecar.overgpstime:10}")
    private int gpsOverTime;

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    /**
     * 获取在线车辆数
     */
    public void getOnlineCarData(){
        LOG.info("每天定时跑在线车辆数开始");
        List<GetLocationDataDto> list = LastestLocationCache.getInstance().getCache("location");
        while (list == null || list.size() == 0){
            try{
                list = LastestLocationCache.getInstance().getCache("location");
                Thread.sleep(1000 * 60);
            }catch(Exception e){

            }
        }
        LOG.info("当前车辆:"+list.size());
        //获取在线车辆
        list = getOnlineCarList(list);
        LOG.info("当前在线车辆:"+list.size());
        long startTime = System.currentTimeMillis();
        String date = dateFormat.format(new Date());
        int zoom = 15;
        List<Integer> cityCodeList = null;
        List<String> allTiles = new ArrayList<>();
        Map<String,Integer> tileDistrictMap = new HashMap<>();
        Map<Integer,List<Integer>> provinceCityMap = new HashMap<>();
        Map<Integer,Integer> districtNumberMap = new HashMap<>();
        Set<String> tileToQuerySet = new HashSet<>();
        for(GetLocationDataDto getLocationDataDto : list){
            String tileId = SlippyUtil.getTileID(getLocationDataDto.getLat(),getLocationDataDto.getLng(),zoom);
            allTiles.add(tileId);
            if(null == tileDistrictMap.get(tileId)){
                //从数据库中获取
                tileToQuerySet.add(tileId);
                //累计6000条,从数据库中使用in查询
                if(tileToQuerySet.size() == 6000){
                    //查询数据库,添加到缓存中
                    tileDistrictMap.putAll(districtTileRepository.findIn(tileToQuerySet));
                    tileToQuerySet.clear();
                }
            }else{
                System.out.println("已经存在");
            }
        }

        if(tileToQuerySet.size() != 0){
            //查询数据库,添加到缓存中tileToQuerySet
            tileDistrictMap.putAll(districtTileRepository.findIn(tileToQuerySet));
            tileToQuerySet.clear();
        }
        for(String tileId : allTiles){
            //根据瓦片ID获取行政区域编码
            Integer districtCode = tileDistrictMap.get(tileId);
            if(null == districtCode){
                LOG.info("根据瓦片ID:"+tileId+",获取不到区域编码");
                continue;
            }
            //获取行政区域编码,省份级别
            int provinceCode = DistrictCodeUtil.getProvinceCode(districtCode);
            //整理数据
            //整理省份和城市的对应关系
            if(provinceCityMap.containsKey(provinceCode)){
                cityCodeList = provinceCityMap.get(provinceCode);
                if(!cityCodeList.contains(districtCode)){
                    cityCodeList.add(districtCode);
                    provinceCityMap.put(provinceCode,cityCodeList);
                }
            }else{
                cityCodeList = new ArrayList<>();
                cityCodeList.add(districtCode);
                provinceCityMap.put(provinceCode,cityCodeList);
            }
            //整理城市的车辆数
            if(districtNumberMap.containsKey(districtCode)){
                districtNumberMap.put(districtCode,districtNumberMap.get(districtCode) + 1);
            }else{
                districtNumberMap.put(districtCode,1);
            }
            //整理省份的车辆数
            if(districtNumberMap.containsKey(provinceCode)){
                //对于直辖市的情况,省和城市的区域编码相同,不再累加
                if(provinceCode != districtCode){
                    districtNumberMap.put(provinceCode,districtNumberMap.get(provinceCode) + 1);
                }

            }else{
                districtNumberMap.put(provinceCode,1);
            }
        }
        //封装数据
        //封装省市数据
        int countryNumber = 0;
        OnlineCarProvinceCity onlineCarProvinceCity = null;
        Set<Integer> provinceCodeKeys = provinceCityMap.keySet();
        List<OnlineCarCity> onlineCarCityList = null;
        OnlineCarCity onlineCarCity = null;
        for(Integer provinceCodeKey : provinceCodeKeys){
            cityCodeList = provinceCityMap.get(provinceCodeKey);
            onlineCarProvinceCity = new OnlineCarProvinceCity();
            onlineCarProvinceCity.setDate(date);
            onlineCarProvinceCity.setDistrictCode(provinceCodeKey);
            onlineCarProvinceCity.setNumber(districtNumberMap.get(provinceCodeKey));
            countryNumber += onlineCarProvinceCity.getNumber();
            onlineCarCityList = new ArrayList<>();
            for(Integer cityCode : cityCodeList){
                onlineCarCity = new OnlineCarCity();
                onlineCarCity.setNumber(districtNumberMap.get(cityCode));
                onlineCarCity.setDistrictCode(cityCode);
                onlineCarCityList.add(onlineCarCity);
            }
            onlineCarProvinceCity.setCityList(onlineCarCityList);
            //保存到数据库中
            onlineCarProvinceCityRepository.save(onlineCarProvinceCity);
        }
        //封装全国数据
        OnlineCarCountry onlineCarCountry = new OnlineCarCountry();
        onlineCarCountry.setDate(date);
        onlineCarCountry.setNumber(countryNumber);
        //保存到数据库中
        onlineCarCountryRepository.save(onlineCarCountry);
        LOG.info("每天定时跑在线车辆数结束,耗时: " + (System.currentTimeMillis() - startTime));
    }

    /**
     * 筛选在线车辆
     * @param list 全部车辆
     * @return 在线车辆
     */
    private List<GetLocationDataDto> getOnlineCarList(List<GetLocationDataDto> list){
        List<GetLocationDataDto> onLineCarList = new ArrayList<>();
        long currentTime = System.currentTimeMillis() / 1000;
        System.out.println(currentTime);
        for(GetLocationDataDto getLocationDataDto : list){
            if(getLocationDataDto.getGpsDate() + gpsOverTime * 60 > currentTime){
                onLineCarList.add(getLocationDataDto);
                System.out.println(getLocationDataDto.getGpsDate()+"在线");
            }else{
                System.out.println(getLocationDataDto.getGpsDate()+"离线");
            }
        }
        return onLineCarList;
    }
}