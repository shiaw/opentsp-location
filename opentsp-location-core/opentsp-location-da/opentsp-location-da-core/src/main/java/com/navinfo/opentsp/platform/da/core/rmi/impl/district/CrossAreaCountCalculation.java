package com.navinfo.opentsp.platform.da.core.rmi.impl.district;

import java.util.ArrayList;
import java.util.List;

import com.navinfo.opentsp.platform.location.kit.calc.Douglas;
import com.navinfo.opentsp.platform.location.kit.calc.DouglasPoint;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.cache.AreaDataEntity;
import com.navinfo.opentsp.platform.da.core.common.util.NearestPoint;
import com.navinfo.opentsp.platform.da.core.persistence.DictManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.DictManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDetailedEntityDB;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictDBEntity;

/**
 * 跨域车次统计相关计算类
 *
 * @author zyl
 * @date 2015年10月16日
 */
public class CrossAreaCountCalculation {
    private static Logger logger = LoggerFactory.getLogger(CrossAreaCountCalculation.class);

    private static DictManage dict = new DictManageImpl();

    /**
     * 判断轨迹是否有可能经过此区域
     *
     * @param dataList  轨迹数据
     * @param areaType  区域类型
     * @param points    区域点集
     * @param queryTime 查询时间（查询条件的结束时间减去开始时间）
     * @return 返回false则轨迹一定不会经过此区域，true则有可能经过
     */
    public static boolean isFilterPositions(List<GpsDetailedEntityDB> dataList, int areaType, int width, List<Point> points, long queryTime) {
        if (null != dataList && dataList.size() > 0) {
            int size = dataList.size();
            LocationData positionA = null;
            try {
                int index = getValidIndexByIndex(dataList, 0);
                positionA = LocationData.parseFrom(dataList.get(index).getData());
                //判断点是否在区域内
                boolean inArea = isInArea(positionA, areaType, width, points);
                if (inArea) {
                    return true;
                }
                //如果A点到区域的时间超过查询时间则排除该轨迹
                long areaToATime = getTimeBetweenAToArea(positionA, areaType, width, points);
                if (areaToATime > queryTime) {
                    logger.info("起始坐标为：X:" + positionA.getLatitude() + " Y:" + positionA.getLongitude() + " 到达查询区域的最短时间为：" + areaToATime + ",超过查询时间：" + queryTime);
                    return false;
                }
                //filteredList为抽稀后的轨迹位置点
                List<GpsDetailedEntityDB> filteredList = new ArrayList<GpsDetailedEntityDB>();
                int startIndex = getValidIndexByIndex(dataList, 0);
                int endIndex = getValidIndexByIndex(dataList, size - 1);
                filteredList.add(dataList.get(startIndex));
                filteredList.add(dataList.get(endIndex));
                //一共需要5个点，所以深度为2
                dataList = getBinaryValue(dataList, 0, size, 2);
                filteredList.addAll(dataList);
                boolean outArea = false;
                for (GpsDetailedEntityDB entity : filteredList) {
                    LocationData position = LocationData.parseFrom(entity.getData());
                    inArea = isInArea(position, areaType, width, points);
                    if (inArea) {
                        return true;
                    }
                    outArea = isOutAreaAndBeyond(position, areaType, width, points);
                    //如果点都在区域80KM内
                    if (!outArea) {
                        return true;
                    }

                }
            } catch (InvalidProtocolBufferException e) {
                logger.error(e.getMessage(), e);
            }
        } else {
//			logger.error("位置数据为空");
        }
        return false;
    }

    /**
     * 获取坐标不为0的数据下标
     *
     * @param dataList
     * @param index
     * @return
     */
    private static int getValidIndexByIndex(List<GpsDetailedEntityDB> dataList, int index) {
        GpsDetailedEntityDB gps = null;
        int size = dataList.size();
        if (index >= size) {
            index = size - 1;
        }
        gps = dataList.get(index);
        try {
            LocationData position = LocationData.parseFrom(gps.getData());
            int lat = position.getLatitude();
            int lon = position.getLongitude();
            if (0 == lat || 0 == lon) {
                index = getValidIndexByIndex(dataList, index + 1);
            }
        } catch (InvalidProtocolBufferException e) {
            logger.error(e.getMessage(), e);
            return 0;
        }
        return index;
    }

