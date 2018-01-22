package com.navinfo.opentsp.platform.dp.core.rule.handler.single;

import com.navinfo.opentsp.platform.dp.core.cache.RuleCache;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.rule.process.single.*;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

@Component
public class SingleRegularHandler {

	@Resource
	private RuleCache ruleCache;

	private static final Logger log = LoggerFactory.getLogger(SingleRegularHandler.class);
	int corePoolSize = 0;
	int maximumPoolSize = 0;
	int keepAliveTime = 1;
	TimeUnit unit = TimeUnit.SECONDS;
	BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(100);
	ThreadFactory threadFactory = new ThreadFactory() {

		@Override
		public Thread newThread(Runnable r) {
			return null;
		}
	};

	static ExecutorService executorServicePool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
	
	static Map<Integer, SingleRegularProcess> rulePools = new ConcurrentHashMap<Integer, SingleRegularProcess>();

	static {
		rulePools.put(RegularCode.speeding_VALUE, new Rule_170010_SpeedingProcess());
		rulePools.put(RegularCode.inOutArea_VALUE, new Rule_170020_InOutAreaProcess());
		rulePools.put(RegularCode.routeDriverTime_VALUE, new Rule_170030_RouteDriverTimeProcess());
		rulePools.put(RegularCode.driverNotCard_VALUE, new Rule_170040_DriverNotCardProcess());
		rulePools.put(RegularCode.doorOpenOutArea_VALUE, new Rule_170050_DoorOpenOutAreaProcess());
		rulePools.put(RegularCode.drivingBan_VALUE, new Rule_170060_DrivingBanProcess());
		rulePools.put(RegularCode.keyPointFence_VALUE, new Rule_170070_KeyPointFenceProcess());
		rulePools.put(RegularCode.outregionToLSpeed_VALUE, new Rule_170100_OutRegionToLSpeed());	
		rulePools.put(RegularCode.inAreaTriggerActivationOrLockNotify_VALUE, new Rule_170160_InAreaTriggerActivationOrLockProcess());
	}

	// 下发终端的终端运算规则
	static Set<Integer> terminalRules = new HashSet<Integer>();
	static {
		terminalRules.add(RegularCode.speeding_VALUE);
		terminalRules.add(RegularCode.inOutArea_VALUE);
		terminalRules.add(RegularCode.routeDriverTime_VALUE);
		terminalRules.add(RegularCode.outregionToLSpeed_VALUE);
		terminalRules.add(RegularCode.inAreaTriggerActivationOrLockNotify_VALUE);
	}

	

	public GpsLocationDataEntity process(final GpsLocationDataEntity dataEntity) {
		final RuleEntity ruleEntity = ruleCache.getRuleEntity(dataEntity.getTerminalId());
		if(ruleEntity != null){
			ruleEntityCal(dataEntity,ruleEntity);//个性规则处理
		}
		return dataEntity;
	}
	
	/**
	 * 个性规则处理
	 * @param dataEntity
	 * @param ruleEntity
	 */
	private void ruleEntityCal(final GpsLocationDataEntity dataEntity, final RuleEntity ruleEntity){
		long s = System.currentTimeMillis();
		// 此终端是否需要进行规则运算
		if (ruleEntity == null) {
			return ;
		}
		final List<Integer> _rule_array = ruleEntity.getRuleArray();
		if (_rule_array != null) {
			for (Integer ruleId : _rule_array) {
				//TerminalEntity terminalEntity = TerminalCache.getInstance().getTerminal(dataEntity.getTerminalId());
				//if (!terminalEntity.isRegularInTerminal()) {//在平台运算规则
//					rulePools.get(ruleId).process(ruleEntity, dataEntity);
					SingleRegularProcess process = rulePools.get(ruleId);
					if(process != null) {
						process.process(ruleEntity, dataEntity);
						long e = System.currentTimeMillis() - s;
						dataEntity.getDetailMap().put(ruleId+"",e);
//						log.info(dataEntity.getUniqueMark()+",通用规则ID-->"+ruleId+",处理时间:"+ e +"ms");
					}
				//}
			}
			
			/*executorServicePool.execute(new Runnable() {
				@Override
				public void run() {
					// 规则运算
					for (Integer ruleId : _rule_array) {
						TerminalEntity terminalEntity = TerminalCache.getInstance().getTerminal(dataEntity.getTerminalId());
						if (!terminalEntity.isRegularInTerminal()) {//在平台运算规则
							rulePools.get(ruleId).process(ruleEntity, dataEntity);
						}
					}
				}
			});*/
		}
	}
}
