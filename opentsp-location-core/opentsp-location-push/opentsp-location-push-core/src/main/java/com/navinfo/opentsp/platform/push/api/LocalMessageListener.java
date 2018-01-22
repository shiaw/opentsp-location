package com.navinfo.opentsp.platform.push.api;

/**
 * Listener for core events
 */
public interface LocalMessageListener {
    void accept(LocalMessageEvent event);
}
