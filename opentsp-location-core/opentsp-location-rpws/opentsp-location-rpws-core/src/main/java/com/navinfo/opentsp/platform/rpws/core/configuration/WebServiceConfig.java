package com.navinfo.opentsp.platform.rpws.core.configuration;

import com.lc.rp.webService.manager.DateAnalysisQueryInterceptor;
import com.lc.rp.webService.manager.TerminalTrackInterceptor;
import com.lc.rp.webService.service.BasicDataQueryWebService;
import com.lc.rp.webService.service.DataAnalysisWebService;
import com.lc.rp.webService.service.SynchronousTerminalDataWebService;
import com.lc.rp.webService.service.impl.center.SynchronousTerminalDataWebServiceImpl;
import com.lc.rp.webService.service.impl.district.BasicDataQueryWebServiceImpl;
import com.lc.rp.webService.service.impl.district.DataAnalysisWebServiceImpl;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServiceConfig {

    @Value("${opentsp.webservice.hostname}")
    private String hostName;

    @Value("${opentsp.webservice.port}")
    private int port;

    @Bean(name = "DAnalysisWS")
    public JaxWsServerFactoryBean dAnalysisWSEndpoint() {
        JaxWsServerFactoryBean config_DataAnalysisfactory = new JaxWsServerFactoryBean();
        DataAnalysisWebService analysisWS = new DataAnalysisWebServiceImpl();
        config_DataAnalysisfactory.setServiceClass(DataAnalysisWebService.class);
        config_DataAnalysisfactory.setAddress("http://" + hostName + ":" + port + "/DAnalysisWS");
        config_DataAnalysisfactory.setServiceBean(analysisWS);
        config_DataAnalysisfactory.getInInterceptors().add(new DateAnalysisQueryInterceptor());
        config_DataAnalysisfactory.create();
        return config_DataAnalysisfactory;
    }

    @Bean(name = "BasicDQWS")
    public JaxWsServerFactoryBean basicDQWSEndpoint() {
        JaxWsServerFactoryBean config_BasicDataQueryfactory = new JaxWsServerFactoryBean();
        BasicDataQueryWebService basicDataQuery = new BasicDataQueryWebServiceImpl();
        config_BasicDataQueryfactory.setServiceClass(BasicDataQueryWebService.class);
        config_BasicDataQueryfactory.setAddress("http://" + hostName + ":" + port + "/BasicDQWS");
        config_BasicDataQueryfactory.setServiceBean(basicDataQuery);
        config_BasicDataQueryfactory.getInInterceptors().add(new TerminalTrackInterceptor());
        config_BasicDataQueryfactory.create();
        return config_BasicDataQueryfactory;
    }

    @Bean(name = "TerminalWS")
    public JaxWsServerFactoryBean terminalWSEndpoint() {
        JaxWsServerFactoryBean config_TerminaInfoQueryfactory = new JaxWsServerFactoryBean();
        SynchronousTerminalDataWebService synchronousTerminalDataWebService = new SynchronousTerminalDataWebServiceImpl();
        config_TerminaInfoQueryfactory.setServiceClass(SynchronousTerminalDataWebServiceImpl.class);
        config_TerminaInfoQueryfactory.setAddress("http://" + hostName + ":" + port + "/TerminalWS");
        config_TerminaInfoQueryfactory.setServiceBean(synchronousTerminalDataWebService);
        config_TerminaInfoQueryfactory.create();
        return config_TerminaInfoQueryfactory;
    }
}
