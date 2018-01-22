package com.navinfo.opentsp.gateway.tcp.proto.location.config;

import com.navinfo.opentsp.gateway.tcp.proto.location.netty.ClientDispatchTcpHandler;
import com.navinfo.opentsp.gateway.tcp.proto.location.netty.ClientInitializer;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.TcpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wanliang on 2017/03/27.
 */
@Configuration
public class TAClientConfiguration {

    @Value("${forword.808.server.ta.address:0.0.0.0}")
    private String dpTcpHost;

    @Value("${forword.808.server.ta.port:0}")
    private int dpTcpPort;

    @Bean
    public TcpClient taClient(ClientInitializer taClientInitializer) {
        TcpClient tcpClient = new TcpClient(dpTcpHost, dpTcpPort, taClientInitializer);
        tcpClient.start();

        ClientDispatchTcpHandler clientDispatchTcpHandler = taClientInitializer.getClientDispatchTcpHandler();
        if(clientDispatchTcpHandler != null){
            clientDispatchTcpHandler.setTcpClient(tcpClient);
        }

        return tcpClient;
    }
}
