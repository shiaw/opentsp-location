package com.navinfo.opentsp.platform.push;

/**
 * Type value which is returned by some dialog component
*/
public enum ValueType {
    STRING,
    /**
     * If it value is applied to input field then field may display as checkbox or toggle button.
     */
    BOOLEAN,
    INTEGER,
    FLOAT,
    /**
     * date in UTC millis
     */
    DATE,
    /**
     * time interval in millis
     */
    INTERVAL
}
