package com.navinfo.opentsp.platform.da.core.rmi.impl.district;

import com.mongodb.DBCollection;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.*;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.*;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.*;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.VehicleDrivingNumber;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.VehicleDrivingNumberImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempGpsData;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.*;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassInDistrict;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesDetail;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesRecord;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesTreeNode;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsStoreService;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCLastestVehicleInfo.LastestVehicleInfo;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCMileageConsumption.MileageConsumption;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCVehiclePassInAreaInfo.VehiclePassInAreaInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.*;
import java.util.Map.Entry;

/**
 * 存储DSA统计计算的结果数据相关服务
 *
 * @author jin_s
 */
@Service
public class AlarmStatisticsStoreServiceimpl  implements AlarmStatisticsStoreService {
    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 1L;
    private static Logger logger = LoggerFactory.getLogger(AlarmStatisticsStoreServiceimpl.class);


    BaseMongoDao mongoDaoImpl = new BaseMongoDaoImpl();

    /**
     * 保存终端产生超速报警信息
     *
     * @param terminalId
     * @param day
     * @param overspeedAlarmList
     * @return
     */
    @Override
    public PlatformResponseResult saveOverspeedAlarmInfo(long terminalId, long day,
                                                         List<OverspeedAlarm> overspeedAlarmList) {

        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DAOverspeedAlarm> convertOverspeedAlarmList = new ArrayList<>();
        for (OverspeedAlarm spd : overspeedAlarmList) {
            DAOverspeedAlarm item = new DAOverspeedAlarm();
            item.setBeginDate(spd.getBeginDate());
            item.setBeginLat(spd.getBeginLat());
            item.setBeginLng(spd.getBeginLng());
            item.setContinuousTime(spd.getContinuousTime());
            item.setEndDate(spd.getEndDate());
            item.setEndLat(spd.getEndLat());
            item.setEndLng(spd.getEndLng());
            item.setLimitSpeed(spd.getLimitSpeed());
            item.setMaxSpeed(spd.getMaxSpeed());
            item.setMinSpeed(spd.getMinSpeed());
            item.setTerminalId(spd.getTerminalId());
            convertOverspeedAlarmList.add(item);
        }

        // 内部实体存储
        OverspeedAlarmEntity overspeedAlarmEntity = new OverspeedAlarmEntity();
        overspeedAlarmEntity.setTerminal_id(terminalId);
        overspeedAlarmEntity.setDay(day);
        overspeedAlarmEntity.setDataList(convertOverspeedAlarmList);
        this.mongoDaoImpl.saveForBatch(overspeedAlarmEntity);
        return PlatformResponseResult.success;
    }

    /**
     * 存储进出区域报警统计信息
     *
     * @param terminalId
     * @param day
     * @param areaINOUTAlarmList
     * @return
     */
    @Override
    public PlatformResponseResult saveAreaINOUTAlarm(long terminalId, long day, List<AreaINOUTAlarm> areaINOUTAlarmList) {

        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DAAreaINOUTAlarm> areaINOUTList = new ArrayList<>();
        for (AreaINOUTAlarm spd : areaINOUTAlarmList) {

            DAAreaINOUTAlarm item = new DAAreaINOUTAlarm();
            item.setBeginDate(spd.getBeginDate());
            item.setAreaId(spd.getAreaId());
            item.setType(spd.getType());
            item.setBeginLat(spd.getBeginLat());
            item.setBeginLng(spd.getBeginLng());
            item.setContinuousTime(spd.getContinuousTime());
            item.setEndDate(spd.getEndDate());
            item.setEndLat(spd.getEndLat());
            item.setEndLng(spd.getEndLng());
            item.setTerminalId(spd.getTerminalId());
            areaINOUTList.add(item);
        }

        // 内部实体存储
        AreaINOUTAlarmEntity areaINOUTAlarmEntity = new AreaINOUTAlarmEntity();
        areaINOUTAlarmEntity.setTerminal_id(terminalId);
        areaINOUTAlarmEntity.setDay(day);
        areaINOUTAlarmEntity.setDataList(areaINOUTList);
        this.mongoDaoImpl.saveForBatch(areaINOUTAlarmEntity);
        return PlatformResponseResult.success;
    }

    /**
     * 保存终端产生超速报警统计信息
     *
     * @param terminalId
     * @param day
     * @param areaOverspeedAlarmList
     * @return
     */
    @Override
    public PlatformResponseResult saveAreaOverspeedAlarmInfo(long terminalId, long day,
                                                             List<AreaOverspeedAlarm> areaOverspeedAlarmList) {

        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DAAreaOverspeedAlarm> areaOspdList = new ArrayList<>();
        for (AreaOverspeedAlarm spd : areaOverspeedAlarmList) {
            DAAreaOverspeedAlarm item = new DAAreaOverspeedAlarm();
            item.setBeginDate(spd.getBeginDate());
            item.setBeginLat(spd.getBeginLat());
            item.setBeginLng(spd.getBeginLng());
            item.setContinuousTime(spd.getContinuousTime());
            item.setEndDate(spd.getEndDate());
            item.setEndLat(spd.getEndLat());
            item.setEndLng(spd.getEndLng());
            item.setTerminalId(spd.getTerminalId());
            item.setAreaId(spd.getAreaId());
            item.setType(spd.getType());
            item.setLimitSpeed(spd.getLimitSpeed());
            item.setMaxSpeed(spd.getMaxSpeed());
            item.setMinSpeed(spd.getMinSpeed());
            areaOspdList.add(item);
        }

        // 内部实体存储
        AreaOverspeedAlarmEntity areaOverspeedAlarmEntity = new AreaOverspeedAlarmEntity();
        areaOverspeedAlarmEntity.setTerminal_id(terminalId);
        areaOverspeedAlarmEntity.setDay(day);
        areaOverspeedAlarmEntity.setDataList(areaOspdList);
        this.mongoDaoImpl.saveForBatch(areaOverspeedAlarmEntity);
        return PlatformResponseResult.success;
    }

