package com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.config;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Configuration
public class FilterCodeConfig {

    private static final Logger log = LoggerFactory.getLogger(FilterCodeConfig.class);

    @Value("${filter.code}")
    private String[] filterCode;

    private List<String> filterCodeList = Lists.newArrayList();

    public List<String> getFilterCodeList() {
        return filterCodeList;
    }

    public void setFilterCodeList(List<String> filterCodeList) {
        this.filterCodeList = filterCodeList;
    }

    @PostConstruct
    public void initFilterCode() {
        if (!StringUtils.isEmpty(filterCode)) {
            filterCodeList = Arrays.asList(filterCode);
        }
    }

}