    /**
     * 查询点A到区域的最短时间
     *
     * @param positionA
     * @param points
     * @return 最短时间 单位为秒
     */
    private static long getTimeBetweenAToArea(LocationData positionA, int areaType, int width, List<Point> points) {
        long shortestDistance = getShortestDistance(positionA, areaType, width, points);
        //每小时80公里，大致等于每秒22.22222米  22*3600=79200
        long time = shortestDistance / 22;
        return time;
    }

    /**
     * 查询点A是否在区域外而且超出80KM
     *
     * @param positionA
     * @param areaType  0:矩形；1:路线；2:圆形
     * @param points
     * @return 是返回true
     */
    private static boolean isOutAreaAndBeyond(LocationData positionA, int areaType, int width, List<Point> points) {
        long shortestDistance = getShortestDistance(positionA, areaType, width, points);
        if (shortestDistance < width + 80 * 1000) {
            return false;
        }
        return true;
    }

    /**
     * 计算获得最短距离
     *
     * @param positionA
     * @param areaType  0:矩形；1:路线；2:圆形
     * @param width
     * @param points
     * @return 最短距离 单位为米
     */
    private static long getShortestDistance(LocationData positionA, int areaType, int width, List<Point> points) {
        long shortest = 0l;
        switch (areaType) {
            case 0:
                List<Point> points2 = new ArrayList<Point>();
                Point pA = points.get(0);
                Point pC = points.get(1);
                Point pB = new Point();
                pB.setLatitude(pA.getLatitude());
                pB.setLongitude(pC.getLongitude());
                Point pD = new Point();
                pD.setLatitude(pC.getLatitude());
                pD.setLongitude(pA.getLongitude());
                points2.add(pA);
                points2.add(pB);
                points2.add(pC);
                points2.add(pD);
                List<Long> distanceList = getDistanceList(positionA, points);
                long dis = Long.MAX_VALUE;
                for (Long distance : distanceList) {
                    if (distance < dis) {
                        dis = distance;
                    }
                }
                shortest = dis;
                break;
            case 1:
                //如果路线拐点太多，则进行抽稀
                LcDictDBEntity dbEntity = dict.getDictByCode(210007);
                if (null != dbEntity && !"".equals(dbEntity.getDict_data())) {
                    double v = Double.parseDouble(dbEntity.getDict_data());
                    points = douglasPoints(points, v);
                } else {
                    points = douglasPoints(points, 10);
                }
                List<Long> distanceList2 = getDistanceList(positionA, points);
                long dis2 = Long.MAX_VALUE;
                for (Long distance : distanceList2) {
                    if (distance < dis2) {
                        dis2 = distance;
                    }
                }
                shortest = dis2;
                break;
            case 2:
                Point center = points.get(0);
//				Long distance = BaiduAPI.getPointAndPointDistance(center.getLatitude(), center.getLongitude(), positionA.getLatitude(), positionA.getLongitude());
                double d = GeometricCalculation.getDistance(center.getLatitude() / 1000000.0, center.getLongitude() / 1000000.0, positionA.getLatitude() / 1000000.0, positionA.getLongitude() / 1000000.0);
                shortest = (long) d;
                break;
            default:
                logger.error("区域类型错误，查询的区域类型为：" + areaType);
                break;
        }
        return shortest;
    }

