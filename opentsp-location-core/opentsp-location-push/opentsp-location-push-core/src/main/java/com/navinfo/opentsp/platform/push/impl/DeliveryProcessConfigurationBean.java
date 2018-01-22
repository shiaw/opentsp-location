package com.navinfo.opentsp.platform.push.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 */
@ConfigurationProperties("opentsp.push.proc")
public class DeliveryProcessConfigurationBean {

    private float delay;

    /**
     * Delay in seconds between worker iterations
     * @return
     */
    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }
}
