package com.navinfo.opentsp.platform.da.core.configuration;


import com.navinfo.opentsp.platform.da.core.common.SpringContextUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }

}
