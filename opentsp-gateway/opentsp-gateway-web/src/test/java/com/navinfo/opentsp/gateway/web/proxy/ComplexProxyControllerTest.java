package com.navinfo.opentsp.gateway.web;

import com.jayway.restassured.RestAssured;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.routing.annotation.AnnotationMessageRouter;
import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.CommandClassResolver;
import com.navinfo.opentsp.gateway.balancer.web.proxy.common.HttpProxy;
import com.navinfo.opentsp.gateway.balancer.web.proxy.common.HttpProxyContext;
import com.navinfo.opentsp.gateway.stub.StubUserDetailService;
import com.navinfo.opentsp.gateway.web.auth.AuthenticationDetailsSourceImpl;
import com.navinfo.opentsp.gateway.web.config.SecurityConfig;
import com.navinfo.opentsp.gateway.web.config.WebConfig;
import com.navinfo.opentsp.gateway.web.controller.CommandController;
import com.navinfo.opentsp.gateway.web.controller.ControllerSuccessor;
import com.navinfo.opentsp.gateway.web.proxy.ProxyCommandResolver;
import com.navinfo.opentsp.gateway.web.proxy.balanced.BalancedProxyController;
import com.navinfo.opentsp.gateway.web.proxy.plain.ProxyController;
import com.navinfo.opentsp.gateway.web.utils.CommandResolver;
import com.navinfo.opentspcore.common.log.DiagnosticInfo;
import com.navinfo.opentspcore.common.log.RandomUUIDGenerator;
import com.navinfo.opentspcore.common.log.UUIDGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.given;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles({"no-auth", "security-stub"})
//@IntegrationTest("server.port:0")
@TestPropertySource(properties = {
        "url.permit.all=/**",
        "/uri_mapping=http://www.google.com/",
        "balanced.service_id.test=/id_mapping",
        "file.storage.service.url=id-test"})
public class ComplexProxyControllerTest {

    @Autowired
    private HttpProxy httpProxy;

    @Value("${local.server.port}")
    int port;

    @Before
    public void setup() throws Exception {
        initMocks(this);
        RestAssured.port = port;
    }

    @Test
    public void testProxy() throws Exception {
        given().when().
                get("/uri_mapping").
                then().
                statusCode(200);

        ArgumentCaptor<HttpProxyContext> httpProxyContextArgumentCaptor = ArgumentCaptor.forClass(HttpProxyContext.class);
        verify(httpProxy, times(1)).service(
                httpProxyContextArgumentCaptor.capture()
        );

        HttpProxyContext capture = httpProxyContextArgumentCaptor.getValue();
        Assert.assertEquals("http://www.google.com/", capture.getTarget().toString());
        Mockito.reset(httpProxy);
    }

    @Test
    public void testBalancedProxy() throws Exception {
        given().when().
                get("/id_mapping").
                then().
                statusCode(200);

        ArgumentCaptor<HttpProxyContext> httpProxyContextArgumentCaptor = ArgumentCaptor.forClass(HttpProxyContext.class);
        verify(httpProxy, times(1)).service(
                httpProxyContextArgumentCaptor.capture()
        );

        HttpProxyContext capture = httpProxyContextArgumentCaptor.getValue();
        // see ServiceDiscoveryStubConfiguration
        Assert.assertEquals("http://id-test:8084/id_mapping", capture.getTarget().toString());
        Mockito.reset(httpProxy);
    }

    @Configuration
    @EnableAutoConfiguration
    @ComponentScan(basePackageClasses = {CommandResolver.class,
            StubUserDetailService.class, AnnotationMessageRouter.class, StubUserDetailService.class})
    @Import({WebConfig.class, SecurityConfig.class})
    public static class TestConfig {

        @Bean
        @Autowired
        ProxyCommandResolver proxyCommandResolver(Environment env) {
            return new ProxyCommandResolver(env);
        }

        @Bean
        CommandController commandController() {
            return new CommandController();
        }

        @Bean
        ProxyController proxyController() {
            return new ProxyController();
        }

        @Bean
        BalancedProxyController balancedProxyController() {
            return new BalancedProxyController();
        }

        @Bean
        MessageChannel messageChannel() {
            return mock(MessageChannel.class);
        }

        @Bean
        DiagnosticInfo diagnosticInfo() {
            return new DiagnosticInfo();
        }

        @Bean
        UUIDGenerator uuidGenerator() {
            return new RandomUUIDGenerator();
        }

        @Bean
        ControllerSuccessor limitingServiceFetcher() {
            return Mockito.mock(ControllerSuccessor.class);
        }

        @Bean
        HttpProxy httpProxy() {
            return Mockito.mock(HttpProxy.class);
        }

        @Bean
        CommandClassResolver commandClassResolver() {
            return new CommandClassResolver();
        }

        @Bean
        AuthenticationDetailsSourceImpl authenticationDetailsSourceImpl() {
            return new AuthenticationDetailsSourceImpl(mock(Queue.class));
        }

    }

}
