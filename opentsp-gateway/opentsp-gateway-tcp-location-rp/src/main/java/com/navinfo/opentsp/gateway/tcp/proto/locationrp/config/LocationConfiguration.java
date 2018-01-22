package com.navinfo.opentsp.gateway.tcp.proto.locationrp.config;

import com.navinfo.opentsp.gateway.tcp.proto.ProtocolConfiguration;
import org.springframework.context.annotation.Import;

/**
 * It configuration must compose all basic components and implementations into usable protocol.
 */
@Import({LocationProtoConfiguration.class, MasterMMConfiguration.class, RPConfiguration.class})
@ProtocolConfiguration
public class LocationConfiguration {

}
