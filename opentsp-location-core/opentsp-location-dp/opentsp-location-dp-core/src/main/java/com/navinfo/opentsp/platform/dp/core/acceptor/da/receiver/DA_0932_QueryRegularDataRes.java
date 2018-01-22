package com.navinfo.opentsp.platform.dp.core.acceptor.da.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.dp.core.acceptor.da.DACommand;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCommonCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.Constant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.regular.LCQueryRegularDataRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component(value = "dA_1932_QueryRegularDataRes")
public class DA_0932_QueryRegularDataRes extends DACommand {

    public static Logger logger = LoggerFactory.getLogger(DA_0932_QueryRegularDataRes.class);

    @Resource
    private RuleCache ruleCache;

    @Resource
    private RuleCommonCache ruleCommonCache;

    @Override
    public int processor(Packet packet) {
        int i = 0;
        try {
            long s = System.currentTimeMillis();
            LCQueryRegularDataRes.QueryRegularDataRes regularDataRes =
                    LCQueryRegularDataRes.QueryRegularDataRes.parseFrom(packet.getContent());
            List<LCRegularData.RegularData> regularDatas = regularDataRes.getDatasList();

            Map<String, RuleEntity> ruleMap = new HashMap<>();
            for (LCRegularData.RegularData regularData : regularDatas) {
                i++;
                logger.info("更新规则缓存 key is : " + Constant.RULE_CACHE_KEY + "_"
                        + String.valueOf(regularData.getRegularCode().getNumber())+",count:"+i);

                if (regularData.getType().getNumber() == LCRegularData.RegularType.individual_VALUE) {
                    // RuleEntity ruleEntity = RuleCache.getInstance().getRuleEntity(regularData.getTerminalId());
                    RuleEntity ruleEntity = ruleCache.getRuleEntity(regularData.getTerminalId());
                    if (ruleEntity == null) {
                        ruleEntity = new RuleEntity();
                        ruleEntity.setTerminal(regularData.getTerminalId());
                        ruleCache.addRuleEntity(ruleEntity);
                    }
                    switch (regularData.getRegularCode().getNumber()) {
                        case LCRegularCode.RegularCode.speeding_VALUE:
                            ruleEntity.addAreaSpeedingRule(regularData.getSpeeding());
                            break;
                        case LCRegularCode.RegularCode.inOutArea_VALUE:
                            ruleEntity.addInOutAreaRule(regularData.getInOutArea());
                            break;
                        case LCRegularCode.RegularCode.routeDriverTime_VALUE:
                            ruleEntity.addRouteDriverTimeRule(regularData.getDriverTime());
                            break;
                        case LCRegularCode.RegularCode.driverNotCard_VALUE:
                            ruleEntity.setDriverNotCardRule(regularData.getDriverNotCard());
                            break;
                        case LCRegularCode.RegularCode.doorOpenOutArea_VALUE:
                            ruleEntity.addDoorOpenOutAreaRule(regularData.getDoorOpenOutArea());
                            break;
                        case LCRegularCode.RegularCode.drivingBan_VALUE:
                            ruleEntity.setDrivingBanRule(regularData.getDrivingBan());
                            break;
                        case LCRegularCode.RegularCode.keyPointFence_VALUE:
                            ruleEntity.addKeyPointFenceRule(regularData.getKeyPointFence());
                            break;
                        case LCRegularCode.RegularCode.outregionToLSpeed_VALUE:
                            ruleEntity.setOutRegionToLSpeedRule(regularData.getOutregionToLSpeed());
                            break;
                        case LCRegularCode.RegularCode.delayOvertimePark_VALUE:
                            ruleEntity.setDelayPark(regularData.getDelayPark());
                            break;
                        case LCRegularCode.RegularCode.terminalBroadcastSwitch_VALUE:
                            ruleEntity.setTerminalMessage(regularData.getTerminalMessage());
                            break;
                        case LCRegularCode.RegularCode.inAreaTriggerActivationOrLockNotify_VALUE:
                            ruleEntity.addInAreaTriggerActivationOrLock(regularData.getInAreaTriggerActivationOrLock());
                            break;
                        case LCRegularCode.RegularCode.inOrOutAreaNotifySwitchPara_VALUE:
                            ruleEntity.setAreaStatusNotifySwitch(true);
                            break;
                        default:
                            break;
                    }
                    ruleMap.put(regularData.getTerminalId()+"", ruleEntity);
                } else if (regularData.getType().getNumber() == LCRegularData.RegularType.common_VALUE) {
                    long key = regularData.getRegularCode().getNumber();
                    List<RuleEntity> ruleEntityList = ruleCommonCache.getRuleEntity(key);
                    RuleEntity ruleEntity = null;
                    logger.info("更新通用规则缓存 key is : " + key+",count:"+i);
                    if (ruleEntityList != null) {
                        for (RuleEntity temp : ruleEntityList) {
                            if (temp == null) {
                                ruleEntity = new RuleEntity();
                                ruleEntity.setRegularCode(regularData.getRegularCode());
                            }
                            switch (regularData.getRegularCode().getNumber()) {
                                case LCRegularCode.RegularCode.messageBroadcast_VALUE:
                                    temp.setMessageBroadcastRule(regularData.getMessageBroadcast());
                                    break;
                                case LCRegularCode.RegularCode.overtimePark_VALUE:
                                    temp.setOverTimeParkRule(regularData.getOvertimePark());
                                    break;
                                case LCRegularCode.RegularCode.inOrOutAreaNotifySetPara_VALUE:
                                    temp.setInOrOutAreaNotifySetParaRule(regularData.getSetPara());
                                    break;
                                default:
                                    break;
                            }
                            ruleEntity = temp;
                        }
                    } else {
                        //缓存中没有 这个规则的，直接添加缓存
                        ruleEntity = new RuleEntity();
                        ruleEntity.setRegularCode(regularData.getRegularCode());
                        switch (regularData.getRegularCode().getNumber()) {
                            case LCRegularCode.RegularCode.messageBroadcast_VALUE:
                                ruleEntity.setMessageBroadcastRule(regularData.getMessageBroadcast());
                                break;
                            case LCRegularCode.RegularCode.overtimePark_VALUE:
                                ruleEntity.setOverTimeParkRule(regularData.getOvertimePark());
                                break;
                            case LCRegularCode.RegularCode.inOrOutAreaNotifySetPara_VALUE:
                                ruleEntity.setInOrOutAreaNotifySetParaRule(regularData.getSetPara());
                                break;
                            default:
                                break;
                        }
                    }
                    ruleCommonCache.addRuleEntity(ruleEntity);
                }
            }
            ruleCache.batchSet(Constant.RULE_CACHE_KEY,ruleMap);

            logger.error("加载规则0932规则数据成功!数量:{},耗时:{}",i,System.currentTimeMillis()-s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
