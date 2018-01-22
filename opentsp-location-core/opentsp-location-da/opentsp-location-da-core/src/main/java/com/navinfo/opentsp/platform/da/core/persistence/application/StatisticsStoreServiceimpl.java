package com.navinfo.opentsp.platform.da.core.persistence.application;

import com.navinfo.opentsp.platform.da.core.persistence.StatisticsStoreService;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.DriverLoginEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.TerminalOnOffLineStatusEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.WFlowEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DAWFlow;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.BaseMongoDao;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.BaseMongoDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.WFlowServiceImp;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.DriverLogin;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.TerminalOnOffLineStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 统计数据实时存储服务
 *
 * @author jin_s
 */
public class StatisticsStoreServiceimpl implements StatisticsStoreService {

    public StatisticsStoreServiceimpl() {
        super();

    }

    /**
     * 一千
     */
    private static final int THOUSAND = 1000;

    BaseMongoDao mongoDaoImpl = new BaseMongoDaoImpl();

    WFlowServiceImp wFlowServiceImp = new WFlowServiceImp();


    /**
     * 保存终端产生驾驶员登录统计信息
     *
     * @param terminalId
     * @param driverLogin
     * @return
     */
    @Override
    public PlatformResponseResult saveDriverLoginInfo(long terminalId,
                                                      DriverLogin driverLogin) {
        long day = driverLogin.getCreditDate();
        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List< com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DADriverLogin> driverInforList = new ArrayList<>();

         com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DADriverLogin item = new  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DADriverLogin();
        item.setBeginLat(driverLogin.getBeginLat());
        item.setBeginLng(driverLogin.getBeginLng());
        item.setEndLat(driverLogin.getEndLat());
        item.setAgencyName(driverLogin.getAgencyName());
        item.setCertificateValid(driverLogin.getCertificateValid());
        item.setCreditDate(driverLogin.getCreditDate());
        item.setDriverCertificate(driverLogin.getDriverCertificate());
        item.setDriverIdCode(driverLogin.getDriverIdCode());
        item.setDriverName(driverLogin.getDriverName());
        item.setDriverTime(driverLogin.getDriverTime());
        item.setEndLNG(driverLogin.getEndLNG());
        item.setFailedReason(driverLogin.getFailedReason());
        item.setStubbsCard(driverLogin.getStubbsCard());
        item.setTerminalId(driverLogin.getTerminalId());
        driverInforList.add(item);

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
     * @param terminalOnOffLineStatus
     * @return
     */
    @Override
    public PlatformResponseResult saveTerminalOnOffLineStatusInfo(
            long terminalId, TerminalOnOffLineStatus terminalOnOffLineStatus) {
        long day = terminalOnOffLineStatus.getBeginDate();

         com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DATerminalOnOffLineStatus item = new  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DATerminalOnOffLineStatus();

        item.setBeginDate(terminalOnOffLineStatus.getBeginDate());
        item.setTerminalId(terminalOnOffLineStatus.getTerminalId());
        item.setOnlineStatus(terminalOnOffLineStatus.getOnlineStatus());
        item.setContinuousTime(terminalOnOffLineStatus.getContinuousTime());
        item.setEndDate(terminalOnOffLineStatus.getEndDate());

        List< com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DATerminalOnOffLineStatus> terOnOffLineList = new ArrayList();
        terOnOffLineList.add(item);

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
     * @param day        (数据格式为时间的YYMMDD)
     * @param wFlowList
     * @return
     */

    private PlatformResponseResult saveWFlowInfo(long terminalId, long day,
                                                 List<DAWFlow> wFlowList) {

        // 注意： 这里需要将外部的RMI对象转化为DA内容的实体对象
        List< com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DAWFlow> wfList = new ArrayList<>();
        for (DAWFlow spd : wFlowList) {
             com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DAWFlow item = new  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DAWFlow();
            item.setDownFlow(spd.getDownFlow());
            item.setFlowDate(spd.getFlowDate());
            item.setUpFlow(spd.getUpFlow());
            wfList.add(item);
        }

        // 内部实体存储
        WFlowEntity wFlowEntity = new WFlowEntity();
        wFlowEntity.setTerminal_id(terminalId);
        wFlowEntity.setDay(day);
        wFlowEntity.setDataList(wfList);
        this.mongoDaoImpl.save(wFlowEntity);
        return PlatformResponseResult.success;
    }

    /**
     * 保存流量统计信息
     *
     * @param terminalId
     * @param hourOfDay  (数据格式为UTC时间)
     * @param upFlow
     * @param downFlow
     * @return
     */

    public PlatformResponseResult saveTerminalWFlowInfo(long terminalId,
                                                        long hourOfDay, float upFlow, float downFlow) {

        DAWFlow wflow = new DAWFlow();
        //上行流量 单位KB
        wflow.setUpFlow(upFlow / 1024);
        //下行流量 单位KB
        wflow.setDownFlow(downFlow / 1024);
        //放入缓存
        saveDataToCache(terminalId, hourOfDay, wflow);
        return PlatformResponseResult.success;
    }

    /**
     * 保存终端流量信息到缓存
     *
     * @param terminalId
     * @param hourOfDay
     * @param wFlow
     */
    private void saveDataToCache(long terminalId, long hourOfDay, DAWFlow wFlow) {
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(hourOfDay * THOUSAND);
        wFlow.setFlowDate(calender.get(Calendar.HOUR_OF_DAY));
        // (数据格式为时间的YYMMDD)
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        Date date = calender.getTime();
        String dateString = sf.format(date);
        //获取缓存的流量数据
        Object obj = wFlowServiceImp.findTerminalData(terminalId);
        if (obj == null) {
            FlowCacheEntity flowData = new FlowCacheEntity();
            flowData.setDate(dateString);
            flowData.addFlowData(wFlow);
            //新增缓存数据
            wFlowServiceImp.putCacheData(String.valueOf(terminalId), flowData);

        } else {
            FlowCacheEntity flowData = (FlowCacheEntity) obj;
            String flowCacheDate = flowData.getDate();
            //日期相同，则追加流量数据到缓存
            if (dateString.equals(flowCacheDate)) {
                flowData.addFlowData(wFlow);
                //更新数据
                wFlowServiceImp.putCacheData(String.valueOf(terminalId), flowData);
            } else {
                //日期不同，需将前一日产生的流量信息存到mongo中
                List<DAWFlow> dataList = flowData.getDataList();
                try {
                    // 内部实体存储
                    PlatformResponseResult saveWFlowInfo = saveWFlowInfo(terminalId, sf.parse(flowCacheDate).getTime() / 1000, dataList);
                    if (PlatformResponseResult.success.equals(saveWFlowInfo))
                        flowData.clearFloeDate();
                    flowData.addFlowData(wFlow);
                    flowData.setDate(dateString);
                    //更新缓存
                    wFlowServiceImp.putCacheData(String.valueOf(terminalId), flowData);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        }

    }

}