    /**
     * 保存进出线路报警统计信息
     *
     * @param terminalId
     * @param day
     * @param routeINOUTAlarmList
     * @return
     */
    @Override
    public PlatformResponseResult saveRouteINOUTAlarmInfo(long terminalId, long day,
                                                          List<RouteINOUTAlarm> routeINOUTAlarmList) {
        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DARouteINOUTAlarm> routeIOUTList = new ArrayList<>();
        TreeMap<Long, DARouteINOUTAlarm> map = new TreeMap<Long, DARouteINOUTAlarm>();

        for (RouteINOUTAlarm spd : routeINOUTAlarmList) {
            DARouteINOUTAlarm item = new DARouteINOUTAlarm();

            item.setAreaId(spd.getAreaId());
            item.setType(spd.getType());
            item.setTriggerContinuousTime(spd.getTriggerContinuousTime());
            item.setTriggerDate((int) spd.getTriggerDate());
            item.setTriggerLat(spd.getTriggerLat());
            item.setTriggerLng(spd.getTriggerLng());
            item.setBeginDate(spd.getBeginDate());
            item.setBeginLat(spd.getBeginLat());
            item.setBeginLng(spd.getBeginLng());
            item.setContinuousTime(spd.getContinuousTime());
            item.setEndDate(spd.getEndDate());
            item.setEndLat(spd.getEndLat());
            item.setEndLng(spd.getEndLng());
            item.setTerminalId(spd.getTerminalId());
            map.put(spd.getBeginDate(), item);

        }
        Entry<Long, DARouteINOUTAlarm> pollFirstEntry = null;
        while (true) {
            pollFirstEntry = map.pollFirstEntry();
            if (pollFirstEntry == null) {
                break;
            }
            routeIOUTList.add(pollFirstEntry.getValue());
        }

        // 内部实体存储
        RouteINOUTAlarmEntity routeINOUTAlarmEntity = new RouteINOUTAlarmEntity();
        routeINOUTAlarmEntity.setTerminal_id(terminalId);
        routeINOUTAlarmEntity.setDay(day);
        routeINOUTAlarmEntity.setDataList(routeIOUTList);
        this.mongoDaoImpl.saveForBatch(routeINOUTAlarmEntity);
        return PlatformResponseResult.success;
    }

    /**
     * 保存终端产生路段超速报警统计信息
     *
     * @param terminalId
     * @param day
     * @param rLineOverspeedAlarmList
     * @return
     */
    @Override
    public PlatformResponseResult saveRLineOverspeedAlarmInfo(long terminalId, long day,
                                                              List<RLineOverspeedAlarm> rLineOverspeedAlarmList) {

        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DARLineOverspeedAlarm> rlineOspdList = new ArrayList<>();
        for (RLineOverspeedAlarm spd : rLineOverspeedAlarmList) {
            DARLineOverspeedAlarm item = new DARLineOverspeedAlarm();

            item.setAreaId(spd.getAreaId());
            item.setSegmentId(spd.getSegmentId());
            item.setLimitSpeed(spd.getLimitSpeed());
            item.setMaxSpeed(spd.getMaxSpeed());
            item.setMinSpeed(spd.getMinSpeed());
            item.setBeginDate(spd.getBeginDate());
            item.setBeginLat(spd.getBeginLat());
            item.setBeginLng(spd.getBeginLng());
            item.setContinuousTime(spd.getContinuousTime());
            item.setEndDate(spd.getEndDate());
            item.setEndLat(spd.getEndLat());
            item.setEndLng(spd.getEndLng());
            item.setTerminalId(spd.getTerminalId());
            rlineOspdList.add(item);
        }

        // 内部实体存储
        RLineOverspeedAlarmEntity rLineOverspeedAlarm = new RLineOverspeedAlarmEntity();
        rLineOverspeedAlarm.setTerminal_id(terminalId);
        rLineOverspeedAlarm.setDay(day);
        rLineOverspeedAlarm.setDataList(rlineOspdList);
        this.mongoDaoImpl.saveForBatch(rLineOverspeedAlarm);
        return PlatformResponseResult.success;
    }

    /**
     * 保存终端产生路段超时报警信息
     *
     * @param terminalId
     * @param day
     * @param rLineOvertimeAlarmList
     * @return
     */
    @Override
    public PlatformResponseResult saveRLineOvertimeAlarm(long terminalId, long day,
                                                         List<RLineOvertimeAlarm> rLineOvertimeAlarmList) {

        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DARLineOvertimeAlarm> rlineOtimeList = new ArrayList<>();
        for (RLineOvertimeAlarm spd : rLineOvertimeAlarmList) {
            DARLineOvertimeAlarm item = new DARLineOvertimeAlarm();
            item.setAreaId(spd.getAreaId());
            item.setSegmentId(spd.getSegmentId());
            item.setType(spd.getType());
            item.setMaxLimitTime(spd.getMaxLimitTime());
            item.setMinLimitTime(spd.getMinLimitTime());
            item.setBeginDate(spd.getBeginDate());
            item.setBeginLat(spd.getBeginLat());
            item.setBeginLng(spd.getBeginLng());
            item.setContinuousTime(spd.getContinuousTime());
            item.setEndDate(spd.getEndDate());
            item.setEndLat(spd.getEndLat());
            item.setEndLng(spd.getEndLng());
            item.setTerminalId(spd.getTerminalId());
            rlineOtimeList.add(item);
        }

        // 内部实体存储
        RLineOvertimeAlarmEntity rLineOvertimeAlarmEntity = new RLineOvertimeAlarmEntity();
        rLineOvertimeAlarmEntity.setTerminal_id(terminalId);
        rLineOvertimeAlarmEntity.setDay(day);
        rLineOvertimeAlarmEntity.setDataList(rlineOtimeList);
        this.mongoDaoImpl.saveForBatch(rLineOvertimeAlarmEntity);
        return PlatformResponseResult.success;
    }

