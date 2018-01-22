package com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.config;

import com.navinfo.opentsp.gateway.tcp.proto.ProtocolConfiguration;
import org.springframework.context.annotation.Import;

/**
 * It configuration must compose all basic components and implementations into usable protocol.
 */
@Import({LocationProtoConfiguration.class, Location808ProtoConfiguration.class})
@ProtocolConfiguration
public class LocationConfiguration {

}
