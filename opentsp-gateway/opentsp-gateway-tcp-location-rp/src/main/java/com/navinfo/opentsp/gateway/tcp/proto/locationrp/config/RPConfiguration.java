package com.navinfo.opentsp.gateway.tcp.proto.locationrp.config;

import com.navinfo.opentsp.gateway.api.ConnectionCloseCallback;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.netty.LocationChannelInitializer;
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
 * User: zhanhk
 * Date: 16/10/9
 * Time: 下午4:59
 */
public class RPConfiguration {

    @Value("${opentsp.server.rp.master.ip.re}")
    private String locationTcpHost;

    @Value("${opentsp.server.rp.master.port.re}")
    private int locationTcpPort;

    /**
     * Server for Car Tbox Interface Protocol
     *
     * @return
     */
    @Bean(name = "RPServer")
    public TspTcpServer locationServer(LocationChannelInitializer channelInitializer) {
        return new TspTcpServer(locationTcpHost, locationTcpPort, channelInitializer);
    }

    @Bean
    public NettyClientConnections locationConnections(ConnectionCloseCallback eventSourceCallback) {
        NettyClientConnections.Builder builder = NettyClientConnections.builder();
        builder.setName("rp");
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
