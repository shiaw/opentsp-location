package com.navinfo.opentsp.platform.dp.core.acceptor.rp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.platform.configuration.OpentspQueues;
import com.navinfo.opentsp.platform.dp.command.RefreshGroupDpCacheCommand;
import com.navinfo.opentsp.platform.dp.core.handler.RPAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaProperty;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.common.LCSetAreaOperation.SetAreaOperation;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.*;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularType;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCRegularDataSave;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCSetCircleAreaAlarm;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.cache.TerminalCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.TerminalEntity;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.LCConstant.LCMessageType;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

@RPAnno(id = "2305")
@Component
public class RP_2305_SetCircleAreaAlarm extends RPCommand
{
    
    @Resource
    TerminalCache terminalCache;
    
    @Resource
	AreaCache areaCache;

    @Resource
	RuleCache ruleCache;

    @Override
    public int processor(Packet packet)
    {
        try
        {
            LCSetCircleAreaAlarm.SetCircleAreaAlarm scaa =
                LCSetCircleAreaAlarm.SetCircleAreaAlarm.parseFrom(packet.getContent());
            log.error("收到设置圆形区域请求");
            log.error(scaa.toString());
            switch (scaa.getOperations().getNumber())
            {
                case SetAreaOperation.updateArea_VALUE:
                    log.info("[DP: CircleAreaAlarm] 圆形区域规则Update操作.");
                    updateCircleArea(packet, scaa);
                    break;
                case SetAreaOperation.additionalArea_VALUE:
                    log.info("[DP: CircleAreaAlarm] 圆形区域规则Add操作.");
                    addCircleArea(packet, scaa);
                    break;
                case SetAreaOperation.modifyArea_VALUE:
                    log.info("[DP: CircleAreaAlarm] 圆形区域规则Modify操作.");
                    modifyCircleArea(packet, scaa);
                    break;
            }
            // TerminalEntity entity = TerminalCache.getInstance().getTerminal(packet.getFrom());
            TerminalEntity entity = terminalCache.getTerminal(packet.getFrom());
            if (entity != null)
            {
                if (entity.isRegularInTerminal())
                {
                    packet.setTo(packet.getFrom());
                    super.writeToTermianl(packet);
                }
            }
        }
        catch (InvalidProtocolBufferException e)
        {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        
        this.commonResponses(4,
            packet.getFrom(),
            packet.getSerialNumber(),
            packet.getCommand(),
            PlatformResponseResult.success_VALUE);
        return 0;
    }
    
    /***********************
     * 添加圆形区域规则
     * 
     * @param packet
     * @param scaa
     */
    private void addCircleArea(Packet packet, LCSetCircleAreaAlarm.SetCircleAreaAlarm scaa)
    {
        LCRegularDataSave.RegularDataSave.Builder builder = LCRegularDataSave.RegularDataSave.newBuilder();
        long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
        for (int i = 0; i < scaa.getAreasCount(); i++)
        {
            // 区域信息、规则信息 ，缓存管理(当区域大于指定数量,则删除最老的区域以及对应的规则数据)
            LCAreaInfo.AreaInfo areaInfo = areaInfo(packet, scaa.getAreas(i));
            AreaEntity areaEntity = areaCache.addAreaEntity(new AreaEntity(areaInfo));
            if (areaEntity != null)
            {
				ruleCache
                    .getRuleEntity(terminalId)
						.removeRuleForArea(areaEntity.getOriginalAreaId(), AreaType.circle);
			}
            
            // 保存新创建的规则到临时缓存中
            List<RegularData> list = ruleInfo(packet, scaa.getAreas(i));
            for (RegularData rd : list)
            {
                RuleEntity rule = ruleCache.getRuleEntity(terminalId);
                if (rule == null)
                {
                    rule = new RuleEntity();
                    rule.setTerminal(terminalId);
                }
                if (rd.hasDoorOpenOutArea())
                    rule.addDoorOpenOutAreaRule(rd.getDoorOpenOutArea());
                if (rd.hasInOutArea())
                    rule.addInOutAreaRule(rd.getInOutArea());
                if (rd.hasSpeeding())
                    rule.addAreaSpeedingRule(rd.getSpeeding());
                if (rd.hasDriverTime())
                    rule.addRouteDriverTimeRule(rd.getDriverTime());
                if (rd.hasKeyPointFence())
                {
                    rule.addKeyPointFenceRule(rd.getKeyPointFence());
                }
				ruleCache.addRuleEntity(rule);
            }
            builder.addInfos(areaInfo);
            builder.addAllDatas(list);
        }
        
        Packet _out_packet = new Packet(true);
        _out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
        _out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
        _out_packet.setCommand(AllCommands.DataAccess.RegularDataSave_VALUE);
        _out_packet.setContent(builder.build().toByteArray());
        super.writeToDataAccess(_out_packet);
    }
    
    /***********************
     * 圆形区域更新
     * 
     * @param packet
     * @param scaa
     */
    private void updateCircleArea(Packet packet, LCSetCircleAreaAlarm.SetCircleAreaAlarm scaa)
    {
        long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
        // 删除缓存中与该区域对应的规则实体，根据区域类型删除
        RuleEntity ruleEntity = ruleCache.getRuleEntity(terminalId);
        Set<Long> deleteAreaIds = ruleEntity.removeRuleForArea(AreaType.circle);
		areaCache.removeAreaEntity(Convert.uniqueMarkToLong(packet.getUniqueMark()), AreaType.circle);
        // 向DA发送数据
        LCDeleteAreaInfo.DeleteAreaInfo.Builder builder = LCDeleteAreaInfo.DeleteAreaInfo.newBuilder();
        builder.setTerminalId(terminalId);
        builder.addAllAreaIdentify(deleteAreaIds);
        
        Packet _out_packet = new Packet(true);
        _out_packet.setUniqueMark(NodeHelper.getNodeUniqueMark());
        _out_packet.setProtocol(LCMessageType.PLATFORM);
        _out_packet.setCommand(AllCommands.DataAccess.DeleteAreaInfo_VALUE);
        _out_packet.setContent(builder.build().toByteArray());
        super.writeToDataAccess(_out_packet);
        
        // 添加最新区域
        addCircleArea(packet, scaa);
    }
    
    /***********************
     * 圆形区域修改
     * 
     * @param packet
     * @param scaa
     */
    private void modifyCircleArea(Packet packet, LCSetCircleAreaAlarm.SetCircleAreaAlarm scaa)
    {
        long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
        // 缓存管理
        RuleEntity ruleEntity = ruleCache.getRuleEntity(terminalId);
        for (int i = 0, size = scaa.getAreasCount(); i < size; i++)
        {
            ruleEntity.removeRuleForArea(scaa.getAreas(i).getAreaIdentify(), AreaType.circle);
           areaCache.removeAreaEntity(terminalId,
                scaa.getAreas(i).getAreaIdentify(),
                LCAreaType.AreaType.circle);
            ruleCache.addRuleEntity(ruleEntity);
        }
        // 添加新的圆形区域对象 ， 区域规则
        addCircleArea(packet, scaa);
    }
    
    private LCAreaInfo.AreaInfo areaInfo(Packet packet, LCSetCircleAreaAlarm.CircleArea area)
    {
        LCAreaInfo.AreaInfo.Builder areaBuilder = LCAreaInfo.AreaInfo.newBuilder();
        areaBuilder.setTerminalId(Convert.uniqueMarkToLong(packet.getUniqueMark()));
        areaBuilder.setAreaIdentify(area.getAreaIdentify());
        areaBuilder.setTypes(LCAreaType.AreaType.circle);
        areaBuilder.setCreateDate(System.currentTimeMillis() / 1000);
        LCAreaData.AreaData.Builder dataBuilder = LCAreaData.AreaData.newBuilder();
        dataBuilder.setDataSN(0);
        dataBuilder.setLatitude(area.getCenterLatitude());
        dataBuilder.setLongitude(area.getCenterLongitude());
        dataBuilder.setRadiusLength(area.getRadius());
        areaBuilder.addDatas(dataBuilder.build());
        return areaBuilder.build();
    }
    
    /********************
     * 创建区域对应的规则. . 超速报警 OverSpeed . 进出区域报警 InOutArea . 区域外开门报警 DoorOpenedOutsideArea
     * 
     * @param packet
     * @param area
     * @return
     */
    private List<LCRegularData.RegularData> ruleInfo(Packet packet, LCSetCircleAreaAlarm.CircleArea area)
    {
        List<LCRegularData.RegularData> list = new ArrayList<LCRegularData.RegularData>();
        int areaProperty = area.getAreaProperty();
        long terminalId = Convert.uniqueMarkToLong(packet.getUniqueMark());
        
        // 区域超速报警
        if ((areaProperty & LCAreaProperty.AreaProperty.speedLimit_VALUE) == LCAreaProperty.AreaProperty.speedLimit_VALUE)
        {
            LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
            regularBuilder.setTerminalId(terminalId);
            regularBuilder.setRegularCode(LCRegularCode.RegularCode.speeding);
            regularBuilder.setLastModifyDate(System.currentTimeMillis() / 1000);
            regularBuilder.setType(RegularType.individual);// 是否为通用规则
            LCAreaSpeeding.AreaSpeeding.Builder areaSpeedBuild = LCAreaSpeeding.AreaSpeeding.newBuilder();
            areaSpeedBuild.setAreaId(area.getAreaIdentify());
            areaSpeedBuild.setTypes(LCAreaType.AreaType.circle);
            areaSpeedBuild.setMaxSpeed(area.getMaxSpeed());
            areaSpeedBuild.setContinuousTime(area.getSpeedingContinuousTime());
            // 是否据时间
            if ((areaProperty & LCAreaProperty.AreaProperty.accordingTime_VALUE) == LCAreaProperty.AreaProperty.accordingTime_VALUE)
            {
                areaSpeedBuild.setBasedTime(true);
                
                // 是否每天
                boolean isEveryday =
                    (areaProperty & LCAreaProperty.AreaProperty.everyDay_VALUE) == LCAreaProperty.AreaProperty.everyDay_VALUE ? true
                        : false;
                if (isEveryday)
                {
                    Calendar start = DateUtils.calendar(area.getBeginDate());
                    start.set(Calendar.YEAR, 1970);
                    start.set(Calendar.MONTH, 1);
                    start.set(Calendar.DAY_OF_MONTH, 1);
                    
                    Calendar end = DateUtils.calendar(area.getEndDate());
                    end.set(Calendar.YEAR, 1970);
                    end.set(Calendar.MONTH, 1);
                    end.set(Calendar.DAY_OF_MONTH, 1);
                    
                    areaSpeedBuild.setStartDate(start.getTimeInMillis() / 1000);
                    areaSpeedBuild.setEndDate(end.getTimeInMillis() / 1000);
                }
                else
                {
                    areaSpeedBuild.setStartDate(area.getBeginDate());
                    areaSpeedBuild.setEndDate(area.getEndDate());
                }
                
                areaSpeedBuild.setIsEveryDay(isEveryday);
                log.info("[DP: RouteAreaAlarm] 圆形区域规则 类型. [区域超速报警, 按照时间报警 True, 是否每天" + isEveryday + "]");
            }
            else
            {
                areaSpeedBuild.setBasedTime(false);
                log.info("[DP: RouteAreaAlarm] 圆形区域规则 类型. [区域超速报警, 按照时间报警 False]");
            }
            regularBuilder.setSpeeding(areaSpeedBuild.build());
            
            list.add(regularBuilder.build());
        }
        
        // 进出区域报警
        if ((areaProperty & LCAreaProperty.AreaProperty.inAreaAlarmToDriver_VALUE) == LCAreaProperty.AreaProperty.inAreaAlarmToDriver_VALUE
            || (areaProperty & LCAreaProperty.AreaProperty.inAreaAlarmToPlatform_VALUE) == LCAreaProperty.AreaProperty.inAreaAlarmToPlatform_VALUE
            || (areaProperty & LCAreaProperty.AreaProperty.outAreaAlarmToDriver_VALUE) == LCAreaProperty.AreaProperty.outAreaAlarmToDriver_VALUE
            || (areaProperty & LCAreaProperty.AreaProperty.outAreaAlarmToPlatform_VALUE) == LCAreaProperty.AreaProperty.outAreaAlarmToPlatform_VALUE)
        {
            
            LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
            regularBuilder.setTerminalId(terminalId);
            regularBuilder.setType(RegularType.individual);// 是否为通用规则
            regularBuilder.setRegularCode(LCRegularCode.RegularCode.inOutArea);
            regularBuilder.setLastModifyDate(System.currentTimeMillis() / 1000);
            LCInOutArea.InOutArea.Builder inOutArea = LCInOutArea.InOutArea.newBuilder();
            inOutArea.setAreaId(area.getAreaIdentify());
            // 是否据时间
            if ((areaProperty & LCAreaProperty.AreaProperty.accordingTime_VALUE) == LCAreaProperty.AreaProperty.accordingTime_VALUE)
            {
                inOutArea.setBasedTime(true);
                inOutArea.setStartDate(area.getBeginDate());
                inOutArea.setEndDate(area.getEndDate());
                
                // 是否每天
                boolean isEveryday =
                    (areaProperty & LCAreaProperty.AreaProperty.everyDay_VALUE) == LCAreaProperty.AreaProperty.everyDay_VALUE ? true
                        : false;
                if (isEveryday)
                {
                    Calendar start = DateUtils.calendar(area.getBeginDate());
                    start.set(Calendar.YEAR, 1970);
                    start.set(Calendar.MONTH, 1);
                    start.set(Calendar.DAY_OF_MONTH, 1);
                    
                    Calendar end = DateUtils.calendar(area.getEndDate());
                    end.set(Calendar.YEAR, 1970);
                    end.set(Calendar.MONTH, 1);
                    end.set(Calendar.DAY_OF_MONTH, 1);
                    
                    inOutArea.setStartDate(start.getTimeInMillis() / 1000);
                    inOutArea.setEndDate(end.getTimeInMillis() / 1000);
                }
                else
                {
                    inOutArea.setStartDate(area.getBeginDate());
                    inOutArea.setEndDate(area.getEndDate());
                }
                inOutArea.setIsEveryDay(isEveryday);
                log.info("[DP: RouteAreaAlarm] 圆形区域规则 类型. [进出区域报警, 按照时间报警 True, 是否每天" + isEveryday + "]");
            }
            else
            {
                inOutArea.setBasedTime(false);
                log.info("[DP: RouteAreaAlarm] 圆形区域规则 类型. [进出区域报警, 按照时间报警 False]");
            }
            inOutArea.setInAreaAlarmToDriver((areaProperty & LCAreaProperty.AreaProperty.inAreaAlarmToDriver_VALUE) == LCAreaProperty.AreaProperty.inAreaAlarmToDriver_VALUE ? true
                : false);
            inOutArea.setInAreaAlarmToPlatform((areaProperty & LCAreaProperty.AreaProperty.inAreaAlarmToPlatform_VALUE) == LCAreaProperty.AreaProperty.inAreaAlarmToPlatform_VALUE ? true
                : false);
            inOutArea.setOutAreaAlarmToDriver((areaProperty & LCAreaProperty.AreaProperty.outAreaAlarmToDriver_VALUE) == LCAreaProperty.AreaProperty.outAreaAlarmToDriver_VALUE ? true
                : false);
            inOutArea.setOutAreaAlarmToPlatform((areaProperty & LCAreaProperty.AreaProperty.outAreaAlarmToPlatform_VALUE) == LCAreaProperty.AreaProperty.outAreaAlarmToPlatform_VALUE ? true
                : false);
            regularBuilder.setInOutArea(inOutArea.build());
            list.add(regularBuilder.build());
        }
        
        // 区域外开门报警
        if ((areaProperty & LCAreaProperty.AreaProperty.openDoorOutArea_VALUE) == LCAreaProperty.AreaProperty.openDoorOutArea_VALUE)
        {
            LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
            regularBuilder.setTerminalId(terminalId);
            regularBuilder.setType(RegularType.individual);// 是否为通用规则
            regularBuilder.setRegularCode(LCRegularCode.RegularCode.doorOpenOutArea);
            regularBuilder.setLastModifyDate(System.currentTimeMillis() / 1000);
            LCDoorOpenOutArea.DoorOpenOutArea.Builder doorOpen = LCDoorOpenOutArea.DoorOpenOutArea.newBuilder();
            doorOpen.setAreaId(area.getAreaIdentify());
            // 是否据时间
            if ((areaProperty & LCAreaProperty.AreaProperty.accordingTime_VALUE) == LCAreaProperty.AreaProperty.accordingTime_VALUE)
            {
                doorOpen.setBasedTime(true);
                doorOpen.setStartDate(area.getBeginDate());
                doorOpen.setEndDate(area.getEndDate());
                
                // 是否每天
                boolean isEveryday =
                    (areaProperty & LCAreaProperty.AreaProperty.everyDay_VALUE) == LCAreaProperty.AreaProperty.everyDay_VALUE ? true
                        : false;
                if (isEveryday)
                {
                    Calendar start = DateUtils.calendar(area.getBeginDate());
                    start.set(Calendar.YEAR, 1970);
                    start.set(Calendar.MONTH, 1);
                    start.set(Calendar.DAY_OF_MONTH, 1);
                    
                    Calendar end = DateUtils.calendar(area.getEndDate());
                    end.set(Calendar.YEAR, 1970);
                    end.set(Calendar.MONTH, 1);
                    end.set(Calendar.DAY_OF_MONTH, 1);
                    
                    doorOpen.setStartDate(start.getTimeInMillis() / 1000);
                    doorOpen.setEndDate(end.getTimeInMillis() / 1000);
                }
                else
                {
                    doorOpen.setStartDate(area.getBeginDate());
                    doorOpen.setEndDate(area.getEndDate());
                }
                doorOpen.setIsEveryDay(isEveryday);
                log.info("[DP: RouteAreaAlarm] 圆形区域规则 类型. [区域外开门报警, 按照时间报警 True, 是否每天" + isEveryday + "]");
            }
            else
            {
                doorOpen.setBasedTime(false);
                log.info("[DP: RouteAreaAlarm] 圆形区域规则 类型. [区域外开门报警, 按照时间报警 False]");
            }
            regularBuilder.setDoorOpenOutArea(doorOpen.build());
            list.add(regularBuilder.build());
        }
        // 关键点围栏规则
        if ((areaProperty & LCAreaProperty.AreaProperty.accordingTime_VALUE) == LCAreaProperty.AreaProperty.accordingTime_VALUE)
        {
            boolean notArriveFenceAlarmToDriver =
                ((areaProperty & LCAreaProperty.AreaProperty.notArriveFenceAlarmToDriver_VALUE) == LCAreaProperty.AreaProperty.notArriveFenceAlarmToDriver_VALUE);
            boolean notArriveFenceAlarmToPlatform =
                ((areaProperty & LCAreaProperty.AreaProperty.notArriveFenceAlarmToPlatform_VALUE) == LCAreaProperty.AreaProperty.notArriveFenceAlarmToPlatform_VALUE);
            boolean notLeaveFenceAlarmToDriver =
                ((areaProperty & LCAreaProperty.AreaProperty.notLeaveFenceAlarmToDriver_VALUE) == LCAreaProperty.AreaProperty.notLeaveFenceAlarmToDriver_VALUE);
            boolean notLeaveFenceAlarmToPlatform =
                ((areaProperty & LCAreaProperty.AreaProperty.notLeaveFenceAlarmToPlatform_VALUE) == LCAreaProperty.AreaProperty.notLeaveFenceAlarmToPlatform_VALUE);
            if (notArriveFenceAlarmToDriver || notArriveFenceAlarmToPlatform || notLeaveFenceAlarmToDriver
                || notLeaveFenceAlarmToPlatform)
            {
                if ((notArriveFenceAlarmToDriver || notArriveFenceAlarmToPlatform)
                    && (notLeaveFenceAlarmToDriver || notLeaveFenceAlarmToPlatform))
                {
                    log.info("[DP: KeyPointFence] 关键点围栏（圆形）. [关键点围栏报警, 未按时到达和未按时离开同时设置 False]，规则错误！！");
                }
                else
                {
                    LCRegularData.RegularData.Builder regularBuilder = LCRegularData.RegularData.newBuilder();
                    regularBuilder.setTerminalId(terminalId);
                    regularBuilder.setType(RegularType.individual);// 是否为通用规则
                    regularBuilder.setRegularCode(LCRegularCode.RegularCode.keyPointFence);
                    regularBuilder.setLastModifyDate(System.currentTimeMillis() / 1000);
                    LCKeyPointFence.KeyPointFence.Builder keyPointFence = LCKeyPointFence.KeyPointFence.newBuilder();
                    keyPointFence.setAreaId(area.getAreaIdentify());
                    // 未按时到达
                    keyPointFence.setNotArriveFenceAlarmToDriver(notArriveFenceAlarmToDriver);
                    keyPointFence.setNotArriveFenceAlarmToPlatform(notArriveFenceAlarmToPlatform);
                    // 未按时离开
                    keyPointFence.setNotLeaveFenceAlarmToDriver(notLeaveFenceAlarmToDriver);
                    keyPointFence.setNotLeaveFenceAlarmToPlatform(notLeaveFenceAlarmToPlatform);
                    keyPointFence.setIsEveryDay((areaProperty & LCAreaProperty.AreaProperty.everyDay_VALUE) == LCAreaProperty.AreaProperty.everyDay_VALUE ? true
                        : false);
                    if (keyPointFence.getIsEveryDay())
                    {
                        Calendar start = DateUtils.calendar(area.getBeginDate());
                        start.set(Calendar.YEAR, 1970);
                        start.set(Calendar.MONTH, 1);
                        start.set(Calendar.DAY_OF_MONTH, 1);
                        
                        Calendar end = DateUtils.calendar(area.getEndDate());
                        end.set(Calendar.YEAR, 1970);
                        end.set(Calendar.MONTH, 1);
                        end.set(Calendar.DAY_OF_MONTH, 1);
                        
                        keyPointFence.setStartDate(start.getTimeInMillis() / 1000);
                        keyPointFence.setEndDate(end.getTimeInMillis() / 1000);
                    }
                    else
                    {
                        keyPointFence.setStartDate(area.getBeginDate());
                        keyPointFence.setEndDate(area.getEndDate());
                    }
                    regularBuilder.setKeyPointFence(keyPointFence.build());
                    list.add(regularBuilder.build());
                }
                
            }
        }
        else
        {
            log.info("[DP: KeyPointFence] 关键点围栏（圆形）. [关键点围栏报警,区域属性未设置根据时间 False]，规则错误！！");
        }
        return list;
    }
}
