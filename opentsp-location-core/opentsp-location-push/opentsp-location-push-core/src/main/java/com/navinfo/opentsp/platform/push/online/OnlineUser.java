package com.navinfo.opentsp.platform.push.online;

import com.navinfo.opentsp.platform.push.api.MessageParcel;
import com.navinfo.opentspcore.common.security.ExtendedUserDetails;
import org.springframework.util.Assert;

import java.util.function.Consumer;

/**
 * Handle for online user. Currently it allows only remove user registration.
 */
public class OnlineUser implements AutoCloseable {

    private final OnlineUsers onlineUsers;
    private final ExtendedUserDetails userDetails;
    private final Consumer<MessageParcel> consumer;

    OnlineUser(OnlineUsers onlineUsers, ExtendedUserDetails userDetails, Consumer<MessageParcel> consumer) {
        this.onlineUsers = onlineUsers;
        this.userDetails = userDetails;
        this.consumer = consumer;
        Assert.notNull(this.consumer, "consumer is null");
        Assert.notNull(this.userDetails, "userDetails is null");
    }

    @Override
    public void close() {
        this.onlineUsers.remove(this);
    }

    public String getUsername() {
        return userDetails.getUsername();
    }

    public ExtendedUserDetails getUserDetails() {
        return userDetails;
    }

    void consume(MessageParcel parcel) {
        this.consumer.accept(parcel);
    }
}