    /**
     * 获得点到每个线段的距离集合
     *
     * @param positionA
     * @param points
     * @return
     */
    private static List<Long> getDistanceList(LocationData positionA, List<Point> points) {
        List<Long> distanceList = new ArrayList<Long>();
        //计算每个线段到点的最短距离
        for (int i = 1, size = points.size(); i < size; i++) {
            Point pA = points.get(i - 1);
            Point pB = points.get(i);
            double[] nearestPoint = NearestPoint.getNearestPoint(pA.getLatitude(), pA.getLongitude(), pB.getLatitude(), pB.getLongitude(), positionA.getLatitude(), positionA.getLongitude());
//			Long distance = BaiduAPI.getPointAndPointDistance(nearestPoint[0], nearestPoint[1], positionA.getLatitude(), positionA.getLongitude());
            double d = GeometricCalculation.getDistance(nearestPoint[0] / 1000000.0, nearestPoint[1] / 1000000.0, positionA.getLatitude() / 1000000.0, positionA.getLongitude() / 1000000.0);
            distanceList.add((long) d);
        }
        return distanceList;
    }

    /**
     * 计算车次
     *
     * @param dataList
     * @param areaType 0:矩形；1:路线；2:圆形
     * @param width
     * @param points
     * @return
     */
    public static int calculationCounts(List<GpsDetailedEntityDB> dataList, int areaType, int width, List<Point> points) {
        int count = 0;
        int size = dataList.size();
        //最大深度，大约为轨迹点个数的1/4
        int maxDeep = (int) Math.ceil(Math.log(size) / Math.log(2));
        //初始深度
        int deep = 0;
        //filteredList为抽稀后的轨迹位置点
        List<GpsDetailedEntityDB> filteredList = new ArrayList<GpsDetailedEntityDB>();
        filteredList.add(dataList.get(0));
        filteredList.add(dataList.get(size - 1));
        try {
            switch (areaType) {
                case 0:
                    //矩形
                    while (true) {
                        for (int i = 0; i < filteredList.size(); i++) {
                            byte[] data = filteredList.get(i).getData();
                            LocationData location = LocationData.parseFrom(data);
                            if (isBelongRectangle(location.getLongitude(), location.getLatitude(), points)) {
                                count = 1;
                                break;
                            }
                        }
                        deep++;
                        if (1 == count || deep > maxDeep) {
                            break;
                        }
                        filteredList = getMiddleValueByDeep(dataList, deep);
                    }
                    break;
                case 1:
                    //路线
                    //从路线中抽取5个点
                    List<Point> filteredPoints = new ArrayList<Point>();
                    filteredPoints.add(points.get(0));
                    List<Point> binaryValue = getBinaryValue(points, 0, points.size(), 2);
                    filteredPoints.add(binaryValue.get(0));
                    filteredPoints.add(binaryValue.get(2));
                    filteredPoints.add(binaryValue.get(1));
                    filteredPoints.add(points.get(points.size() - 1));
                    while (true) {
                        for (int i = 0; i < filteredList.size(); i++) {
                            byte[] data = filteredList.get(i).getData();
                            LocationData location = LocationData.parseFrom(data);
                            if (isBelonglineArea(location.getLongitude(), location.getLatitude(), filteredPoints, width)) {
                                count = 1;
                                break;
                            }
                        }
                        deep++;
                        if (1 == count || deep > maxDeep) {
                            break;
                        }
                        filteredList = getMiddleValueByDeep(dataList, deep);
                    }
                    break;
                case 2:
                    //圆
                    while (true) {
                        for (int i = 0; i < filteredList.size(); i++) {
                            byte[] data = filteredList.get(i).getData();
                            LocationData location = LocationData.parseFrom(data);
                            if (isBelongCycle(location.getLongitude(), location.getLatitude(), points, width)) {
                                count = 1;
                                break;
                            }
                        }
                        deep++;
                        if (1 == count || deep > maxDeep) {
                            break;
                        }
                        filteredList = getMiddleValueByDeep(dataList, deep);
                    }
                    break;
                default:
                    logger.error("区域类型错误，查询的区域类型为：" + areaType);
                    break;
            }
        } catch (InvalidProtocolBufferException e) {
            logger.error(e.getMessage(), e);
        }
        return count;
    }

