package com.navinfo.opentsp.platform.push;

/**
 * Enum with types of components. We hardcoded limited list
 * of component types due to all devices must support of its.
 */
public enum ComponentType {
    /**
     * Simple text label, client must support simple html formatting or remove html tags.
     */
    LABEL,
    /**
     * Input field, optionally may show title inside own shape, but it does not required.
     */
    INPUT,
    /**
     * Button, also must support html or remove tags.
     */
    BUTTON,
    /**
     * One or multi selection list
     */
    LIST
}
