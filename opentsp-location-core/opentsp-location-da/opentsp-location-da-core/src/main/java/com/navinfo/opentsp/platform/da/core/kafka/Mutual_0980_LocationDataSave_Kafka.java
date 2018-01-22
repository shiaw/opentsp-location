package com.navinfo.opentsp.platform.da.core.kafka;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.platform.da.core.persistence.TermianlDynamicManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.TermianlDynamicManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisClusters;
import com.navinfo.opentsp.platform.da.core.startup.DARun;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCLocationDataSave.LocationDataSave;
import com.navinfo.opentspcore.common.handler.AbstractKafkaCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;

/**
 * 位置数据处理
 */
//@KafkaConsumerHandler(topic = "daposdone",commandId = "0980")
//@Component
public class Mutual_0980_LocationDataSave_Kafka extends AbstractKafkaCommandHandler {

	final static TermianlDynamicManage terminalDynamicManage = new TermianlDynamicManageImpl();

	private static final Logger log = LoggerFactory.getLogger(Mutual_0980_LocationDataSave_Kafka.class);

	protected Mutual_0980_LocationDataSave_Kafka() {
		super(KafkaCommand.class);
	}

	private long timecahe = 0;

	@Value("${opentsp.monitor.timeOut:60000}")
	private long timeOut;

	@Override
	public void handle(KafkaCommand kafkaCommand) {
//		Jedis jedis = null;
//		boolean isBroken = false;
//		try {
//			Packet packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(),Packet.class);
//			log.info("DP --> DA SUCESS!{}", packet.toString());
//			LocationDataSave locationDataSave = LocationDataSave.parseFrom(packet.getContent());
//			terminalDynamicManage.saveGpsData(locationDataSave.getTerminalId(),locationDataSave.getLocationData());
//			if(timecahe == 0)
//			{
//				jedis = RedisClusters.getInstance().getJedis();
//				timecahe = System.currentTimeMillis();
//				// redis.set(dahost,timeche)
//				log.info("~~~~~~末次位置首次将daHost与时间存入redis~~~~~");
//				jedis.set(DARun.localhost,String.valueOf(timecahe));
//				log.info("~~~~~~redis存储结束~~~~~");
//			} else {
//				long time = System.currentTimeMillis();
//				if(time - timecahe > timeOut) {
//					timecahe = time;
//					jedis = RedisClusters.getInstance().getJedis();
//					// redis.set(dahost,timeche)
//					log.info("~~~~~~末次位置daHost与时间存入redis~~~~~");
//					jedis.set(DARun.localhost,String.valueOf(timecahe));
//					log.info("~~~~~~redis存储结束~~~~~");
//				}
//			}
//		} catch (IOException e) {
//			isBroken = true;
//			log.error(e.getMessage(), e);
//		} finally {
//			RedisClusters.getInstance().release(jedis, isBroken);
//		}

	}

}