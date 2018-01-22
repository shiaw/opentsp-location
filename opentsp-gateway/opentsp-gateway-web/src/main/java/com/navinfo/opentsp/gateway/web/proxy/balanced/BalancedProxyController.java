package com.navinfo.opentsp.gateway.web.proxy.balanced;

import com.codahale.metrics.annotation.Metric;
import com.navinfo.opentsp.gateway.balancer.web.proxy.common.HttpProxyContext;
import com.navinfo.opentsp.gateway.web.proxy.AbstractController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerRequest;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

/**
 * Proxy command controller
 */
@RestController
public class BalancedProxyController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BalancedProxyController.class);

    private static final Object STUB = new Object();

    @Autowired
    private LoadBalancerClient balancerClient;

    /**
     * Proxy controller entry point
     * has metrics annotation
     *
     * @param webRequest for fetching URL pattern
     * @param request    servlet
     * @param response   servlet
     * @throws Exception
     */
    @Metric(name = "balanced-request")
    @ResponseBody
    public void balance(final NativeWebRequest webRequest,
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
        final String uriPattern = webRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE, 0).toString();

        long proxyStart = System.currentTimeMillis();
        //creates remote service URL
        String serviceID = getProxyCommandResolver().getServiceIDByUriPattern(uriPattern);
        Assert.notNull(serviceID, "can't find service id by path: " +uriPattern);
        balancerClient.execute(serviceID, new LoadBalancerRequest<Object>() {
            @Override
            public Object apply(ServiceInstance instance) throws Exception {
                // we do not want any value instead of uuid
                final URI src = new URI("http", null, instance.getHost(), instance.getPort(), request.getRequestURI(), null, null);
                final HttpProxyContext proxyContext = new HttpProxyContext(request, response, src, null);
                getHttpProxy().service(proxyContext);
                return STUB;
            }
        });

        long proxyEnd = System.currentTimeMillis();

        LOGGER.info("some metrics: iplimiting: started: {}, finished: {}, duration: {}," +
                        " balance request started: {}, finished: {}, duration: {}",
                ipLimitingStart, ipLimitingStop, ipLimitingStop - ipLimitingStart,
                proxyStart, proxyEnd, proxyEnd - proxyStart);


    }

}
