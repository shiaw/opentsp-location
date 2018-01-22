package com.navinfo.opentsp.platform.dp.core.rule.process.single;

import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaDataEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.dp.core.common.SpringContextUtil;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRegularProcess;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRuleAnno;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.Rule_170160_Status;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.Rule_170160_Status.Rule_170160StatusEntry;
import com.navinfo.opentsp.platform.dp.core.rule.tools.GeometricCalculation;
import com.navinfo.opentsp.platform.dp.core.rule.tools.Help.InOutMark;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCInAreaTriggerActivationOrLock.InAreaTriggerActivationOrLock;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCActivationOrLockNotify.ActivationOrLockNotify;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@SingleRuleAnno(ruleId = RegularCode.inAreaTriggerActivationOrLockNotify_VALUE)
public class Rule_170160_InAreaTriggerActivationOrLockProcess extends SingleRegularProcess {

    @Resource
    private Rule_170160_Status rule_170160_status;
    @Resource
    private AreaCache areaCache;

    private static int MAX_IDLE_TIME = 24 * 60 * 60; //区域报警的首点需要特殊处理，该变量为首点的过期时间阀值

    @Override
    public GpsLocationDataEntity process(RuleEntity rule,
                                         GpsLocationDataEntity dataEntity) {
        if (areaCache == null) {
            areaCache = (AreaCache) SpringContextUtil.getBean("areaCache");
        }
        Map<Long, AreaEntity> areaMap = areaCache.getAreaEntity(dataEntity.getTerminalId()); //获取区域
        if (areaMap == null) {
            return dataEntity;
        }
        List<InAreaTriggerActivationOrLock> rules = rule.getInAreaTriggerActivationOrLockRule(); //获取该终端锁车/激活通知的规则
        for (InAreaTriggerActivationOrLock inAreaTAOrLock : rules) {
            AreaEntity areaEntity = areaMap.get(inAreaTAOrLock.getAreaId());
            if (areaEntity == null) {
                continue;
            }
            InOutMark inOutMark = checkStatusInOrOut(areaEntity, dataEntity);
            handleAreaRegular(inOutMark, areaEntity, inAreaTAOrLock, dataEntity);

        }
        return dataEntity;
    }


    /**
     * 判断点是否在区域（圆形，矩形，多边形，路线）内
     *
     * @param areaEntity
     * @param dataEntity
     * @return 点在区域内的状态枚举
     */
    private InOutMark checkStatusInOrOut(AreaEntity areaEntity, GpsLocationDataEntity dataEntity) {
        boolean isInArea = false;
        List<AreaDataEntity> areaDataEntities = areaEntity.getDatas();
        switch (areaEntity.getAreaType()) {
            case AreaType.circle_VALUE:
                isInArea = GeometricCalculation.insideCircle(dataEntity.getLongitude(), dataEntity.getLatitude(), areaEntity.getDatas().get(0), areaEntity.getDatas().get(0).getRadiusLength());
                break;
            case AreaType.rectangle_VALUE:
                isInArea = GeometricCalculation.insideRectangle(dataEntity.getLongitude(), dataEntity.getLatitude(), areaDataEntities.get(0), areaDataEntities.get(1));
                break;
            case AreaType.polygon_VALUE:
                isInArea = GeometricCalculation.insidePolygon(dataEntity.getLongitude(), dataEntity.getLatitude(), areaDataEntities);
                break;
            case AreaType.route_VALUE:
                isInArea = GeometricCalculation.insideRoute(dataEntity.getLongitude(), dataEntity.getLatitude(), areaDataEntities);
                break;
        }
        InOutMark inoutMark = isInArea ? InOutMark.In : InOutMark.Out;
        return inoutMark;
    }

    /**
     * 区域规则处理
     *
     * @param inOutMark
     * @param areaEntity
     * @param rule
     * @param dataEntity
     */
    private void handleAreaRegular(InOutMark inOutMark, AreaEntity areaEntity, InAreaTriggerActivationOrLock rule, GpsLocationDataEntity dataEntity) {

        //首点
        if (rule_170160_status.getLastStatus(dataEntity.getTerminalId(), areaEntity.getAreaId(), areaEntity.getAreaType()) == null) {
            //首点在区域内,发送通知
            if (inOutMark == InOutMark.In) {
                activationOrLockNotify(rule.getStatus(), areaEntity.getAreaId(), dataEntity);
                log.error("首点在区域内发送通知，区域[" + areaEntity.getAreaId() + "]" +
                        "	终端id[" + dataEntity.getTerminalId() + "]" + " 	status:" + rule.getStatus() +
                        "	半径:" + areaEntity.getDatas().get(0).getRadiusLength());
            }
        } else {
            Rule_170160StatusEntry entity = rule_170160_status.getLastStatus(dataEntity.getTerminalId(), areaEntity.getAreaId(), areaEntity.getAreaType());
            long intervalTime = dataEntity.getGpsDate() - entity.getTimestemp();
            if (intervalTime >= MAX_IDLE_TIME) {//超过时间阀值，按照首点处理
                if (inOutMark == InOutMark.In) {
                    activationOrLockNotify(rule.getStatus(), areaEntity.getAreaId(), dataEntity);
                    log.error("超过时间阀值，按首点处理。首点在区域内发送通知，区域[" + areaEntity.getAreaId() + "]" +
                            "	终端id[" + dataEntity.getTerminalId() + "]" + " 	status:" + rule.getStatus() +
                            "	半径:" + areaEntity.getDatas().get(0).getRadiusLength());
                }
            } else {
                if (entity.getInOutMark() != inOutMark) {
                    //判断 区域状态是否为in，是则进区域，发送通知，否则出区域，不处理
                    if (inOutMark == InOutMark.In) {
                        activationOrLockNotify(rule.getStatus(), areaEntity.getAreaId(), dataEntity);
                        log.error("首次进区域发送通知，区域[" + areaEntity.getAreaId() + "]" +
                                "	终端id[" + dataEntity.getTerminalId() + "]" + " 	status:" + rule.getStatus() +
                                "	半径:" + areaEntity.getDatas().get(0).getRadiusLength());
                    }
                }
            }
        }
        //记录最新状态
        rule_170160_status.updateLatestStatus(dataEntity.getGpsDate(), dataEntity.getTerminalId(), areaEntity.getAreaId(), areaEntity.getAreaType(), inOutMark);
    }

    /**
     * 向上层业务系统发送 激活/锁车通知(0x3321)
     *
     * @param status
     * @param areaId
     * @param dataEntity
     */
    public void activationOrLockNotify(int status, long areaId, GpsLocationDataEntity dataEntity) {
        ActivationOrLockNotify.Builder activationOrLockNotifyBuilder = ActivationOrLockNotify.newBuilder();
        activationOrLockNotifyBuilder.setStatus(status);
        activationOrLockNotifyBuilder.addAreaIdentify(areaId);

        Packet _packet = new Packet(true);
        _packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
        _packet.setTo(dataEntity.getTerminalId());
        _packet.setFrom(NodeHelper.getNodeCode());
        _packet.setUniqueMark(dataEntity.getUniqueMark());
        _packet.setCommand(AllCommands.Terminal.ActivationOrLockNotify_VALUE);
        _packet.setContent(activationOrLockNotifyBuilder.build().toByteArray());
        //采用一致性hash向RP节点发送数据 // TODO: 16/8/24
        //RPClusters.execute(_packet);
        sendKafkaHandler.writeKafKaToRP(_packet);
        log.error("向上层应用系统推送(3321)通知 : " + dataEntity.getUniqueMark() + " , " + areaId + " , " + status);
    }
}
