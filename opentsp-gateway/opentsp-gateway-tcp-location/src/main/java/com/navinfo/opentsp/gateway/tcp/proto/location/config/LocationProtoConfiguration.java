package com.navinfo.opentsp.gateway.tcp.proto.location.config;

import com.navinfo.opentsp.gateway.api.ConnectionCloseCallback;
import com.navinfo.opentsp.gateway.tcp.proto.location.netty.LocationChannelInitializer;
import com.navinfo.opentsp.gateway.tcp.server.ConnectionIdGenerator;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnections;
import com.navinfo.opentsp.gateway.tcp.server.TspTcpServer;
import com.navinfo.opentspcore.common.gateway.ConnectionInfoImpl;
import com.navinfo.opentspcore.common.gateway.TextMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

/**
 * Basic spring config for CTIP.
 * This configuration must configure all common things which is needed for prodiuction and test usage, implementation
 * of handlers and production specific data must be in
 */
public class LocationProtoConfiguration {

    @Value("${opentsp.server.locationtcp.address}")
    private String locationTcpHost;

    @Value("${opentsp.server.locationtcp.port}")
    private int locationTcpPort;

    /**
     * Server for Car Tbox Interface Protocol
     *
     * @return
     */
    @Bean
    public TspTcpServer locationServer(LocationChannelInitializer channelInitializer) {
        return new TspTcpServer(locationTcpHost, locationTcpPort, channelInitializer);
    }

    @Bean
    public NettyClientConnections locationConnections(ConnectionCloseCallback eventSourceCallback) {
        NettyClientConnections.Builder builder = NettyClientConnections.builder();
        builder.setName("location");
        builder.setConnectionInfo(ConnectionInfoImpl.builder()
                .addSupportedMessage(TextMessage.class)
                .build());
        builder.setIdGenerator(new ConnectionIdGenerator() {
            @Override
            public String getId(NettyClientConnection connection) {
                return UUID.randomUUID().toString();
            }
        });
        builder.closeCallback(eventSourceCallback);
        return builder.build();
    }

}
