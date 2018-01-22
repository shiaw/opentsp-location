package com.navinfo.opentsp.gateway.web.config;

import com.navinfo.opentsp.gateway.UserAuthenticationManager;
import com.navinfo.opentsp.gateway.stub.StubUserDetailService;
import com.navinfo.opentsp.gateway.web.controller.CommandController;
import com.navinfo.opentsp.gateway.web.controller.RestErrorHandler;
import com.navinfo.opentsp.gateway.web.limiting.LimitingServiceFetcher;
import com.navinfo.opentsp.gateway.web.oauth.OauthAuthenticationToken;
import com.navinfo.opentsp.gateway.web.oauth.OauthAuthenticator;
import com.navinfo.opentsp.gateway.web.provider.PluggableAuthenticationProvider;
import com.navinfo.opentsp.platform.configuration.CacheConfiguration;
import com.navinfo.opentsp.platform.configuration.EurekaClient;
import com.navinfo.opentspcore.common.validator.ValidatorConfiguration;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Configuration
@ComponentScan(basePackageClasses = {CommandController.class, OauthAuthenticator.class, UserAuthenticationManager.class,
        StubUserDetailService.class, RestErrorHandler.class})
@Import({SecurityConfig.class, WebConfig.class, ValidatorConfiguration.class, CacheConfiguration.class, EurekaClient.class})
public class GatewayConfig {

    /* we need to explicitly declare EurekaClient dependency, elsewhere
     * spring ignore it import when instantiate this config for tomcat context (it possibly bug)
     */
    @Autowired(required = false)
    EurekaClient eurekaClient;

    @Bean
    GlobalAuthenticationConfigurerAdapter globalAuthenticationConfigurerAdapter(@Qualifier("oauthAuthenticationProvider") final
                                                                                ObjectFactory<AuthenticationProvider> authenticationProvider) {
        return new GlobalAuthenticationConfigurerAdapter() {
            @Override
            public void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.authenticationProvider(authenticationProvider.getObject());
            }

            @Override
            public void init(AuthenticationManagerBuilder auth) throws Exception {

            }
        };
    }

    @Bean
    AuthenticationProvider oauthAuthenticationProvider(OauthAuthenticator oauthAuthenticator, UserDetailsService userDetailsService) {
        return new PluggableAuthenticationProvider(new PluggableAuthenticationProvider.Config().addSupportedAuthentication(OauthAuthenticationToken.class),
          oauthAuthenticator, userDetailsService);
    }

    /**
     * URL of ip-limiting service
     */
    @Value("${ip.limiting.url:}")
    private String ipLimitingUrl;

    /**
     * Keys which will be fetched from headers and passed to ip-limiting service as query parameters
     */
    @Value("${ip.limiting.keys:ak,am,sdkId}")
    private String[] ipLimitingKeys;

    /**
     * Regex for allowing access by user-agents HttpHeader w/o ip-limiting checking
     */
    @Value("${ip.limiting.user.agent.skipped.pattern:}")
    private String userAgentSkippedPattern;
    /**
     * ip-limiting service timeout
     */
    @Value("${ip.limiting.server.timeout:2000}")
    private Integer ipLimitingServerTimeOut;

    /**
     * Name of header property with client IP address
     * @return
     */
    @Value("${client.ip.address:x-forwarded-for}")
    private String clientIpAddress;


    /**
     * Patterns for skipping ip-limiting
     * @return
     */
    @Value("${ip.limiting.skipped.patterns:}")
    private String[] skippedIpLimitingPatterns;

    @Bean
    LimitingServiceFetcher limitingServiceFetcher() {
        return LimitingServiceFetcher.newBuilder()
                .clientIpAddress(clientIpAddress)
                .paramKeys(Arrays.asList(ipLimitingKeys))
                .skippedIpLimitingPatterns(skippedIpLimitingPatterns)
                .restTemplate(restTemplate(ipLimitingServerTimeOut))
                .url(ipLimitingUrl)
                .userAgentSkippedPattern(userAgentSkippedPattern)
                .build();
    }

    public RestTemplate restTemplate(Integer ipLimitingServerTimeOut) {
        return new RestTemplate(clientHttpRequestFactory(ipLimitingServerTimeOut));
    }

    private ClientHttpRequestFactory clientHttpRequestFactory(Integer ipLimitingServerTimeOut) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(ipLimitingServerTimeOut);
        factory.setConnectTimeout(ipLimitingServerTimeOut);
        return factory;
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainerFactory() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                Http11NioProtocol protocolHandler = (Http11NioProtocol) connector.getProtocolHandler();
                protocolHandler.setThreadPriority(8);
                protocolHandler.setAcceptorThreadPriority(10);
                protocolHandler.setPollerThreadCount(3);
                protocolHandler.setProperty("socket.performanceBandwidth","2");
                protocolHandler.setProperty("socket.performanceLatency","1");
                protocolHandler.setProperty("socket.unlockTimeout","0");
            }
        });
        // configure some more properties
        return factory;
    }

}