    /**
     * 停滞超时数据存储
     *
     * @param StagnationTimeoutAlarmList 停滞超时统计数据
     * @param day                        当天统计时间，单位(秒)
     */
    @Override
    public PlatformResponseResult saveStagnationTimeoutInfo(List<StagnationTimeoutEntity> StagnationTimeoutAlarmList,
                                                            long day) {
        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DAStagnationTimeoutAlarm> StagnationTimeoutList = new ArrayList<>();
        for (StagnationTimeoutEntity spd : StagnationTimeoutAlarmList) {
            DAStagnationTimeoutAlarm item = new DAStagnationTimeoutAlarm();
            item.setTerminalId(spd.getTerminalId());
            item.setBeginDate(spd.getBeginDate());
            item.setEndDate(spd.getEndDate());
            item.setContinuousTime(spd.getContinuousTime());
            item.setLimitParking(spd.getLimitParking());
            item.setBeginLat(spd.getBeginLat());
            item.setBeginLng(spd.getBeginLng());
            item.setStatus(spd.getStatus() == 1 ? true : false);
            item.setTailMerge(spd.getTailMerge());
            StagnationTimeoutList.add(item);
        }
        // 内部实体存储
        StagnationTimeoutAlarmEntity overtimeParkingAlarmEntity = new StagnationTimeoutAlarmEntity();
        overtimeParkingAlarmEntity.setDay(day);
        overtimeParkingAlarmEntity.setDataList(StagnationTimeoutList);
        this.mongoDaoImpl.saveForBatch(overtimeParkingAlarmEntity);
        return PlatformResponseResult.success;
    }

    /**
     * 保存终端产生疲劳驾驶统计信息
     *
     * @param terminalId
     * @param day
     * @param fatigueAlarmList
     * @return
     */
    @Override
    public PlatformResponseResult saveFatigueAlarmInfo(long terminalId, long day, List<FatigueAlarm> fatigueAlarmList) {

        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DAFatigueAlarm> fatigueList = new ArrayList<>();
        for (FatigueAlarm spd : fatigueAlarmList) {
            DAFatigueAlarm item = new DAFatigueAlarm();
            item.setLimitDayDriving(spd.getLimitDayDriving());
            item.setLimitDriving(spd.getLimitDriving());
            item.setLimitRest(spd.getLimitRest());
            item.setBeginDate(spd.getBeginDate());
            item.setBeginLat(spd.getBeginLat());
            item.setBeginLng(spd.getBeginLng());
            item.setContinuousTime(spd.getContinuousTime());
            item.setEndDate(spd.getEndDate());
            item.setEndLat(spd.getEndLat());
            item.setEndLng(spd.getEndLng());
            item.setTerminalId(spd.getTerminalId());
            fatigueList.add(item);
        }

        // 内部实体存储
        FatigueAlarmEntity fatigueAlarmEntity = new FatigueAlarmEntity();
        fatigueAlarmEntity.setTerminal_id(terminalId);
        fatigueAlarmEntity.setDay(day);
        fatigueAlarmEntity.setDataList(fatigueList);
        this.mongoDaoImpl.saveForBatch(fatigueAlarmEntity);
        return PlatformResponseResult.success;
    }

    /**
     * 保存终端产生紧急报警统计信息
     *
     * @param terminalId
     * @param day
     * @param emergencyAlarmList
     * @return
     */
    @Override
    public PlatformResponseResult saveEmergencyAlarmInfo(long terminalId, long day,
                                                         List<EmergencyAlarm> emergencyAlarmList) {
        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DAEmergencyAlarm> emergencyList = new ArrayList<>();
        for (EmergencyAlarm spd : emergencyAlarmList) {
            DAEmergencyAlarm item = new DAEmergencyAlarm();
            item.setBeginDate(spd.getBeginDate());
            item.setBeginLat(spd.getBeginLat());
            item.setBeginLng(spd.getBeginLng());
            item.setContinuousTime(spd.getContinuousTime());
            item.setEndDate(spd.getEndDate());
            item.setEndLat(spd.getEndLat());
            item.setEndLng(spd.getEndLng());
            item.setTerminalId(spd.getTerminalId());
            emergencyList.add(item);
        }

        // 内部实体存储
        EmergencyAlarmEntity emergencyAlarmEntity = new EmergencyAlarmEntity();
        emergencyAlarmEntity.setTerminal_id(terminalId);
        emergencyAlarmEntity.setDay(day);
        emergencyAlarmEntity.setDataList(emergencyList);
        this.mongoDaoImpl.saveForBatch(emergencyAlarmEntity);
        return PlatformResponseResult.success;
    }

