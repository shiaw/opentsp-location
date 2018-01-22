package com.navinfo.opentsp.gateway.web.limiting;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.navinfo.opentsp.common.messaging.Command;
import com.navinfo.opentsp.common.messaging.ProxiedCommand;
import com.navinfo.opentsp.gateway.web.config.GatewayConfig;
import com.navinfo.opentsp.users.commands.GetUsersCommand;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class LimitingServiceFetcherTest {

    private final static int EXISTING_PORT = 8888;
    private final static int FAKE_PORT = 8887;

    //The @Rule 'wireMockRule' must be public
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(EXISTING_PORT));
    private GatewayConfig gatewayConfig = new GatewayConfig();

    /**
     * positive test
     */
    @Test
    public void testProcess() {
        buildIpLimitingTRUEResponse(0);
        HttpServletRequest request = prepareRequest();

        LimitingServiceFetcher limitingServiceFetcher = createService(EXISTING_PORT);
        limitingServiceFetcher.process(null, request);

    }

    /**
     * positive test
     * remote service isn't accessible
     */
    @Test
    public void testFailToConnect() {
        HttpServletRequest request = prepareRequest();
        LimitingServiceFetcher limitingServiceFetcher = createService(FAKE_PORT);
        limitingServiceFetcher.process(new ProxiedCommand<Command.Result>() {
            @Override
            public Class<? extends Result> getResultType() {
                return null;
            }
        }, request);
    }

    /**
     * positive test
     * timeOut: serverDealy: 25000, wait time 2000
     */
    @Test
    public void testTimeOutToConnect() {
        buildIpLimitingTRUEResponse(25000);
        HttpServletRequest request = prepareRequest();
        LimitingServiceFetcher limitingServiceFetcher = createService(EXISTING_PORT);
        limitingServiceFetcher.process(null, request);
    }

    /**
     * negative test
     * remote service returns false, browser will show 400 error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testResponseFalse() {
        buildIpLimitingFALSEResponse();
        HttpServletRequest request = prepareRequest();
        LimitingServiceFetcher limitingServiceFetcher = createService(EXISTING_PORT);
        limitingServiceFetcher.process(null, request);

    }

    /**
     * positive test
     * commands isn't a proxy
     */
    @Test
    public void testNotProxyCommand() {
        buildIpLimitingFALSEResponse();
        HttpServletRequest request = prepareRequest();
        LimitingServiceFetcher limitingServiceFetcher = createService(EXISTING_PORT);
        limitingServiceFetcher.process(new GetUsersCommand(), request);
    }

    /**
     * test normalizing of query string
     */
    @Test
    public void testNormalizedQueryString() {
        LimitingServiceFetcher limitingServiceFetcher = createService(EXISTING_PORT);
        Map<String, String> map = limitingServiceFetcher.prepareQueryStringMap("key1=value1&key2=value2&key3=kkk");
        MockHttpServletRequest request = new MockHttpServletRequest();
        limitingServiceFetcher.normalizeQueryString(map, request);
        Object queryString = request.getAttribute("queryString");
        Assert.assertNotNull(queryString);
        Assert.assertEquals("key3=kkk", queryString);

    }

    /**
     * Test extracting values from header
     */
    @Test
    public void headersExtractTest() throws UnsupportedEncodingException {
        LimitingServiceFetcher limitingServiceFetcher = createService(EXISTING_PORT);
        //Prepare request
        MockHttpServletRequest request = new MockHttpServletRequest();
        // put information about user agent with some non URI like value
        String key = "User-Agent";
        String value = "sfsf sdffsd {} [] ";
        request.addHeader(key, value);

        //Create URL
        String ua = limitingServiceFetcher.buildUrl(request);
        Assert.assertNotNull(ua);
        //parse it for checking params
        Map<String, String> map = limitingServiceFetcher.prepareQueryStringMap(ua);
        //test that param was encoded
        Assert.assertEquals(UriUtils.encodeQueryParam(value, "UTF-8"), map.get(key));

    }

    /**
     * positive test
     * test path matching (pattern matches with path)
     */
    @Test
    public void testSkippedIpLimitingPathes() {
        buildIpLimitingFALSEResponse();
        HttpServletRequest request = prepareRequest();
        LimitingServiceFetcher limitingServiceFetcher = createService(EXISTING_PORT, "/sear*", "/fail*");
        limitingServiceFetcher.process(null, request);

    }

    /**
     * negative test
     * test path matching (pattern doesn't match with path)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testFailSkippedIpLimitingPathes() {
        buildIpLimitingFALSEResponse();
        HttpServletRequest request = prepareRequest();
        LimitingServiceFetcher limitingServiceFetcher = createService(EXISTING_PORT, "/fail*");
        limitingServiceFetcher.process(null, request);

    }

    /**
     * positive test
     * test user-agent matches pattern
     */
    @Test
    public void testSkippedUserAgent() {
        buildIpLimitingFALSEResponse();
        MockHttpServletRequest request = prepareRequest();
        request.addHeader("User-Agent", "user-agent-something one two");
        LimitingServiceFetcher limitingServiceFetcher = createServiceBuilder(EXISTING_PORT)
                .userAgentSkippedPattern("user-agent-.*").build();
        limitingServiceFetcher.process(null, request);

    }

    /**
     * negative test
     * test user-agent doesn't match pattern
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRejectedUserAgent() {
        buildIpLimitingFALSEResponse();
        MockHttpServletRequest request = prepareRequest();
        request.addHeader("User-Agent", "user-agent-something one two");
        LimitingServiceFetcher limitingServiceFetcher = createServiceBuilder(EXISTING_PORT)
                .userAgentSkippedPattern("agent-user-.*").build();
        limitingServiceFetcher.process(null, request);

    }

    private void buildIpLimitingFALSEResponse() {
        wireMockRule.stubFor(get(urlPathMatching("/fail")).willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\"isSuccess\":\"false\"}")));
    }

    private void buildIpLimitingTRUEResponse(int timeout) {
        wireMockRule.stubFor(get(urlPathMatching("/search")).willReturn(aResponse()
                .withFixedDelay(timeout)
                .withHeader("Content-Type", "application/json")
                .withBody("{\"isSuccess\":\"true\"}")));
    }

    private MockHttpServletRequest prepareRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/search");
        request.setQueryString("key1=value1&key2=value2&key3=kkkk,key4=");
        return request;
    }

    private LimitingServiceFetcher createService(int port, String ... skippedPathes) {
        return createServiceBuilder(port, skippedPathes)
                .build();
    }

    private LimitingServiceFetcher.Builder createServiceBuilder(int port, String ... skippedPathes) {
        return LimitingServiceFetcher.newBuilder()
                .url("http://localhost:" + port + "/search")
                .clientIpAddress("clientIpAddress")
                .paramKeys(Arrays.asList("key1", "key2", "User-Agent"))
                .skippedIpLimitingPatterns(skippedPathes)
                .restTemplate(gatewayConfig.restTemplate(2000));
    }

}