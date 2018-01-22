package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service;

import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCLastestLocationDataRes.LastestLocationDataRes;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCMileageAndOilDataRes;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa.TerminalOnlinePercentage;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.GpsDataEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.Point;
import com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBusDataRecords.CANBusDataRecords;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetTerminalFirstRecieveDateRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetTerminalFirstRecieveDateRes.GetTerminalFirstRecieveDateRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetTerminalInAreaRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetTerminalInDistrictRes.GetTerminalInDistrictRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetTerminalLocationDataRes.GetTerminalLocationDataRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCLastestLocationDataByAlarmFilterRess.LastestLocationDataByAlarmFilterRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCLastestTerminalStatusRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCLatestParkRecoreds;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;


public interface TermianlDataService {
    /**
     * 分页查询某终端的历史位置数据
     *
     * @param terminalIds
     *            终端标识
     * @param startDate
     *            开始时间
     * @param endDate
     *            结束时间
     * @return List<GpsDetailedEntity> 数据集合
     */
    @Deprecated
    public GpsDataEntity queryGpsData(List<Long> terminalIds, long startDate, long endDate, boolean isFilterAlarm,
                                      boolean isAllAlarm, long alarms, boolean isFilterBreakdown, long breakdownCode,
                                      CommonParameter commonParameter, String mdkey) throws RemoteException;

    /**
     * 查询某终端的历史位置数据
     *
     * @param terminalId
     *            终端标识
     * @param beginTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return List<GpsDetailedEntity> 数据集合
     */
    public GpsDataEntity queryGpsData(long terminalId, long beginTime, long endTime) throws RemoteException;

    /**
     * 获取批量终端某一天的的轨迹数据，给统计计算使用
     *
     * modified by zhangyj
     * @param tids
     * @param begin
     * @return
     */
    public Map<Long, List<LocationData>> queryGpsDataFromRedis(List<Long> tids, long begin, long end) throws RemoteException;

    // 以下方法于2016年4月21日新增，用于查询5分钟一段的位置数据临时缓存

    /**
     * 查询一批终端的当天指定时间段[0, 288)的位置数据（5分钟一段，左闭右开区间）
     *
     * modified by zhangyj
     *
     * @param tids
     * @param segment
     * @return
     */
    public Map<Long, List<LocationData>> queryGpsDataSegementThisDay(List<Long> tids, int segment) throws RemoteException;

    /**
     * 查询指定终端的当天指定时间段[0, 288)的位置数据（5分钟一段，左闭右开区间）
     *
     * modified by zhangyj
     *
     * @param tid
     * @param segment
     * @return
     */
    public List<LocationData> queryGpsDataSegementThisDay(long tid, int segment) throws RemoteException;

    // 以上方法于2016年4月21日新增，用于查询5分钟一段的位置数据临时缓存

    /**
     * 检索车辆在指定的时间段是否经过区域，检索后返回车辆轨迹的第一个点和最后一个点
     *
     * @param terminalIds
     * @param beginTime
     * @param endTime
     * @param leftLongitude
     * @param rightLongitude
     * @param topLatitude
     * @param bottomLatitude
     * @return
     * @throws RemoteException
     */
    public Map<Long, GpsDataEntity> getCrossAreaTrack(List<Long> terminalIds, long beginTime, long endTime,
                                                      int leftLongitude, int rightLongitude, int topLatitude, int bottomLatitude) throws RemoteException;

    /**
     * 查询终端上线率
     *
     * @param terminalIds
     * @param beginTime
     * @param endTime
     * @param commonParameter
     * @return
     * @throws RemoteException
     */
    public List<TerminalOnlinePercentage> getTerminalOnlinePercentage(List<Long> terminalIds, long beginTime,
                                                                      long endTime, CommonParameter commonParameter) throws RemoteException;

    /**
     * 跨域车次检索
     *
     * @param terminalIds
     * @param beginTime
     * @param endTime
     * @param areaType
     * @param points
     * @return
     * @throws RemoteException
     */
    public byte[] getCrossAreaCounts(List<Long> terminalIds, long beginTime, long endTime, int areaType,
                                     List<Point> points) throws RemoteException;

    public byte[] getCrossAreaCounts2(List<Long> terminalIds, long beginTime, long endTime, int areaType,
                                      List<Point> points, int width) throws RemoteException;

    /**
     * 查询所有终端
     *
     * @param node_code
     *            节点编号
     * @param district
     *            分区号
     * @return 终端ID集合
     */
    public List<Long> getByNodeCodeAndDistrict(long node_code, int district) throws RemoteException;

    /**
     * rmi服务可用性检测
     *
     * @return
     * @throws RemoteException
     */
    public boolean isConnected() throws RemoteException;

    public CANBusDataRecords getCANBusData(List<Long> terminalID, int type, long startDate, long endDate,
                                           CommonParameter commonParameter) throws RemoteException;

    /**
     * 3.8里程油量数据检索
     *
     * @param terminalIds
     * @return
     */
    public List<LCMileageAndOilDataRes.MileageAndOilData> getMileageAndOilData(List<Long> terminalIds) throws RemoteException;

    /**
     * 3.9最新位置数据检索
     *
     * @param terminalIds
     * @return
     * @throws RemoteException
     */
    public LastestLocationDataRes getLastestLocationData(List<Long> terminalIds, String mileageRange,
                                                         int type, CommonParameter commonParameter) throws RemoteException;
    /***
     *
     * @param terminalIds
     * @param type 0：有效位置（经纬度）1：有效CAN数据 2：所有数据
     * @return
     * @throws RemoteException
     */
    public Map<Long, List<LocationData>> getLastLocationData(List<Long> terminalIds, int type) throws RemoteException;

    public void delGpsDataSegementThisDay() throws RemoteException;

    public GetTerminalLocationDataRes getTerminalLocationData(long terminalId, long queryDate,
                                                              int index, long accessTocken) throws RemoteException;

    public LCGetTerminalInAreaRes.GetTerminalInAreaRes getTerminalInArea(List<Long> terminalIds, int type, List<Point> points, int radius) throws RemoteException;

    public GetTerminalInDistrictRes GetTerminalInDistrict(List<Long> terminalIds, int districtCode) throws RemoteException;

    public LastestLocationDataByAlarmFilterRes getLastestLocationDataByAlarmFilter(List<Long> terminalIds, boolean isFilter, long alarmType,
                                                                                   int isCancel, int type, CommonParameter commonParameter) throws RemoteException;
    /**
     * 获取终端最新在线状态
     * @param terminalIds
     * @return
     * @throws RemoteException
     */
    public LCLastestTerminalStatusRes.LastestTerminalStatusRes GetLastestTerminalStatus(List<Long> terminalIds) throws RemoteException;

   public GetTerminalFirstRecieveDateRes GetTerminalFirstRecieveDate(List<Long> terminalIds) throws RemoteException;

   public void saveGpsDataTask() throws RemoteException;

    /**
     * 获取车辆最后进入区域 信息
     * @param terminalIds  终端列表
     * @param areaIds    区域列表
     * @return
     * @throws Exception
     */
   public LCLatestParkRecoreds.LatestParkRecoreds GetLatestParkRecordsRes(List<Long> terminalIds, List<Long> areaIds) throws  Exception;

}