    /**
     * 保存终端产生主电源欠压统计信息
     *
     * @param terminalId
     * @param day
     * @param powerStayInLowerAlarmList
     * @return
     */
    @Override
    public PlatformResponseResult savePowerStayInLowerAlarmInfo(long terminalId, long day,
                                                                List<PowerStayInLowerAlarm> powerStayInLowerAlarmList) {
        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DAPowerStayInLowerAlarm> powerInlowList = new ArrayList<>();
        for (PowerStayInLowerAlarm spd : powerStayInLowerAlarmList) {
            DAPowerStayInLowerAlarm item = new DAPowerStayInLowerAlarm();
            item.setBeginDate(spd.getBeginDate());
            item.setBeginLat(spd.getBeginLat());
            item.setBeginLng(spd.getBeginLng());
            item.setContinuousTime(spd.getContinuousTime());
            item.setEndDate(spd.getEndDate());
            item.setEndLat(spd.getEndLat());
            item.setEndLng(spd.getEndLng());
            item.setTerminalId(spd.getTerminalId());
            powerInlowList.add(item);
        }

        // 内部实体存储
        PowerStayInLowerAlarmEntity powerStayInLowerAlarmEntity = new PowerStayInLowerAlarmEntity();
        powerStayInLowerAlarmEntity.setTerminal_id(terminalId);
        powerStayInLowerAlarmEntity.setDay(day);
        powerStayInLowerAlarmEntity.setDataList(powerInlowList);
        this.mongoDaoImpl.saveForBatch(powerStayInLowerAlarmEntity);
        return PlatformResponseResult.success;
    }

    /**
     * 保存终端产生主电压断电统计信息
     *
     * @param terminalId
     * @param day
     * @param electricPowerOffAlarmList
     * @return
     */
    @Override
    public PlatformResponseResult saveElectricPowerOffAlarmInfo(long terminalId, long day,
                                                                List<ElectricPowerOffAlarm> electricPowerOffAlarmList) {
        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DAElectricPowerOffAlarm> elecPowerOffList = new ArrayList<>();
        for (ElectricPowerOffAlarm spd : electricPowerOffAlarmList) {
            DAElectricPowerOffAlarm item = new DAElectricPowerOffAlarm();
            item.setBeginDate(spd.getBeginDate());
            item.setBeginLat(spd.getBeginLat());
            item.setBeginLng(spd.getBeginLng());
            item.setContinuousTime(spd.getContinuousTime());
            item.setEndDate(spd.getEndDate());
            item.setEndLat(spd.getEndLat());
            item.setEndLng(spd.getEndLng());
            item.setTerminalId(spd.getTerminalId());
            elecPowerOffList.add(item);
        }

        // 内部实体存储
        ElectricPowerOffAlarmEntity electricPowerOffAlarmEntity = new ElectricPowerOffAlarmEntity();
        electricPowerOffAlarmEntity.setTerminal_id(terminalId);
        electricPowerOffAlarmEntity.setDay(day);
        electricPowerOffAlarmEntity.setDataList(elecPowerOffList);
        this.mongoDaoImpl.saveForBatch(electricPowerOffAlarmEntity);
        return PlatformResponseResult.success;
    }

    /**
     * 保存终端产生ACC状态统计信息
     *
     * @param terminalId
     * @param day
     * @param aCCStatusAlarmList
     * @return
     */
    @Override
    public PlatformResponseResult saveACCStatusAlarmInfo(long terminalId, long day,
                                                         List<TerACCStatus> aCCStatusAlarmList) {

        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DAACCStatusAlarm> accList = new ArrayList<>();
        for (TerACCStatus spd : aCCStatusAlarmList) {
            DAACCStatusAlarm item = new DAACCStatusAlarm();
            item.setBeginDate(spd.getBeginDate());
            item.setBeginLat(spd.getBeginLat());
            item.setBeginLng(spd.getBeginLng());
            item.setContinuousTime(spd.getContinuousTime());
            item.setEndDate(spd.getEndDate());
            item.setEndLat(spd.getEndLat());
            item.setEndLng(spd.getEndLng());
            item.setAccStatus(spd.getAccStatus());
            item.setTerminalId(spd.getTerminalId());
            accList.add(item);
        }

        // 内部实体存储
        ACCStatusAlarmEntity aCCStatusAlarmEntity = new ACCStatusAlarmEntity();
        aCCStatusAlarmEntity.setTerminal_id(terminalId);
        aCCStatusAlarmEntity.setDay(day);
        aCCStatusAlarmEntity.setDataList(accList);
        this.mongoDaoImpl.saveForBatch(aCCStatusAlarmEntity);
        return PlatformResponseResult.success;
    }

    /**
     * 保存终端产生里程统计信息
     *
     * @param terminalId
     * @param day
     * @param milagesList
     * @return
     */
    @Override
    public PlatformResponseResult saveMilagesInfo(long terminalId, long day, int oil, List<Mileages> milagesList) {
        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DAMilages> accList = new ArrayList<>();
        for (Mileages spd : milagesList) {
            DAMilages item = new DAMilages();
            item.setBeginLat(spd.getBeginLat());
            item.setBeginLng(spd.getBeginLng());
            item.setEndDate(spd.getEndDate());
            item.setEndLat(spd.getEndLat());
            item.setEndLng(spd.getEndLng());
            item.setCanMileage(spd.getCanMileage());
            item.setBeginMileage(spd.getBeginMileage());
            item.setEndMileage(spd.getEndMileage());
            item.setElectricConsumption(spd.getElectricConsumption());
            item.setOilConsumption(spd.getOilConsumption());
            item.setTerminalId(spd.getTerminalID());
            item.setStartDate(spd.getStartDate());
            item.setStaticDate(spd.getStaticDate());
            item.setTerminalMileage(spd.getGpsMileage());
            accList.add(item);
        }

        // 内部实体存储
        MilagesEntity milagesEntity = new MilagesEntity();
        milagesEntity.setDay(day);
        // milagesEntity.setOil(oil);
        milagesEntity.setDataList(accList);
        this.mongoDaoImpl.save(milagesEntity);
        return PlatformResponseResult.success;
    }

