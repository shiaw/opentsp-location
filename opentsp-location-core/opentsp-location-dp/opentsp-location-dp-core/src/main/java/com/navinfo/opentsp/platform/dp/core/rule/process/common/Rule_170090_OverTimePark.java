package com.navinfo.opentsp.platform.dp.core.rule.process.common;

import com.navinfo.opentsp.platform.dp.core.cache.RuleCommonCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.rule.handler.common.CommonRegularProcess;
import com.navinfo.opentsp.platform.dp.core.rule.handler.common.CommonRuleAnno;
import com.navinfo.opentsp.platform.dp.core.rule.status.common.DelayTimeCache;
import com.navinfo.opentsp.platform.dp.core.rule.status.common.FilterCache;
import com.navinfo.opentsp.platform.dp.core.rule.status.common.Rule_170090_Status;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllAlarm;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOverTimePark.OverTimePark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

/**
 * 站内超时报警
 * 青汽平台--进区域滞留通知--0x3526不用，屏蔽此通知
 * 规则编码：overtimePark	170090	区域滞留超时    消息ID：0x2502
 */
@CommonRuleAnno(ruleId = RegularCode.overtimePark_VALUE)
public class Rule_170090_OverTimePark extends CommonRegularProcess {

	@Resource
	private RuleCommonCache ruleCommonCache;

    @Resource
    private DelayTimeCache delayTimeCache;
	/*@Resource
	private DelayTimeCache delayTimeCache_;*/

	@Resource
	private Rule_170090_Status rule_170090_Status;

	public static Logger log = LoggerFactory.getLogger(Rule_170090_OverTimePark.class);

