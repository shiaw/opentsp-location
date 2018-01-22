package com.navinfo.opentsp.gateway.web.proxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ProxyCommandResolverTest.TestConfig.class)
@TestPropertySource(properties = {
        "/uri_mapping=http://www.google.com/",
        "balanced.service_id.test=/id_mapping"})
public class ProxyCommandResolverTest {

    @Autowired
    private ProxyCommandResolver proxyCommandMapper;

    @Test
    public void getRemoteUrl() throws Exception {
        String google = proxyCommandMapper.getRemoteUrlByUriPattern("/uri_mapping");
        assertEquals("http://www.google.com/", google);
    }

    @Test
    public void getServiceID() throws Exception {
        String serviceId = proxyCommandMapper.getServiceIDByUriPattern("/id_mapping");
        assertEquals("test", serviceId);
    }

    public static class TestConfig {

        @Autowired
        private Environment env;

        @Bean
        public ProxyCommandResolver proxyCommandResolver() {
            return new ProxyCommandResolver(env);
        }

    }

}