    /**
     * 保存终端产生驾驶员登录统计信息
     *
     * @param terminalId
     * @param day
     * @param driverLoginList
     * @return
     */
    @Override
    public PlatformResponseResult saveDriverLoginInfo(long terminalId, long day, List<DriverLogin> driverLoginList) {
        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DADriverLogin> driverInforList = new ArrayList<>();
        for (DriverLogin spd : driverLoginList) {
            DADriverLogin item = new DADriverLogin();
            item.setBeginLat(spd.getBeginLat());
            item.setBeginLng(spd.getBeginLng());
            item.setEndLat(spd.getEndLat());
            item.setAgencyName(spd.getAgencyName());
            item.setCertificateValid(spd.getCertificateValid());
            item.setCreditDate(spd.getCreditDate());
            item.setDriverCertificate(spd.getDriverCertificate());
            item.setDriverIdCode(spd.getDriverIdCode());
            item.setDriverName(spd.getDriverName());
            item.setDriverTime(spd.getDriverTime());
            item.setEndLNG(spd.getEndLNG());
            item.setFailedReason(spd.getFailedReason());
            item.setStubbsCard(spd.getStubbsCard());
            item.setTerminalId(spd.getTerminalId());
            driverInforList.add(item);
        }

        // 内部实体存储
        DriverLoginEntity driverLoginEntity = new DriverLoginEntity();
        driverLoginEntity.setTerminal_id(terminalId);
        driverLoginEntity.setDay(day);
        driverLoginEntity.setDataList(driverInforList);
        this.mongoDaoImpl.saveForBatch(driverLoginEntity);
        return PlatformResponseResult.success;
    }

    /**
     * 保存终端在线状态统计信息
     *
     * @param terminalId
     * @param day
     * @param terminalOnOffLineStatusList
     * @return
     */
    @Override
    public PlatformResponseResult saveTerminalOnOffLineStatusInfo(long terminalId, long day,
                                                                  List<TerminalOnOffLineStatus> terminalOnOffLineStatusList) {

        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DATerminalOnOffLineStatus> terOnOffLineList = new ArrayList<>();
        for (TerminalOnOffLineStatus spd : terminalOnOffLineStatusList) {
            DATerminalOnOffLineStatus item = new DATerminalOnOffLineStatus();

            item.setBeginDate(spd.getBeginDate());
            item.setTerminalId(spd.getTerminalId());
            item.setOnlineStatus(spd.getOnlineStatus());
            item.setContinuousTime(spd.getContinuousTime());
            item.setEndDate(spd.getEndDate());

            terOnOffLineList.add(item);
        }

        // 内部实体存储
        TerminalOnOffLineStatusEntity terminalOnOffLineStatusEntity = new TerminalOnOffLineStatusEntity();
        terminalOnOffLineStatusEntity.setTerminal_id(terminalId);
        terminalOnOffLineStatusEntity.setDay(day);
        terminalOnOffLineStatusEntity.setDataList(terOnOffLineList);
        this.mongoDaoImpl.saveForBatch(terminalOnOffLineStatusEntity);
        return PlatformResponseResult.success;
    }

    /**
     * 保存流量统计信息
     *
     * @param terminalId
     * @param day
     * @param wFlowList
     * @return
     */
    @Override
    public PlatformResponseResult saveWFlowInfo(long terminalId, long day, List<WFlow> wFlowList) {

        return PlatformResponseResult.success;
    }