	@Override
	public GpsLocationDataEntity process(double distance , final AreaEntity areaEntity, final GpsLocationDataEntity dataEntity) {
		//通用规则
		final List<RuleEntity> ruleCommonEntityList = ruleCommonCache.getRuleEntity(RegularCode.overtimePark_VALUE);

		if(ruleCommonEntityList!=null){
			for(RuleEntity ruleEntity:ruleCommonEntityList){
				if(ruleEntity == null) {
					return dataEntity;
				}
				List<OverTimePark> rules = ruleEntity.getOvertimePark(); //
				if(rules==null){
					return dataEntity;
				}
				for(OverTimePark rule : rules){
					if(rule.getAreaId()==areaEntity.getAreaId()){//取到区域对应的规则数据
						String statusStr = dataEntity.getTerminalId()+"_" + rule.getAreaId();//终端id+区域id
						//判断当前位置是否在推送半径内, 比较距离和服务站半径的大小
						if(distance<=areaEntity.getDatas().get(0).getRadiusLength()){
							//此时在服务站内，需要判断缓存保存的上一个状态

							//停留超时，只要进服务站，就打标记
							dataEntity.addStaytimeParkingAddition(rule.getAreaId(), areaEntity.getAreaType());

							Long fistInTime = rule_170090_Status.getOvertimeParkTime(statusStr);
							/*Long fistInTime_ = rule_170090_Status.getStaytimeParkTime(statusStr);
							if(fistInTime_ == null){
								log.error(statusStr+","+dataEntity.getGpsDate()+"进服务站，保存，测试半径："+areaEntity.getDatas().get(0).getRadiusLength()+",距离："+distance);
								dataEntity.addStaytimeParkingAddition(rule.getAreaId(), areaEntity.getAreaType());
								rule_170090_Status.addStaytimeParkCache(statusStr, dataEntity.getGpsDate());//保存的是首次进入服务站的时间点
							}else{
								if((dataEntity.getGpsDate()- fistInTime_)>0) { //超过某个停留时间报警
									if(delayTimeCache_.isDelayTime(dataEntity.getUniqueMark())){
										//存在延时，需要判断GPS时间与延时终止时间戳
										//当前GPS时间小于延时终止时间戳，则不报警。如果大于，则继续报警
										if(dataEntity.getGpsDate()>=delayTimeCache_.getDelayTime(dataEntity.getUniqueMark())){

											log.info("终端【"+dataEntity.getTerminalId()+"】服务站【区域ID："+rule.getAreaId()+"】区域停留报警,当前GPS时间："
													+dataEntity.getGpsDate()+" 延时时间:"+delayTimeCache_.getDelayTime(dataEntity.getUniqueMark())
													+",剩余延时时间:"+(delayTimeCache_.getDelayTime(dataEntity.getUniqueMark())-dataEntity.getGpsDate())+",延时设置到期，清除延时缓存");
											dataEntity.addStaytimeParkingAddition(rule.getAreaId(), areaEntity.getAreaType());

											delayTimeCache_.delDelayTimeCache(dataEntity.getUniqueMark());
										}else{
											log.info("终端【"+dataEntity.getTerminalId()+"】服务站【区域ID："+rule.getAreaId()+"】区域停留延时处理，当前GPS时间："
													+dataEntity.getGpsDate()+" 延时时间:"+delayTimeCache_.getDelayTime(dataEntity.getUniqueMark())
													+",剩余延时时间:"+(delayTimeCache_.getDelayTime(dataEntity.getUniqueMark())-dataEntity.getGpsDate())+"秒");
											//如果当前时间大于延时终止时间戳，则清除该缓存。
//											DelayTimeCache.getInstance().delDelayTimeCache(dataEntity.getUniqueMark());
										}
									}else{
										log.error("终端【"+dataEntity.getTerminalId()+"】服务站【区域ID："+rule.getAreaId()+"】区域停留报警");
										dataEntity.addStaytimeParkingAddition(rule.getAreaId(), areaEntity.getAreaType());
									}
								}
							}*/
							if(fistInTime == null){
								//不存在此车缓存状态，认为是首次进入该服务站，此时处理：添加车在此服务站内部的缓存，
								//1、添加车在此服务站内部的缓存
								rule_170090_Status.addOvertimeParkCache(statusStr, dataEntity.getGpsDate());//保存的是首次进入服务站的时间点
							}else{
								//存在上次记录的缓存，需要判断缓存状态
								//缓存中保存的首次进入时间
								if((dataEntity.getGpsDate()- fistInTime)>rule.getOvertimeLimit()){//rule.getOvertimeLimit()
									//超过最大滞留时间，需要处理：①、添加报警滞留超时报警
									//①、添加报警滞留超时报警:
									//do .....
									if(delayTimeCache.isDelayTime(dataEntity.getUniqueMark())){
										//存在延时，需要判断GPS时间与延时终止时间戳
										//当前GPS时间小于延时终止时间戳，则不报警。如果大于，则继续报警
										if(dataEntity.getGpsDate()>=delayTimeCache.getDelayTime(dataEntity.getUniqueMark())){

											log.info("终端【"+dataEntity.getTerminalId()+"】服务站【区域ID："+rule.getAreaId()+"】滞留超时报警,当前GPS时间："
													+dataEntity.getGpsDate()+" 延时时间:"+delayTimeCache.getDelayTime(dataEntity.getUniqueMark())
													+",剩余延时时间:"+(delayTimeCache.getDelayTime(dataEntity.getUniqueMark())-dataEntity.getGpsDate())+",延时设置到期，清除延时缓存");
											dataEntity.addParkingAddition(rule.getAreaId(), areaEntity.getAreaType());

											delayTimeCache.delDelayTimeCache(dataEntity.getUniqueMark());
										}else{
											log.info("终端【"+dataEntity.getTerminalId()+"】服务站【区域ID："+rule.getAreaId()+"】滞留超时延时处理，当前GPS时间："
													+dataEntity.getGpsDate()+" 延时时间:"+delayTimeCache.getDelayTime(dataEntity.getUniqueMark())
													+",剩余延时时间:"+(delayTimeCache.getDelayTime(dataEntity.getUniqueMark())-dataEntity.getGpsDate())+"秒");
											//如果当前时间大于延时终止时间戳，则清除该缓存。
//											DelayTimeCache.getInstance().delDelayTimeCache(dataEntity.getUniqueMark());
										}
									}else{
										log.info("终端【"+dataEntity.getTerminalId()+"】服务站【区域ID："+rule.getAreaId()+"】滞留超时报警");
										dataEntity.addParkingAddition(rule.getAreaId(), areaEntity.getAreaType());
									}
								}
							}
						}else{
							/*if(rule_170090_Status.getStaytimeParkTime(statusStr)==null){
								//车辆没有进入该服务站，不做处理
							}else{
								//存在上次记录的缓存，说明车辆刚出服务站，处理：需要删除缓存信息
								log.error(statusStr+","+dataEntity.getGpsDate()+"出服务站，测试半径："+areaEntity.getDatas().get(0).getRadiusLength()+",距离："+distance);
								rule_170090_Status.delStaytimeParkCache(statusStr);
							}*/

							//此时在服务站外部，需要判断缓存保存的上一个状态
							if(rule_170090_Status.getOvertimeParkTime(statusStr)==null){
								//车辆没有进入该服务站，不做处理
							}else{
								//存在上次记录的缓存，说明车辆刚出服务站，处理：需要删除缓存信息
								rule_170090_Status.delOvertimeParkCache(statusStr);
							}
						}
					}
				}
			}
		}
		return dataEntity;
	}
}