    /**
     * 判断点是否在区域内
     *
     * @param location
     * @param areaType
     * @param width
     * @param points
     * @return
     */
    public static boolean isInArea(LocationData location, int areaType, int width, List<Point> points) {
        boolean result = false;
        try {
            switch (areaType) {
                case 0:
                    result = isBelongRectangle(location.getLongitude(), location.getLatitude(), points);
                    break;
                case 1:
                    result = isBelongCycle(location.getLongitude(), location.getLatitude(), points, width);
                    break;
                case 2:
                    result = isBelonglineArea(location.getLongitude(), location.getLatitude(), points, width);
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 判断点是否在区域内
     *
     * @param location
     * @param areaType
     * @param width
     * @param points
     * @return
     */
    public static boolean isInAreaV2(LocationData location, int areaType, int width, List<Point> points) {
        boolean result = false;
        try {
            switch (areaType) {
                case 1:
                    result = isBelongCycle(location.getLongitude(), location.getLatitude(), points, width);
                    break;
                case 2:
                    result = isBelongRectangle(location.getLongitude(), location.getLatitude(), points);
                    break;
                case 3:
                    result = isPolygonContainsPoint(points, location.getLongitude(), location.getLatitude());
                    break;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return result;
    }


    /**
     * 判断点是否在矩形内
     *
     * @param longitude
     * @param latitude
     * @param points
     * @return
     */
    public static boolean isBelongRectangle(int longitude, int latitude,
                                            List<Point> points) {
        int LeftToplong = points.get(0).getLongitude();
        int LeftToplat = points.get(0).getLatitude();
        int RightBottomlong = points.get(1).getLongitude();
        int RightBottomlat = points.get(1).getLatitude();
        if (longitude >= LeftToplong && longitude <= RightBottomlong
                && latitude >= RightBottomlat && latitude <= LeftToplat)
            return true;
        return false;
    }

    public static boolean isPolygonContainsPoint(java.util.List<Point> mPoints, int longitude, int latitude) {
        // http://blog.csdn.net/shao941122/article/details/51504519  参考这个算法。计算交叉点
        int nCross = 0;
        if (mPoints.size() < 3) return false;//集合小于3，无法构成多边形。
        for (int i = 0; i < mPoints.size(); i++) {
            Point p1 = mPoints.get(i);
            Point p2 = mPoints.get((i + 1) % mPoints.size());
            // 求解 y=p.y 与 p1p2 的交点
            if (p1.getLongitude() == p2.getLongitude()) // p1p2 与 y=p0.y平行
                continue;
            // 交点在p1p2延长线上
            if (longitude < Math.min(p1.getLongitude(), p2.getLongitude()))
                continue;
            // 交点在p1p2延长线上
            if (longitude >= Math.max(p1.getLongitude(), p2.getLongitude()))
                continue;
            // 求交点的 X 坐标
            double x = (double) (longitude / 1000000.0 - p1.getLongitude() / 1000000.0)
                    * (double) (p2.getLatitude() / 1000000.0 - p1.getLatitude() / 1000000.0)
                    / (double) (p2.getLongitude() / 1000000.0 - p1.getLongitude() / 1000000.0)
                    + p1.getLatitude() / 1000000.0;
            if (x > latitude / 1000000.0)
                nCross++; // 只统计单边交点
        }
        // 单边交点为偶数，点在多边形之外 ---
        return (nCross % 2 == 1);
    }

    /**
     * 判断点是否在线段内
     *
     * @param longitude
     * @param latitude
     * @param points
     * @param width
     * @return
     */
    public static boolean isBelonglineArea(int longitude, int latitude,
                                           List<Point> points, int width) {
        List<AreaDataEntity> areaDataEntityList = new ArrayList<AreaDataEntity>();
        for (int i = 0; i < points.size(); i++) {
            AreaDataEntity entity = new AreaDataEntity();
            entity.setLongitude(points.get(i).getLongitude());
            entity.setLatitude(points.get(i).getLatitude());
            entity.setRadiusLength(width);// 设置路宽
            areaDataEntityList.add(entity);
        }
        if (GeometricCalculation.insideRoute(longitude, latitude,
                areaDataEntityList))
            return true;
        return false;
    }

    /**
     * 判断点是否在圆内
     *
     * @param longitude
     * @param latitude
     * @param points
     * @param width
     * @return
     */
    public static boolean isBelongCycle(int longitude, int latitude,
                                        List<Point> points, int width) {
        AreaDataEntity entity = new AreaDataEntity();
        entity.setLongitude(points.get(0).getLongitude());
        entity.setLatitude(points.get(0).getLatitude());
        if (GeometricCalculation.insideCircle(longitude, latitude, entity, width))
            return true;
        return false;
    }

    /**
     * 抽稀路径的拐点
     *
     * @param points
     * @param precision 抽稀精度
     * @return
     */
    private static List<Point> douglasPoints(List<Point> points, double precision) {
        List<Point> result = new ArrayList<Point>();
        Douglas douglas = new Douglas();
        douglas.points.clear();
        String pString = "";
        for (Point point : points) {
            pString += point.getLatitude() / 1000000.0 + " " + point.getLongitude() / 1000000.0 + ",";
        }
        pString = pString.substring(0, pString.length() - 2);
//		douglas.readDouglasPoint("1 4,2 3,4 2,6 6,7 7,8 6,9 5,10 10");
        douglas.readDouglasPoint(pString);
        douglas.compress(douglas.points.get(0), douglas.points.get(douglas.points.size() - 1), precision);
        for (int i = 0; i < douglas.points.size(); i++) {
            DouglasPoint p = douglas.points.get(i);
            if (p.getIndex() > -1) {
                Point point = new Point();
                point.setLatitude((int) (p.getX() * 1000000));
                point.setLongitude((int) (p.getY() * 1000000));
                result.add(point);
//				System.out.print(p.getX() + "——" + p.getY() + ",");
            }
        }
        return result;
    }

    /**
     * 通过二分法获取深度为deep的元素
     *
     * @param list
     * @param startIndex 起始下标
     * @param endIndex   结束下标
     * @param deep       深度
     * @return 返回（2的deep次方-1）个元素
     */
    private static <T> List<T> getBinaryValue(List<T> list, int startIndex, int endIndex, int deep) {
        List<T> result = new ArrayList<T>();
        if (deep <= 0) {
            return result;
        }
        int middle = (startIndex + endIndex) >> 1;
        T midValue = list.get(middle);
        deep = deep - 1;
        if (deep > 0) {
            List<T> r1 = getBinaryValue(list, startIndex, middle, deep);
            result.addAll(r1);
            List<T> r2 = getBinaryValue(list, middle, endIndex, deep);
            result.addAll(r2);
        }
        result.add(midValue);
        return result;
    }

    /**
     * 获得指定深度的二分法叶子元素
     *
     * @param source
     * @param deep
     * @return 返回（2的deep-1次方）个元素
     */
    private static List<GpsDetailedEntityDB> getMiddleValueByDeep(List<GpsDetailedEntityDB> source, int deep) {
        List<GpsDetailedEntityDB> result = new ArrayList<GpsDetailedEntityDB>();
        int begin = 0;
        int size = source.size();
        GpsDetailedEntityDB midValue = null;
        if (deep <= 1) {
            midValue = source.get((begin + size) >> 1);
            result.add(midValue);
        } else {
            int resultSize = 2 << (deep - 2);
            if (resultSize > size / 4) {
                return result;
            }
            for (int i = 1; i <= resultSize; i++) {
                int index = size * (1 + (2 * (i - 1))) / (2 << (deep - 1));
                midValue = source.get(index);
                result.add(midValue);
            }
        }
        return result;
    }
}