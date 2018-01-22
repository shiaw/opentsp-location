package com.navinfo.opentsp.platform.push.persisted;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Simple holder which provide to us easy way for store object with its type into json through Jackson library.
 */
final class JsonHolder {
    private Object value;

    public JsonHolder() {
    }

    public JsonHolder(Object value) {
        this.value = value;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
