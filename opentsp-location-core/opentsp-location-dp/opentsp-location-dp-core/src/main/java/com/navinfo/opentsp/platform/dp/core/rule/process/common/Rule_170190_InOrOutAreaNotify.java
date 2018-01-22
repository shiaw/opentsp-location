package com.navinfo.opentsp.platform.dp.core.rule.process.common;

import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.cache.RuleCommonCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.NodeHelper;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.rule.handler.common.CommonRegularProcess;
import com.navinfo.opentsp.platform.dp.core.rule.handler.common.CommonRuleAnno;
import com.navinfo.opentsp.platform.dp.core.rule.status.common.Rule_170190_Status;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.LatestParkEntity;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCInOrOutAreaNotify.InOrOutAreaNotify;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCInOrOutAreaNotifySetPara.InOrOutAreaNotifySetPara;

import javax.annotation.Resource;
import java.util.List;

@CommonRuleAnno(ruleId = LCRegularCode.RegularCode.inOrOutAreaNotifySetPara_VALUE)
public class Rule_170190_InOrOutAreaNotify extends CommonRegularProcess {

    @Resource
    private RuleCache ruleCache;

    @Resource
    private RuleCommonCache ruleCommonCache;

    @Resource
    private Rule_170190_Status rule_170190_Status;