    /**
     * 报警汇总统计信息
     *
     * @param terminalId
     * @param day
     * @param totalAlarmInfo
     * @return
     */
    @Override
    public PlatformResponseResult saveTotalAlarmInfo(long terminalId, long day, List<TotalAlarmInfo> totalAlarmInfo) {

        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DATotalAlarmInfo> totalInforList = new ArrayList<>();
        for (TotalAlarmInfo spd : totalAlarmInfo) {
            DATotalAlarmInfo item = new DATotalAlarmInfo();

            item.setContinuousTime(spd.getContinuousTime());
            item.setCounts(spd.getCounts());
            item.setType(spd.getType());
            totalInforList.add(item);
        }

        // 内部实体存储
        TotalAlarmInfoEntity totalAlarmInfoEntity = new TotalAlarmInfoEntity();
        totalAlarmInfoEntity.setTerminal_id(terminalId);
        totalAlarmInfoEntity.setDay(day);
        totalAlarmInfoEntity.setDataList(totalInforList);
        this.mongoDaoImpl.save(totalAlarmInfoEntity);
        return PlatformResponseResult.success;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public PlatformResponseResult saveOvertimeParkAlarmInfo(long terminalId, long day, List<OvertimeParkAlarm> alarmInfo)
            {
        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DAOvertimeParkAlarm> alarms = new ArrayList<>();
        for (OvertimeParkAlarm spd : alarmInfo) {
            DAOvertimeParkAlarm item = new DAOvertimeParkAlarm();
            item.setTerminalId(spd.getTerminalId());
            item.setAreaId(spd.getAreaId());
            item.setLimitParking(spd.getLimitParking());
            item.setContinuousTime(spd.getContinuousTime());
            item.setBeginDate(spd.getBeginDate());
            item.setEndDate(spd.getEndDate());
            item.setBeginLat(spd.getBeginLat());
            item.setEndLat(spd.getEndLat());
            item.setBeginLng(spd.getBeginLng());
            item.setEndLng(spd.getEndLng());
            alarms.add(item);
        }

        // 内部实体存储
        OvertimeParkAlarmEntity entity = new OvertimeParkAlarmEntity();
        entity.setTerminal_id(terminalId);
        entity.setDay(day);
        entity.setDataList(alarms);
        this.mongoDaoImpl.saveForBatch(entity);
        return PlatformResponseResult.success;
    }

    @Override
    public PlatformResponseResult delOvertimeParkStatisticInfo(String id, long recordDate) {
        MongoDao dao = new MongoDaoImp();
        Calendar calendar = DateUtils.calendar(recordDate);
        int mon = calendar.get(Calendar.MONTH) + 1;
        String month = "";
        if (mon < 10) {
            month = "0" + mon;
        } else {
            month = "" + mon;
        }
        String s = calendar.get(Calendar.YEAR) + month;
        if (s.length() == 6) {
            s = s.substring(2);
            logger.info("删除滞留超时：" + id + " 日期：" + s);
            DBCollection dbCollection = null;
            try {
                dbCollection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS,
                        "OvertimeParkingInArea_" + s);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            PlatformResponseResult result = dao.delObjectById(dbCollection, id);
            MongoManager.close();
            return result;
        } else {
            logger.error("删除滞留超时：" + id + "时解析日期出错：" + recordDate + "---" + s);
            return PlatformResponseResult.failure;
        }
    }

    @Override
    public PlatformResponseResult saveVehiclePassGridTimes(List<VehiclePassTimesTreeNode> grids) {
        VehicleDrivingNumber number = new VehicleDrivingNumberImpl();
        PlatformResponseResult save = number.saveVehiclePassGridTimes(grids);
        return save;
    }

    @Override
    public PlatformResponseResult saveVehiclePassAreaTimes(List<VehiclePassTimesRecord> areas) {
        VehicleDrivingNumber number = new VehicleDrivingNumberImpl();
        PlatformResponseResult save = number.saveVehiclePassAreaTimes(areas);
        return save;
    }

    @Override
    public PlatformResponseResult saveFaultCodeStatistic(List<FaultCodeEntity> alarms, long day) {
        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List<DAFaultCodeAlarm> daAlarms = new ArrayList<DAFaultCodeAlarm>();
        for (FaultCodeEntity spd : alarms) {
            DAFaultCodeAlarm item = new DAFaultCodeAlarm();
            item.setTerminalId(spd.getTerminalId());
            item.setSpn(spd.getSpn());
            item.setFmi(spd.getFmi());
            item.setBeginDate(spd.getBeginDate());
            item.setEndDate(spd.getEndDate());
            item.setContinuousTime(spd.getContinueTime());
            item.setBeginLat(spd.getBeginLat());
            item.setBeginLng(spd.getBeginLng());
            item.setEndLat(spd.getEndLat());
            item.setEndLng(spd.getEndLng());
            daAlarms.add(item);
        }
        logger.error(">>>dsa故障率统计数据存储结果数：  " + alarms.size());
        // 内部实体存储
        FaultCodeDaAlarmEntity entity = new FaultCodeDaAlarmEntity();
        entity.setDay(day);
        entity.setDataList(daAlarms);
       if(alarms.size()>0) {
           this.mongoDaoImpl.saveForBatch(entity);
       }
        return PlatformResponseResult.success;
    }

    @Override
    public PlatformResponseResult updateBatchOvertimeParkAlarmInfo(
            List<OvertimeParkAlarm> allTerminalOvertimeParkAlarm, long day) {
        if (allTerminalOvertimeParkAlarm.size() == 0) {
            return PlatformResponseResult.success;
        }
        logger.error(">>>DSA  overtimepark数据存储合并结果 size ： " + allTerminalOvertimeParkAlarm.size());
        OvertimeParkAlarmEntity entity = new OvertimeParkAlarmEntity();
        entity.setDay(day);
        // entity.setDataList(alarms);
        this.mongoDaoImpl.updateOverTimeEntity(entity, allTerminalOvertimeParkAlarm);
        // 内部实体存储
        return PlatformResponseResult.success;
    }

    @Override
    public PlatformResponseResult updateBatchStaytimeParkAlarmInfo(
            List<StaytimeParkAlarm> allTerminalStaytimeParkAlarm, long day) {
        if (allTerminalStaytimeParkAlarm.size() == 0) {
            return PlatformResponseResult.success;
        }
        logger.error(">>>DSA  overtimepark数据存储合并结果 size ： " + allTerminalStaytimeParkAlarm.size());
        StaytimeParkAlarmEntity entity = new StaytimeParkAlarmEntity();
        entity.setDay(day);
        // entity.setDataList(alarms);
        this.mongoDaoImpl.updateStayTimeEntity(entity, allTerminalStaytimeParkAlarm);
        // 内部实体存储
        return PlatformResponseResult.success;
    }

    @Override
    public PlatformResponseResult updateFaultCodeStatistic(List<FaultCodeEntity> alarms, long day) {
        if (alarms.size() == 0) {
            return PlatformResponseResult.success;
        }
        logger.error(">>>故障率统计数据合并结果数：" + alarms.size());
        FaultCodeDaAlarmEntity entity = new FaultCodeDaAlarmEntity();
        entity.setDay(day);
        this.mongoDaoImpl.updateFaultCodeEntity(entity, alarms);
        // 内部实体存储
        return PlatformResponseResult.success;
    }

    @Override
    public PlatformResponseResult saveBatchMilagesInfo(List<Mileages> allTerminalMileages, long day)
            {
        if (allTerminalMileages.size() == 0) {
            return PlatformResponseResult.success;
        }
        logger.error(">>>dsa>里程数据存储结果数：  " + allTerminalMileages.size());
        List<DAMilages> mileages = new ArrayList<DAMilages>();
        for (Mileages mileage : allTerminalMileages) {
            DAMilages item = new DAMilages();
            item.setBeginLat(mileage.getBeginLat());
            item.setBeginLng(mileage.getBeginLng());
            item.setEndDate(mileage.getEndDate());
            item.setEndLat(mileage.getEndLat());
            item.setEndLng(mileage.getEndLng());
            item.setCanMileage(mileage.getCanMileage());
            item.setBeginMileage(mileage.getBeginMileage());
            item.setEndMileage(mileage.getEndMileage());
            item.setElectricConsumption(mileage.getElectricConsumption());
            item.setOilConsumption(mileage.getOilConsumption());
            item.setTerminalId(mileage.getTerminalID());
            item.setStartDate(mileage.getStartDate());
            item.setStaticDate(mileage.getStaticDate());
            item.setTerminalMileage(mileage.getGpsMileage());

            item.setMeterMileage(mileage.getMeterMileage());
            item.setFuelOil(mileage.getFuelOil());
            item.setOilValue(mileage.getOilValue());
            item.setBeginGpsMileage(mileage.getBeginGpsMileage());
            item.setEndGpsMileage(mileage.getEndGpsMileage());
            item.setBeginMeMileage(mileage.getBeginMeMileage());
            item.setEndMeMileage(mileage.getEndMeMileage());
            mileages.add(item);
        }
        MilagesEntity milagesEntity = new MilagesEntity();
        milagesEntity.setDay(day);
        milagesEntity.setDataList(mileages);

        this.mongoDaoImpl.saveForBatch(milagesEntity);
        return PlatformResponseResult.success;
    }

    @Override
    public PlatformResponseResult saveBatchOvertimeParkAlarmInfo(List<OvertimeParkAlarm> allTerminalOvertimeParkAlarm,
                                                                 long day) {
        if (allTerminalOvertimeParkAlarm.size() == 0) {
            return PlatformResponseResult.success;
        }
        logger.error(">>>DSA  overtimepark数据存储结果 size ： " + allTerminalOvertimeParkAlarm.size());
        List<DAOvertimeParkAlarm> alarms = new ArrayList<>();
        for (OvertimeParkAlarm spd : allTerminalOvertimeParkAlarm) {
            DAOvertimeParkAlarm item = new DAOvertimeParkAlarm();
            item.setTerminalId(spd.getTerminalId());
            item.setAreaId(spd.getAreaId());
            item.setLimitParking(spd.getLimitParking());
            item.setContinuousTime(spd.getContinuousTime());
            item.setBeginDate(spd.getBeginDate());
            item.setEndDate(spd.getEndDate());
            item.setBeginLat(spd.getBeginLat());
            item.setEndLat(spd.getEndLat());
            item.setBeginLng(spd.getBeginLng());
            item.setEndLng(spd.getEndLng());
            item.setTailMerge(spd.getTailMerge());
            alarms.add(item);
        }
        // 内部实体存储
        OvertimeParkAlarmEntity entity = new OvertimeParkAlarmEntity();
        entity.setDay(day);
        entity.setDataList(alarms);
        this.mongoDaoImpl.saveForBatch(entity);
        return PlatformResponseResult.success;
    }

    @Override
    public PlatformResponseResult saveBatchStaytimeParkAlarmInfo(List<StaytimeParkAlarm> allTerminalStaytimeParkAlarm,
                                                                 long day) {
        if (allTerminalStaytimeParkAlarm.size() == 0) {
            return PlatformResponseResult.success;
        }
        logger.error(">>>DSA  staytimepark数据存储结果 size ： " + allTerminalStaytimeParkAlarm.size());
        List<DAStaytimeParkAlarm> alarms = new ArrayList<>();
        for (StaytimeParkAlarm spd : allTerminalStaytimeParkAlarm) {
            DAStaytimeParkAlarm item = new DAStaytimeParkAlarm();
            item.setTerminalId(spd.getTerminalId());
            item.setAreaId(spd.getAreaId());
            item.setContinuousTime(spd.getContinuousTime());
            item.setBeginDate(spd.getBeginDate());
            item.setEndDate(spd.getEndDate());
            item.setBeginLat(spd.getBeginLat());
            item.setEndLat(spd.getEndLat());
            item.setBeginLng(spd.getBeginLng());
            item.setEndLng(spd.getEndLng());
            item.setTailMerge(spd.getTailMerge());
            alarms.add(item);
        }
        // 内部实体存储
        StaytimeParkAlarmEntity entity = new StaytimeParkAlarmEntity();
        entity.setDay(day);
        entity.setDataList(alarms);
        this.mongoDaoImpl.saveForBatch(entity);
        return PlatformResponseResult.success;
    }

    public PlatformResponseResult saveMileageAndOilDataToStaticRedis(List<Mileages> mileages) {
        if (mileages.size() == 0) {
            return PlatformResponseResult.success;
        }
        List<String> terminals = new ArrayList<String>();
        List<byte[]> mileageDatas = new ArrayList<byte[]>();
        for (Mileages mileage : mileages) {
            MileageConsumption.Builder mileageAndOilData = MileageConsumption.newBuilder();
            mileageAndOilData.setTerminalID(mileage.getTerminalID());
            mileageAndOilData.setBeginLat(mileage.getBeginLat());
            mileageAndOilData.setBeginLng(mileage.getBeginLng());
            mileageAndOilData.setBeginMileage(mileage.getBeginMileage());
            mileageAndOilData.setElectricConsumption(mileage.getElectricConsumption());
            mileageAndOilData.setEndDate(mileage.getEndDate());
            mileageAndOilData.setEndLat(mileage.getEndLat());
            mileageAndOilData.setEndLng(mileage.getEndLng());
            mileageAndOilData.setEndMileage(mileage.getEndMileage());
            mileageAndOilData.setMileage(mileage.getCanMileage());
            mileageAndOilData.setOilConsumption(mileage.getOilConsumption());
            mileageAndOilData.setStartDate(mileage.getStartDate());
            mileageAndOilData.setStaticDate(mileage.getStaticDate());
            mileageAndOilData.setTerminalMileage(mileage.getGpsMileage());
            mileageAndOilData.setMeterMileage(mileage.getMeterMileage());
            mileageAndOilData.setOilValue(mileage.getOilValue());
            mileageAndOilData.setFuelOil(mileage.getFuelOil());
            mileageAndOilData.setBeginGpsMileage(mileage.getBeginGpsMileage());
            mileageAndOilData.setEndGpsMileage(mileage.getEndGpsMileage());
            mileageAndOilData.setBeginMeMileage(mileage.getBeginMeMileage());
            mileageAndOilData.setEndMeMileage(mileage.getEndMeMileage());
            terminals.add(String.valueOf(mileage.getTerminalID()));
            mileageDatas.add(mileageAndOilData.build().toByteArray());
        }
        TempGpsData.saveMileageAndOilDataToStaticRedis(terminals, mileageDatas);
        return PlatformResponseResult.success;
    }

    public PlatformResponseResult saveLastestVehicleInfoToStaticRedis(List<VehiclePassTimesRecord> vehiclePassTimesList) {
        if (vehiclePassTimesList.size() == 0) {
            return PlatformResponseResult.success;
        }
        for (VehiclePassTimesRecord vehiclePassTimes : vehiclePassTimesList) {
            VehiclePassInAreaInfo.Builder builder = VehiclePassInAreaInfo.newBuilder();
            builder.setDistrictCodes(vehiclePassTimes.getDistrict());
            for (Long tid : vehiclePassTimes.getTerminals()) {
                VehiclePassTimesDetail vehiclePassTimesDetail = vehiclePassTimes.getVehiclePassTimesDetail(tid);
                LastestVehicleInfo.Builder newVehicleInfo = LastestVehicleInfo.newBuilder();
                newVehicleInfo.setHasFaultCode(vehiclePassTimesDetail.getFaultCode());
                newVehicleInfo.setMileage(vehiclePassTimesDetail.getMileage());
                newVehicleInfo.setRunTime(vehiclePassTimesDetail.getRunTime());
                newVehicleInfo.setTid(vehiclePassTimesDetail.getTid());
                builder.addVehicleInfo(newVehicleInfo);
            }
            TempGpsData.saveLastestVehicleInfoToStaticRedis(vehiclePassTimes.getDistrict(), builder.build());
        }
        logger.error(">>>dsa车次区域数据存储结果数： " + vehiclePassTimesList.size());
        return PlatformResponseResult.success;
    }

    @Override
    public PlatformResponseResult saveDistrictTimesToStaticRedis(List<VehiclePassInDistrict> list)
            {
        // TODO Auto-generated method stub
        TempGpsData.saveDistrictTimesToStaticRedis(list);

        return PlatformResponseResult.success;
    }

    @Override
    public PlatformResponseResult updateStagnationTimeoutInfo(List<StagnationTimeoutEntity> StagnationTimeoutAlarmList,
                                                              long day) {
        if (StagnationTimeoutAlarmList.size() == 0) {
            return PlatformResponseResult.success;
        }
        logger.error(">>>停滞超时统计数据合并结果数：" + StagnationTimeoutAlarmList.size());
        StagnationTimeoutAlarmEntity entity = new StagnationTimeoutAlarmEntity();
        entity.setDay(day);
        this.mongoDaoImpl.updateStagnationTimeoutEntity(entity, StagnationTimeoutAlarmList);
        // 内部实体存储
        return PlatformResponseResult.success;
    }

    /**
     * 停滞超时撤销和恢复
     *
     * @param _id 数据记录标识
     * @param    isCancel    true：撤销；false：恢复
     * @param    recordDate    停滞超时报警开始时间
     */
    @Override
    public PlatformResponseResult stagnationTimeoutCancelOrNot(String _id,
                                                               boolean isCancel, long recordDate) {
        this.mongoDaoImpl.updateStagnationTimeoutCancelOrNot(_id, isCancel, recordDate);
        return PlatformResponseResult.success;
    }

    @Override
    public PlatformResponseResult saveBatchStaytimeParkCache(Map<Long, StaytimeParkAlarm> cache)
    {
        // TODO Auto-generated method stub
        TempGpsData.saveBatchStaytimeParkCache(cache);

        return PlatformResponseResult.success;
    }
}
