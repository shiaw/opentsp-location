package com.navinfo.opentsp.gateway.web.proxy;

import com.navinfo.opentsp.gateway.web.utils.CommandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Spring Proxy requests mapper
 */
@Service
public class ProxyCommandMapper extends CommandMapper {

    public static final RequestMethod[] METHODS = new RequestMethod[]{GET, DELETE, POST, PUT, OPTIONS, PATCH};

    private final ProxyCommandResolver proxyCommandFetcher;

    @Autowired
    public ProxyCommandMapper(ProxyCommandResolver proxyCommandFetcher) {
        this.proxyCommandFetcher = proxyCommandFetcher;
        setOrder(1);
    }

    /**
     * Creates mapping between restControllers METHODS and URLs for servlet dispatcher
     *
     * @param method
     * @param handlerType
     * @return
     */
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        //request super for matching rest URLs
        RequestMappingInfo mappingForMethod = super.getMappingForMethod(method, handlerType);
        if (mappingForMethod != null) {
            return mappingForMethod;
        }
        // if method name equals balance then create mapping
        if (method.getName().equals("proxy")) {
            return this.createRequestMappingInfo(proxyCommandFetcher.getUriPatternToUrlMappingKeys(), METHODS);
        }
        if (method.getName().equals("balance")) {
            return this.createRequestMappingInfo(proxyCommandFetcher.getUriPatternToIdMappingKeys(), METHODS);
        }
        return null;
    }

    protected boolean isHandler(Class<?> beanType) {
        return true;
    }

}
