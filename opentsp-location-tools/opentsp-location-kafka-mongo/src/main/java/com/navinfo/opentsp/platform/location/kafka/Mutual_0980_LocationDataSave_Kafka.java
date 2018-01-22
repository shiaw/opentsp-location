package com.navinfo.opentsp.platform.location.kafka;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCLocationDataSave.LocationDataSave;
import com.navinfo.opentsp.platform.location.service.LocationDataService;
import com.navinfo.opentspcore.common.handler.AbstractKafkaCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 位置数据处理
 */
@KafkaConsumerHandler(topic = "daposdone",commandId = "0980")
@Component
public class Mutual_0980_LocationDataSave_Kafka extends AbstractKafkaCommandHandler {

	@Autowired
	private LocationDataService locationDataService;

	@Value("${opentsp.location.save.date:0}")
	private String  locationSaveDate;

	private static final Logger log = LoggerFactory.getLogger(Mutual_0980_LocationDataSave_Kafka.class);

	protected Mutual_0980_LocationDataSave_Kafka() {
		super(KafkaCommand.class);
	}

	@Override
	public void handle(KafkaCommand kafkaCommand) {

		try {
			Packet packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(),Packet.class);
			LocationDataSave locationDataSave = LocationDataSave.parseFrom(packet.getContent());
		    String dateStr=DateUtils.format(locationDataSave.getLocationData().getGpsDate(),
					DateUtils.DateFormat.YYYYMMDD);
			String dateTimeStr=DateUtils.format(locationDataSave.getLocationData().getGpsDate(),
					DateUtils.DateFormat.YY_YY_MM_DD_HH_MM_SS);
		    //判断GPS时间是否为当天的数据
			if(!locationSaveDate.equals(dateStr)){
				log.info("no save data ,tid:{},date:{},gpsDate:{}",packet.getUniqueMark(),dateStr,dateTimeStr);
                return;
			}
			//terminalDynamicManage.saveGpsData(locationDataSave.getTerminalId(),locationDataSave.getLocationData());
			locationDataService.saveGpsData(packet);
			log.info("save gps data ,tid:{},date:{}",packet.getUniqueMark(),dateStr);
		} catch (IOException e) {
			log.error("数据格式错误!",e);
		}

	}

}
