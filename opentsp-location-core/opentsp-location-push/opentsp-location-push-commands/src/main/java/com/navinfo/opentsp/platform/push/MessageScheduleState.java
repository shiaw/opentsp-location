package com.navinfo.opentsp.platform.push;

import com.navinfo.opentsp.common.utils.ArrayUtils;

import java.util.*;

/**
 * State of message scheduling <p/>
 *  note that in some cases message at once can have SUCCESS or DELIVERED state
 *  (it usual for messages which targeted for concrete user which is online), each state
 *  have 'isEnd()' method which allow us to determine end of message delivering process.
 */
public enum MessageScheduleState {

    /**
     * successful delivering to all recipients,
     */
    SUCCESS,
    /**
     * canceling of message through API
     */
    CANCEL,
    /**
     * TTL excision.
     */
    EXCEED,
    /**
     * message can not be delivered due to errors
     */
    ERROR,
    /**
     * message sent to some users (if message delivered to all users then state will be {@link #SUCCESS})
     */
    DELIVER(SUCCESS, CANCEL, EXCEED, ERROR),
    /**
     *  message was scheduled for delivering,
     */
    SCHEDULE(DELIVER, SUCCESS, CANCEL, EXCEED, ERROR);

    private final Set<MessageScheduleState> nextStates;

    MessageScheduleState(MessageScheduleState ... next) {
        // EnumSet is not work it ctor of enum
        Set<MessageScheduleState> set = new HashSet<>();
        if(!ArrayUtils.isEmpty(next)) {
            set.addAll(Arrays.asList(next));
        }
        this.nextStates = Collections.unmodifiableSet(set);
    }

    /**
     * Set of all possible next states.
     * @return
     * /
    public Set<MessageScheduleState> getNextStates() {
        return nextStates;
    }

    /**
     * Determine when is mean the end state.
     * @return
     * /
    @JsonIgnore
    public boolean isEnd() {
        return nextStates.isEmpty();
    }*/
}
