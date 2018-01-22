package com.navinfo.opentsp.gateway.web.limiting;

import com.codahale.metrics.annotation.Metric;
import com.navinfo.opentsp.common.messaging.Command;
import com.navinfo.opentsp.common.messaging.ProxiedQuery;
import com.navinfo.opentsp.gateway.web.controller.ControllerSuccessor;
import com.sun.jndi.toolkit.url.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Service which tries to request information from Ip limiting service
 */
public class LimitingServiceFetcher implements ControllerSuccessor {

    private final static Logger LOGGER = LoggerFactory.getLogger(LimitingServiceFetcher.class);

    private final String url;
    private final String clientIpAddress;
    private final RestTemplate restTemplate;
    private final List<String> paramKeys;
    private final String[] skippedIpLimitingPatterns;
    private final String userAgentSkippedPattern;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * @param builder
     */
    private LimitingServiceFetcher(Builder builder) {
        url = builder.url;
        clientIpAddress = builder.clientIpAddress;
        restTemplate = builder.restTemplate;
        paramKeys = builder.paramKeys;
        skippedIpLimitingPatterns = builder.skippedIpLimitingPatterns;
        userAgentSkippedPattern = builder.userAgentSkippedPattern;
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    /**
     * Check command by sending request to remote limiting service
     * if service returns false, browser will show 400 with text "Access was not granted for url: " + url
     * if service returns 500 or doesn't response browser will show 500
     * <p/>
     * Method has annotation Counted (which allows to understand which part of request time will take this "ip-limit-service" request)
     *
     * @param command
     * @param request
     */
    @Metric(name = "ip-limit-service")
    @Override
    public void process(Command<?> command, HttpServletRequest request) {
        if (filterCommand(command, request)) {
            String url = buildUrl(request);
            boolean success = doRequest(url);
            if (!success) {
                String fetchedUrl = extractURL(request);
                LOGGER.warn("command ({}) by url ({}) was rejected by ip-limiting service", command, fetchedUrl);
                throw new IllegalArgumentException("ip-limiting service rejected request by url: " + fetchedUrl);
            }
        }
    }

    /**
     * check request match for ip-limiting checking;
     * @param command
     * @param request
     * @return
     */
    private boolean filterCommand(Command<?> command, HttpServletRequest request) {
        if (!StringUtils.hasText(url)) {
            LOGGER.debug("Ip limiting disabled: ip-limiting url is empty");
            return false;
        }
        for (int i = 0; i < skippedIpLimitingPatterns.length; i++) {
            String skippedIpLimitingPath = skippedIpLimitingPatterns[i];
            boolean matches = antPathMatcher.match(skippedIpLimitingPath, request.getRequestURI());
            if (matches) {
                LOGGER.debug("URL matches pattern {}", skippedIpLimitingPath);
                return false;
            }
        }
        if (StringUtils.hasText(userAgentSkippedPattern)) {
            String userAgent = request.getHeader("User-Agent");
            try {
                String decoded = UriUtils.decode(userAgent, StandardCharsets.UTF_8.name());
                boolean matches = decoded.matches(userAgentSkippedPattern);
                if (matches) {
                    LOGGER.debug("user-agent {} matches Regexp {}", decoded, userAgentSkippedPattern);
                    return false;
                }
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("Can't decode string", e);
                return false;
            }
        }
        if (command == null || command instanceof ProxiedQuery) {
            return true;
        }
        LOGGER.debug("Command !instanceof ProxiedQuery");
        return false;
    }

    /**
     * Perform remote request
     * Methods catches HttpClientErrorException and return false
     * But HttpServerErrorException will cause 500 in browser - it's mean that remote service is npt available!
     *
     * @param url
     * @return
     */
    public boolean doRequest(String url) {
        try {
             ApiGrantResult result = restTemplate.getForObject(url, ApiGrantResult.class);
            LOGGER.debug("result of request ({}) to limiting service {}", url, result);
            return result.getIsSuccess() == null ? false : result.getIsSuccess();
        } catch (HttpClientErrorException e) {
            LOGGER.warn("client request error", e);
            return false;
        } catch (Exception e) {
            LOGGER.warn("server request error", e);
            return true;
        }
    }

    /**
     * Build url by params
     *
     * @param request
     * @return
     */
    protected String buildUrl(HttpServletRequest request) {
        Map<String, String> map = prepareQueryStringMap(request.getQueryString());
        normalizeQueryString(map, request);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (String paramKey : paramKeys) {
            String value = fetchValue(paramKey, map, request);
            builder.queryParam(paramKey, value);
        }
        builder.queryParam("ip", extractIP(request));
        String uriPattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
        boolean  regValid=uriPattern.matches( "\\S*/\\{\\S+\\}\\S*");
        if(regValid){
            builder.queryParam("url", uriPattern);
        }else {
            builder.queryParam("url", extractURL(request));
        }
        builder.queryParam("ym", request.getHeader("Origin"));
        String result = builder.toUriString();
        LOGGER.debug("result url: {}", result);
        return result;
    }

    /**
     * set to request queryString w/o com.navinfo.opentsp.gateway.web.limiting.LimitingServiceFetcher#paramKeys
     *
     * @param request
     */
    protected void normalizeQueryString(Map<String, String> m, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>(m);
        Set<String> keys = map.keySet();
        keys.removeAll(paramKeys);
        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        for (String key : keys) {
            builder.queryParam(key, map.get(key));
        }
        request.setAttribute("queryString", builder.build().toUri().getQuery());

    }

    private String extractURL(HttpServletRequest request) {
        return StringUtils.hasText(request.getHeader("url")) ? request.getHeader("url") : request.getRequestURI();
    }

    private String extractIP(HttpServletRequest request) {
        if (StringUtils.hasText(clientIpAddress)) {
            String value = request.getHeader(clientIpAddress);
            if (value != null) { return value; }
        }
        return request.getRemoteAddr();
    }

    /**
     * Tries to fetch data in next order:
     * 1. from header (key in header should have "X-" prefix)
     * 2. from header (w/o "X-" prefix)
     * 3. from request string
     *
     * @param paramKey
     * @param map
     * @param request
     * @return
     */
    private String fetchValue(String paramKey, Map<String, String> map, HttpServletRequest request) {
        String xheader = request.getHeader("X-" + paramKey);
        String header = request.getHeader(paramKey);
        if (StringUtils.hasText(xheader)) {
            return xheader;
        } else if (StringUtils.hasText(header)) {
            return header;
        } else {
            return map.get(paramKey);
        }
    }

    /**
     * parses queryString
     *
     * @param queryString
     * @return
     */
    protected Map<String, String> prepareQueryStringMap(String queryString) {
        if (queryString == null) {
            return Collections.emptyMap();
        }

        Map<String, String> map = new HashMap<>();
        String[] split = queryString.split("&");
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            String[] splited = s.split("=");
            if (splited.length == 2) {
                map.put(splited[0], splited[1]);
            }
        }
        return map;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    public static final class Builder {
        private String url;
        private String clientIpAddress;
        private RestTemplate restTemplate;
        private List<String> paramKeys;
        private String[] skippedIpLimitingPatterns;
        private String userAgentSkippedPattern;

        private Builder() {
        }

        public LimitingServiceFetcher build() {
            return new LimitingServiceFetcher(this);
        }


        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder clientIpAddress(String val) {
            clientIpAddress = val;
            return this;
        }

        public Builder restTemplate(RestTemplate val) {
            restTemplate = val;
            return this;
        }

        public Builder paramKeys(List<String> val) {
            paramKeys = val;
            return this;
        }

        public Builder skippedIpLimitingPatterns(String[] val) {
            skippedIpLimitingPatterns = val;
            return this;
        }

        public Builder userAgentSkippedPattern(String val) {
            userAgentSkippedPattern = val;
            return this;
        }
    }
}
