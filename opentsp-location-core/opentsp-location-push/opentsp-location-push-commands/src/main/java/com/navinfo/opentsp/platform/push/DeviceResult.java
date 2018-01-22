package com.navinfo.opentsp.platform.push;

import com.navinfo.opentsp.common.messaging.CommandResult;

import java.util.Map;

/**
 * Result of device command.
 */
public class DeviceResult extends CommandResult {
    private Map<String, Object> results;

    public Map<String, Object> getResults() {
        return results;
    }

    public <T> T getResult(String key, Class<T> type) {
        if(results == null) {
            return null;
        }
        Object o = results.get(key);
        return type.cast(o);
    }

    public void setResults(Map<String, Object> results) {
        this.results = results;
    }
}
