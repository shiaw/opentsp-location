package com.navinfo.opentsp.platform.rprest.cache;

import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TermianlRuleAndParaService;
import com.navinfo.opentsp.platform.rprest.persisted.DistrictTileRepository;
import com.navinfo.opentsp.platform.rprest.rmiclient.DARmiClient;
import com.navinfo.opentsp.platform.rprest.rmiclient.RmiConstant;
import com.navinfo.opentsp.platform.rprest.utils.DistrictCodeUtil;
import com.navinfo.opentsp.platform.rprest.utils.SlippyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 初始化规则数据，目前需要加载的有滞留超时阀值、服务站区域（用来做车次统计）。
 *
 * @author hk
 */
@Component
public class TerminalRuleCache {

    private Logger logger = LoggerFactory.getLogger(TerminalRuleCache.class);
    private volatile List<AreaInfo> areas = new ArrayList<AreaInfo>();

    private Map<Integer,AreaInfo> areaMaps=new ConcurrentHashMap<>();
    private volatile Map<Long, Integer> areaOverTimeCache = new HashMap<>();

    /**
     * 城市与服务站ID的映射关系
     */
    private Map<Integer, List<Integer>> districtAreaMap = new ConcurrentHashMap<>();
    /**
     * 服务站ID与瓦片映射关系
     */
    /**
     * 服务站ID与省的映射关系
     */
    private Map<Integer, Integer> areaProvinceMap = new ConcurrentHashMap<>();
    /**
     * 服务站ID与瓦片映射关系
     */


    private List<String> allTiles = new ArrayList<>();
    private Map<String, Integer> tileDistrictMap = new HashMap<>();
    @Autowired
    private DARmiClient rmiclient;
    private Object mux = new Object();
    @Autowired
    private DistrictTileRepository districtTileRepository;

    private void initAreaInfoForStatistic() {
        try {
            logger.info("开始加载[服务站缓存]...");
            TermianlRuleAndParaService currentServer = (TermianlRuleAndParaService) rmiclient.rmiBalancerRequest(RmiConstant.QUERY_TER_RULES);
            // 加载服务站车次统计使用信息。
            List<AreaInfo> areaInfos = currentServer.getAreaInfoForStatistic();
            if (null != areaInfos && areaInfos.size() > 0) {
                for (AreaInfo areaInfo:areaInfos){
                    areaMaps.put(new Long(areaInfo.getAreaIdentify()).intValue(),areaInfo);
                }
                areas = areaInfos;
                logger.info("加载[服务站缓存]完成，共加载数据条数：" + areas.size());
            } else {
                logger.warn("加载[服务站缓存]出错，加载结果为空，请检查");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Integer,AreaInfo> getAreaInfoForStatistic() {
        if (areas.size() < 1) {
            synchronized (mux) {
                if (areas.size() < 1) {
                    initAreaInfoForStatistic();
                }
            }
        }
        return areaMaps;
    }

    public Map<Integer, List<Integer>> getDistrictAreaMap() {
        synchronized (mux) {
            initMap();
        }
        return districtAreaMap;
    }

    public Map<Integer, Integer> getAreaProvinceMap() {
        synchronized (mux) {
            initMap();
        }
        return areaProvinceMap;
    }

    private void initMap() {
        if (areas.size() > 1) {
            Set<String> tileToQuerySet = new HashSet<>();
            Map<Integer, String> areaMapID = new HashMap<>();
            for (AreaInfo areaInfo : areas) {
                double lat = ((double) areaInfo.getDatas(0).getLatitude()) / Math.pow(10, 6);
                double lon = ((double) areaInfo.getDatas(0).getLongitude()) / Math.pow(10, 6);
                String tileId = SlippyUtil.getTileID(lat, lon, 15);
                areaMapID.put(new Long(areaInfo.getAreaIdentify()).intValue(), tileId);
                allTiles.add(tileId);
                if (null == tileDistrictMap.get(tileId)) {
                    //从数据库中获取
                    tileToQuerySet.add(tileId);
                    //累计6000条,从数据库中使用in查询
                    if (tileToQuerySet.size() == 6000) {
                        //查询数据库,添加到缓存中
                        tileDistrictMap.putAll(districtTileRepository.findIn(tileToQuerySet));
                        tileToQuerySet.clear();
                    }
                }
            }
            if (tileToQuerySet.size() != 0) {
                //查询数据库,添加到缓存中tileToQuerySet
                tileDistrictMap.putAll(districtTileRepository.findIn(tileToQuerySet));
                tileToQuerySet.clear();
                tileToQuerySet = null;
            }

            for (Map.Entry<Integer, String> entry : areaMapID.entrySet()) {
                Integer districtCode = tileDistrictMap.get(entry.getValue());
                if(districtCode==null) continue;
                List<Integer> areaList = new ArrayList<>();
                //市与服务站关系
                if (districtAreaMap.containsKey(districtCode)) {
                    areaList = districtAreaMap.get(districtCode);
                }
                if (!areaList.contains(entry.getKey())) {
                    areaList.add(entry.getKey());
                }
                districtAreaMap.put(districtCode, areaList);

                int provinceCode = DistrictCodeUtil.getProvinceCode(districtCode);
                List<Integer> provincoAreaList = new ArrayList<>();
                if (!areaProvinceMap.containsKey(provinceCode)) {
                    areaProvinceMap.put(entry.getKey(), provinceCode);
                }
            }
//            for (String tileId : allTiles) {
//                //根据瓦片ID获取行政区域编码
//                Integer districtCode = tileDistrictMap.get(tileId);
//                List<Integer> areaList = new ArrayList<>();
//                if (districtAreaMap.containsKey(districtCode)) {
//                    areaList = districtAreaMap.get(districtCode);
//                }
////                if (areaList.contains())
//
//            }
        }
    }

    public void reload() {
        areas.clear();
        getAreaInfoForStatistic();
        initMap();

    }
}
