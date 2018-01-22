//package com.navinfo.opentsp.platform.da.core.configuration;
//
//import com.navinfo.opentsp.platform.da.core.webService.service.impl.center.*;
//import org.apache.cxf.Bus;
//import org.apache.cxf.jaxws.EndpointImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.xml.ws.Endpoint;
//
//@Configuration
//public class WebServiceConfig {
//
//    @Autowired
//    private Bus bus;
//
//    @Bean
//    public Endpoint configWSEndpoint() {
//        EndpointImpl endpoint = new EndpointImpl(bus, new CenterConfigWebServiceImpl());
//        endpoint.publish("/ConfigWS");
//        return endpoint;
//    }
//
//    @Bean
//    public Endpoint dictWSEndpoint() {
//        EndpointImpl endpoint = new EndpointImpl(bus, new CenterDictWebServiceImpl());
//        endpoint.publish("/DictWS");
//        return endpoint;
//    }
//
//    @Bean
//    public Endpoint logWSEndpoint() {
//        EndpointImpl endpoint = new EndpointImpl(bus, new CenterLogWebServiceImpl());
//        endpoint.publish("/LogWS");
//        return endpoint;
//    }
//
//    @Bean
//    public Endpoint terminalWSEndpoint() {
//        EndpointImpl endpoint = new EndpointImpl(bus, new CenterTerminalWebServiceImpl());
//        endpoint.publish("/TerminalWS");
//        return endpoint;
//    }
//    @Bean
//    public Endpoint userWSEndpoint() {
//        EndpointImpl endpoint = new EndpointImpl(bus, new CenterUserWebServiceImpl());
//        endpoint.publish("/userWS");
//        return endpoint;
//    }
//}
