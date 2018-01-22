package com.navinfo.opentsp.platform.dsa.service.impl.offline;

import com.navinfo.opentsp.platform.dsa.rmiclient.RmiConstant;
import com.navinfo.opentsp.platform.dsa.service.impl.ConcurrentAlgOffline;
import com.navinfo.opentsp.platform.dsa.service.interf.Offline;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.StatisticResultEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesQuadtree;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesTreeNode;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author HK 13级瓦片边长大概在4.8公里 瓦片和行政编码映射关系,TMS 直辖市 编码为省编码,父编码为0 地级市
 *         编码为市编码,父编码为省编码 9/10/11,13/14/15
 *         <p>
 *         热力图在统计过程中完成了相同瓦片ID的数据合并。
 */
@Component
@Scope("prototype")
public class VehiclePassGridTimes extends ConcurrentAlgOffline implements Offline {
    private final static int minZoom = 15;
    private final static int provinceZoom = 9;
    private final static int cityZoom = 13;
    public static Logger logger = LoggerFactory.getLogger(VehiclePassGridTimes.class);

    private static void vehiclePassTimesUpdate(VehiclePassTimesTreeNode node, long tid) {
        if (node != null) {
            Map<Long, Integer> terminals = node.getTerminals();
            if (terminals.size() >= 0 && terminals.get(tid) == null) {
                terminals.put(tid, 1);
                node.setTimes(node.getTimes() + 1);
            }
        } else {
            logger.error("四叉树未构建.");
        }
    }

    private static VehiclePassTimesTreeNode getQuadtree(List<VehiclePassTimesTreeNode> list, long tileId, int district,
                                                        long begin) {
        for (VehiclePassTimesTreeNode root : list) {
            if (root.getTileId() == tileId) {
                return root;
            }
        }
        VehiclePassTimesQuadtree tree = new VehiclePassTimesQuadtree(3);
        int[] zxy = VehiclePassTimesQuadtree.tileIdTozxy(tileId);
        VehiclePassTimesTreeNode root = tree.creatRoot(zxy[0], zxy[1], zxy[2], 0, getMonth(begin), district);
        tree.createChildrenByDepth(root);
        list.add(root);
        return root;
    }

    private static int getMonth(long begin) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(begin * 1000);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int result = year * 10000 + month * 100;
        if (day <= 10) {
            result += 1;
        } else if (day <= 20) {
            result += 2;
        } else {
            result += 3;
        }
        return result;
    }

    public void saveResult(StatisticResultEntity list) throws Exception {
        long startLong = System.currentTimeMillis();
        List<VehiclePassTimesTreeNode> grids = list.getGrids();
        AlarmStatisticsStoreService daServer = (AlarmStatisticsStoreService) getRmiclient().rmiBalancerRequest(RmiConstant.SAVE_STATIC_TER_DATA);
        PlatformResponseResult faultResult = daServer.saveVehiclePassGridTimes(grids);
        if (PlatformResponseResult.success_VALUE != faultResult.getNumber()) {
            logger.error("grids store failure.");
        }
        logger.debug(">>>区域车次统计数据存儲耗时:{},总结果数:{}", (System.currentTimeMillis() - startLong) / 1000.0, grids.size());
    }

    @Override
    public void execute(Long tid, List<LocationData> gpsdata, StatisticResultEntity result) {
        if (null == gpsdata || gpsdata.size() == 0) {
            return;
        }
        // 所有的行政区域和瓦片的映射
        Map<Long, Integer[]> districtAndTileMap = districtAndTileMappingCache.getAllMap();
        for (LocationData location : gpsdata) {

            // 获取行政区域
            if (location.getSpeed() < 5) {
                continue;
            }
            long tileIdMin = VehiclePassTimesQuadtree.getTileNumber(location.getOriginalLat(),
                    location.getOriginalLng(), minZoom);
            Integer[] district = districtAndTileMap.get(tileIdMin);
            if (district == null) {
                continue;
            }
            // 省级网格统计
            long tileIdProvince = VehiclePassTimesQuadtree.getTileNumber(location.getOriginalLat(),
                    location.getOriginalLng(), provinceZoom);
            VehiclePassTimesTreeNode provinceTree;
            if (district[1] != 0) {
                provinceTree = getQuadtree(result.getGrids(), tileIdProvince, district[1], st);
            } else {
                provinceTree = getQuadtree(result.getGrids(), tileIdProvince, district[0], st);
            }
            long tileIdProvinceMin = VehiclePassTimesQuadtree.getTileNumber(location.getOriginalLat(),
                    location.getOriginalLng(), provinceZoom + 2);
            VehiclePassTimesTreeNode nodeProvinceLeaf = provinceTree.findTreeNodeById(tileIdProvinceMin);
            vehiclePassTimesUpdate(nodeProvinceLeaf, tid);
            List<VehiclePassTimesTreeNode> provinceElders = nodeProvinceLeaf.getElders();
            for (VehiclePassTimesTreeNode elder : provinceElders) {
                vehiclePassTimesUpdate(elder, tid);
            }
            // 市级网格统计 直辖市增加 cityZoom级别瓦片
            long tileIdCity = VehiclePassTimesQuadtree.getTileNumber(location.getOriginalLat(),
                    location.getOriginalLng(), cityZoom);
            VehiclePassTimesTreeNode cityTree = getQuadtree(result.getGrids(), tileIdCity, district[0], st);
            VehiclePassTimesTreeNode nodeCityMin = cityTree.findTreeNodeById(tileIdMin);
            vehiclePassTimesUpdate(nodeCityMin, tid);
            List<VehiclePassTimesTreeNode> cityElder = nodeCityMin.getElders();
            for (VehiclePassTimesTreeNode elder : cityElder) {
                vehiclePassTimesUpdate(elder, tid);
            }

        }
    }
}
