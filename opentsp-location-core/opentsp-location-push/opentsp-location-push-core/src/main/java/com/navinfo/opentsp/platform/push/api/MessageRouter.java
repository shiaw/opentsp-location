package com.navinfo.opentsp.platform.push.api;

import com.navinfo.opentsp.platform.push.ScheduledTask;

import java.util.Collection;

/**
 * This router chooses recipients for message. <p/>
 * Router can use static list of recipients or compute recipients with {@link ScheduledTask#getRouterArgument()}.
 * For example 'userRouter' with routerArgument = 'oneUser, twoUser, threeUser' sends message to these users,
 *  but router 'groupRouter' with routerArgument = 'administrator' will chooses all members from 'administrator' security group.
 *  @see UserRecipient
 */
public interface MessageRouter {
    /**
     * Resolve message route and obtain list of all message recipients.
     * @param task
     * @return collection of message recipients, it must be an convertible to json. Note that we cannot return strings or other primitives
     *  because we need to known the type of recipient (for example {@link UserRecipient} or device)
     */
    Collection<?> resolveRoute(ScheduledTask task);
}
