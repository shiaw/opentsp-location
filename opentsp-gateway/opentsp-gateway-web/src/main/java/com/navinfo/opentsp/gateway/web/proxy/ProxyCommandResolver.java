package com.navinfo.opentsp.gateway.web.proxy;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Resolve information from properties and build mapping
 */
@Service
public class ProxyCommandResolver {

    private static final String[] SCHEMES = {"http","https"}; // DEFAULT schemes = "http", "https", "ftp"
    private static final UrlValidator URL_VALIDATOR = new UrlValidator(SCHEMES);

    /**
     * URI pattern (path) -> remote URL mapping
     */
    private Map<String, String> uriPatternToUrlMapping;
    /**
     * URI pattern (path) -> service ID mapping
     */
    private Map<String, String> uriPatternToIdMapping;

    @Autowired
    public ProxyCommandResolver(Environment env) {
        initUriPatternToUrlMapper(env);
        initUriPatternToIdMapping(env);
    }

    private void initUriPatternToIdMapping(Environment env) {
        Map<String, String> tempMap = new HashMap<>(64, 0.3f);
        //will be chosen all properties which keys start from "balanced"
        for (Map.Entry<String, Object> entry : new RelaxedPropertyResolver(env).getSubProperties("balanced.service_id.").entrySet()) {
            String key = entry.getKey();
            String[] paths = entry.getValue().toString().split(",");
            for (String path : paths) {
                tempMap.put(path, key);
            }
        }
        uriPatternToIdMapping = Collections.unmodifiableMap(tempMap);
    }

    private void initUriPatternToUrlMapper(Environment env) {
        Map<String, String> tempMap = new HashMap<>(64, 0.3f);
        //will be chosen all properties which keys start from "/" and value is valid URL
        for (Map.Entry<String, Object> entry : new RelaxedPropertyResolver(env).getSubProperties("/").entrySet()) {
            String key = entry.getKey();
            String url = entry.getValue().toString();
            boolean valid = URL_VALIDATOR.isValid(url);
            //判断/test/{a}/{b} 包含{}
            boolean  regValid=url.matches( "\\S*/\\{\\S+\\}\\S*");
            if (valid||regValid) {
                tempMap.put("/" + key, url);
            }
        }
        uriPatternToUrlMapping = Collections.unmodifiableMap(tempMap);
    }

    public String getRemoteUrlByUriPattern(String url) {
        return uriPatternToUrlMapping.get(url);
    }

    public String getServiceIDByUriPattern(String url) {
        return uriPatternToIdMapping.get(url);
    }

    public Collection<String> getUriPatternToUrlMappingKeys() {
        return uriPatternToUrlMapping.keySet();
    }

    public Collection<String> getUriPatternToIdMappingKeys() {
        return uriPatternToIdMapping.keySet();
    }
}
