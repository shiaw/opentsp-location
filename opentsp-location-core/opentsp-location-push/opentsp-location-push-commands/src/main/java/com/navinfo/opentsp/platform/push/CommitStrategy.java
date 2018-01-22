package com.navinfo.opentsp.platform.push;

/**
 * Strategy type for commit delivering of retrieved messages
*/
public enum CommitStrategy {
    /**
     * Commit automatically
     */
    AUTO,
    /**
     * Commit automatically only non perisitent messages
     */
    AUTO_NON_PERSISTENT,
    /**
     * Does not do commit.
     */
    NONE
}
