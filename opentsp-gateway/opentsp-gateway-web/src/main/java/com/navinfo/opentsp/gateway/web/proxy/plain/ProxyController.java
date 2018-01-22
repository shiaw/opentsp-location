package com.navinfo.opentsp.gateway.web.proxy.plain;

import com.codahale.metrics.annotation.Metric;
import com.navinfo.opentsp.gateway.balancer.web.proxy.common.HttpProxyContext;
import com.navinfo.opentsp.gateway.web.proxy.AbstractController;
import org.apache.catalina.util.ParameterMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Proxy command controller
 */
@RestController
public class ProxyController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyController.class);


    /**
     * Proxy controller entry point
     * has metrics annotation
     *
     * @param webRequest for fetching URL pattern
     * @param request    servlet
     * @param response   servlet
     * @throws Exception
     */
    @Metric(name = "proxy-request")
    @ResponseBody
    public void proxy(final NativeWebRequest webRequest,
                      final HttpServletRequest request,
                      final HttpServletResponse response) throws Exception {
        wrapRequest(webRequest, request, response);

    }

    /**
     * Fetch response
     */
    protected void doRequest(final NativeWebRequest webRequest,
                             final HttpServletRequest request,
                             final HttpServletResponse response) throws Exception {
        long ipLimitingStart = System.currentTimeMillis();
        //checks request in ip-limiting service
        if (getSuccessors() != null) {
            for (int i = 0; i < getSuccessors().length; i++) { getSuccessors()[i].process(null, request); }
        }
        long ipLimitingStop = System.currentTimeMillis();

        //gets URL pattern
        String uriPattern = webRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE, 0).toString();
        String poxyURI=getProxyCommandResolver().getRemoteUrlByUriPattern(uriPattern);
        if(uriPattern.matches( "\\S*/\\{\\S+\\}\\S*")) {
            Map<String,String> pathvariableMap = (Map<String,String>) webRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, 0);
            for (String pathvariableKey:pathvariableMap.keySet()){
                poxyURI= poxyURI.replace("{"+pathvariableKey+"}",pathvariableMap.get(pathvariableKey));
            }
        }
        final URI uri = new URI(poxyURI);
        //does async request
        final HttpProxyContext proxyContext = new HttpProxyContext(request, response, uri, null);
        long proxyStart = System.currentTimeMillis();
        getHttpProxy().service(proxyContext);
        long proxyEnd = System.currentTimeMillis();

        LOGGER.info("some metrics: iplimiting: started: {}, finished: {}, duration: {}," +
                        " balance request started: {}, finished: {}, duration: {}",
                ipLimitingStart, ipLimitingStop, ipLimitingStop - ipLimitingStart,
                proxyStart, proxyEnd, proxyEnd - proxyStart);


    }
}
