package com.navinfo.opentsp.platform.push.online;

import com.navinfo.opentsp.platform.push.api.MessageParcel;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
*/
final class UserRegistration {

    private final String username;
    private final List<OnlineUser> handles = new CopyOnWriteArrayList<>();

    UserRegistration(String username) {
        this.username = username;
    }

    public void deliver(MessageParcel parcel) {
        for(OnlineUser handle: handles) {
            handle.consume(parcel);
        }
    }

    public void register(OnlineUser onlineUser) {
        handles.add(onlineUser);
    }

    public void remove(OnlineUser onlineUser) {
        handles.remove(onlineUser);
    }
}
