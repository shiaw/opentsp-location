package com.navinfo.opentsp.platform.rprest.kafka;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.platform.rprest.scheduler.RpRestScheduler;
import com.navinfo.opentspcore.common.handler.AbstractKafkaCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 位置数据处理
 * @author wanliang
 */
@KafkaConsumerHandler(topic = "posraw",commandId = "3002")
@Component
public class LocationReport3002Handler extends AbstractKafkaCommandHandler {

	private static final Logger log = LoggerFactory.getLogger(LocationReport3002Handler.class);

	protected LocationReport3002Handler() {
		super(KafkaCommand.class);
	}
	@Override
	public void handle(KafkaCommand kafkaCommand) {
		   RpRestScheduler.packetCount++;
		// log.info("当前缓存包数：{}",PacketCountScheduler.packetCount);
	}


}
