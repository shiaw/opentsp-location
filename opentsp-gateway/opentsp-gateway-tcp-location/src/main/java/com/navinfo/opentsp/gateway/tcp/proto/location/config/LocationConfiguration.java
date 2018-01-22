package com.navinfo.opentsp.gateway.tcp.proto.location.config;

import com.navinfo.opentsp.gateway.tcp.proto.ProtocolConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * It configuration must compose all basic components and implementations into usable protocol.
 */
@Import(LocationProtoConfiguration.class)
@ProtocolConfiguration
public class LocationConfiguration {

}
