package com.navinfo.opentsp.platform.dp.core.acceptor.ta.receiver;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.platform.dp.core.cache.DPMonitorCache;
import com.navinfo.opentsp.platform.dp.core.cache.DPMonitorCacheHolder;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.rule.RegularHandler;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentspcore.common.handler.AbstractKafkaCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * 位置数据处理
 * @author zhanhk
 */
@KafkaConsumerHandler(topic = "posraw",commandId = "3002")
@Component
public class TA_3002_LocationReport extends AbstractKafkaCommandHandler {

	private static final Logger log = LoggerFactory.getLogger(TA_3002_LocationReport.class);

	protected TA_3002_LocationReport() {
		super(KafkaCommand.class);
	}

	@Autowired
	public RegularHandler regularHandler;

	private DPMonitorCache dpMonitorCache = new DPMonitorCache();

	public DPMonitorCache getDpMonitorCache() {
		return dpMonitorCache;
	}

	public void setDpMonitorCache(DPMonitorCache dpMonitorCache) {
		this.dpMonitorCache = dpMonitorCache;
	}

	@Override
	public void handle(KafkaCommand kafkaCommand) {
		Packet packet;
		try {
			packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(), Packet.class);
//			log.info("TA --> DP SUCCESS!{}", packet.toString());
			LCLocationData.LocationData locationData = LCLocationData.LocationData.parseFrom(packet.getContent());
			GpsLocationDataEntity dataEntity = new GpsLocationDataEntity(locationData, packet.getUniqueMark(), packet.getSerialNumber(), AllCommands.Terminal.ReportLocationData_VALUE);
//
			String threadName = Thread.currentThread().getName();
			dpMonitorCache.setName(threadName);
			//ThreadLocal缓存对应监控统计对象
			DPMonitorCacheHolder.setDPMonitorCache(dpMonitorCache);

			try {
				dpMonitorCache.addDPCount();
				long s = System.currentTimeMillis();
				//进入规则运算
				regularHandler.handler(dataEntity);
				long e = System.currentTimeMillis() - s;
				//记录统计信息
				if (!dataEntity.getDetailMap().isEmpty()) {
					dpMonitorCache.setMaxTime(e, dataEntity.getDetailMap(),
							dataEntity.getProcessFilterTime(), dataEntity.getRuleProcessTime(),
							dataEntity.getRuleSignTime(), dataEntity.getEncryptTime(),
							dataEntity.getForwardTime());
					dpMonitorCache.setTid(dataEntity.getTerminalId());
				}
//				log.info("{}-{}-{},规则处理总时间: {} ms",threadName, dataEntity.getUniqueMark(),dataEntity.getGpsDate(),e);
			} catch (Exception e) {
				dpMonitorCache.decDPCount();
				log.error("DP 3002规则链处理错误,{}", e);
			}
		} catch (Exception e) {
			log.error("数据格式错误!", e);
		}
	}

}
