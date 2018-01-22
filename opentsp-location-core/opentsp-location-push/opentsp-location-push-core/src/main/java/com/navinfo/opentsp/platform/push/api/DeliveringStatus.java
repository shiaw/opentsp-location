package com.navinfo.opentsp.platform.push.api;

/**
 * Status of single delivering
 */
public enum DeliveringStatus {
    /**
     * Delivering in process
     */
    SCHEDULED(false),
    /**
     * Delivering success
     */
    OK(true),
    /**
     * Delivering error, it is mean that we must choose next delivering method
     */
    ERROR(false),
    /**
     * Delivering fail, we do not have any way to deliver message, usually it means that all methods give error.
     */
    FAIL(true);

    private final boolean end;

    DeliveringStatus(boolean end) {
        this.end = end;
    }

    public boolean isEnd() {
        return end;
    }
}