    @Override
    public GpsLocationDataEntity process(double distance,
                                         AreaEntity areaEntity, GpsLocationDataEntity dataEntity) {
        /**
         * 去掉个性规则判断，全部都走
         */
//		RuleEntity ruleEntity = ruleCache.getRuleEntity(dataEntity.getTerminalId());
//		if(ruleEntity != null){
//			boolean bool = ruleEntity.isAreaStatusNotifySwitch();
//			if(bool != true){ //判断车辆是否是未售车辆
//				return dataEntity;
//			}
//		}else{
//			return dataEntity;
//		}
        final List<RuleEntity> ruleCommonEntityList = ruleCommonCache.getRuleEntity(LCRegularCode.RegularCode.inOrOutAreaNotifySetPara_VALUE);
        if (ruleCommonEntityList == null) {
            return dataEntity;
        }
//		Rule_170190_Status status = Rule_170190_Status.getInstance();

        for (RuleEntity rule : ruleCommonEntityList) {
            List<InOrOutAreaNotifySetPara> rules = rule.getSetPara();
            for (InOrOutAreaNotifySetPara para : rules) {
                if (para.getAreaIdentify() == areaEntity.getAreaId()) {//取到区域对应的规则数据
                    String statusStr = dataEntity.getTerminalId() + "_" + para.getAreaIdentify() + "";//终端id+区域id


                    //判断当前位置是否在推送半径内,
                    if (distance <= areaEntity.getDatas().get(0).getRadiusLength()) {
                        Long fistInTime_ = rule_170190_Status.getStaytimeParkTime(statusStr);

                        if (fistInTime_ == null) {
                            log.error(statusStr + "," + dataEntity.getGpsDate() + "进服务站，保存，测试半径：" + areaEntity.getDatas().get(0).getRadiusLength() + ",距离：" + distance);
                            dataEntity.addStaytimeParkingAddition(para.getAreaIdentify(), areaEntity.getAreaType());
                            rule_170190_Status.addStaytimeParkCache(statusStr, dataEntity.getGpsDate());//保存的是首次进入服务站的时间点
                            LatestParkEntity latestParkEntity = new LatestParkEntity();
                            latestParkEntity.setTerminalId(dataEntity.getTerminalId());
                            latestParkEntity.setAreaId(para.getAreaIdentify());
                            latestParkEntity.setGpsTime(dataEntity.getGpsDate());
                            rule_170190_Status.addStayTimeCache(statusStr, latestParkEntity);
                            if(para.getStatus() != 2) {
                                sendInOrOutAreaNotify(areaEntity.getAreaId(), 1, dataEntity);
                                log.error("推送进区域数据:" + statusStr + "_distance:{},radiu:{},areaId:{}", distance, areaEntity.getDatas().get(0).getRadiusLength(), areaEntity.getAreaId());
                            }
                        }
                    } else {
                        if (rule_170190_Status.getStaytimeParkTime(statusStr) == null) {
                            //车辆没有进入该服务站，不做处理
                        } else {
                            //存在上次记录的缓存，说明车辆刚出服务站，处理：需要删除缓存信息
                            log.error(statusStr + "," + dataEntity.getGpsDate() + "出服务站，测试半径：" + areaEntity.getDatas().get(0).getRadiusLength() + ",距离：" + distance);
                            rule_170190_Status.delStaytimeParkCache(statusStr);
                            if (para.getStatus() != 1) {
                                sendInOrOutAreaNotify(areaEntity.getAreaId(), 2, dataEntity);
                                log.error("推送出区域数据:" + statusStr + "_distance:{},radiu:{},areaId:{}", distance, areaEntity.getDatas().get(0).getRadiusLength(), areaEntity.getAreaId());
                            }
                        }
                    }


//					String statusStr = dataEntity.getTerminalId()+"_" + para.getAreaIdentify()+"";//终端id+区域id
////					Boolean statusCache = status.statusInorOut(statusStr);
//					//判断当前位置是否在推送半径内,
//					if(distance<=areaEntity.getDatas().get(0).getRadiusLength()){
//						Long fistInTime_ = rule_170190_Status.getStaytimeParkTime(statusStr);
//						if(status.IscontainsKey(statusStr)){
//							if(statusCache == null){
//								//说明之前存储的值有异常，重新记录
//								status.addStatus(statusStr, true);
//							}else{
//								if(statusCache){
//									//说明上次在半径内，这次也在，不做处理
//								}else{
//									//上次记录车辆不在区域内，则这次是首次进去区域。根据规则判断，本次区域是否可以发送车辆进区域通知。0,1则为可以发送,2为只能发送出通知。
//									if(para.getStatus() != 2){
//										sendInOrOutAreaNotify(areaEntity.getAreaId(),1,dataEntity);
//										log.error("推送进区域数据:"+statusStr+"_distance:{},radiu:{},areaId:{}",distance,areaEntity.getDatas().get(0).getRadiusLength(),areaEntity.getAreaId());
//									}
//									status.addStatus(statusStr, true);
//								}
//							}
//						}else{
//							//按首点处理，添加车辆第一次进出状态缓存
//							status.addStatus(statusStr, true);
//						}
//					}else{
//						if(!status.IscontainsKey(statusStr)){
//							//无上次记录状态，添加状态为false
//							status.addStatus(statusStr, false);
//						}else{
//							if(statusCache){
//								//说明上次在区域内，这次出了区域。则根据规则判断，是否要发送出区域通知。0,2为发送,1只能发送进区域通知
//								status.addStatus(statusStr, false);
//								if(para.getStatus() != 1){
//									sendInOrOutAreaNotify(areaEntity.getAreaId(),2,dataEntity);
//									log.error("推送出区域数据:"+statusStr+"_distance:{},radiu:{},areaId:{}",distance,areaEntity.getDatas().get(0).getRadiusLength(),areaEntity.getAreaId());
//								}
//							}else{
//								//车一直在区域外，不做处理
//							}
//						}
//					}
                }
            }
        }
        return dataEntity;
    }


    //发送进出区域通知 消息ID：0x3528

    public void sendInOrOutAreaNotify(long areaId, int status, GpsLocationDataEntity dataEntity) {
        InOrOutAreaNotify.Builder builder = InOrOutAreaNotify.newBuilder();
        builder.setAreaIdentify(areaId);
        builder.setStatus(status);
        builder.setGpsDate(dataEntity.getGpsDate());
        builder.setLat(dataEntity.getLatitude());
        builder.setLon(dataEntity.getLongitude());
        Packet _packet = new Packet(true);
        _packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
        _packet.setTo(dataEntity.getTerminalId());
        _packet.setFrom(NodeHelper.getNodeCode());
        _packet.setUniqueMark(dataEntity.getUniqueMark());
        _packet.setCommand(LCAllCommands.AllCommands.Terminal.InOrOutAreaNotify_VALUE);
        _packet.setContent(builder.build().toByteArray());
        //采用一致性hash向RP节点发送数据
        sendKafkaHandler.writeKafKaToRP(_packet);
    }
}
