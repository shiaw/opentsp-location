package com.navinfo.opentsp.platform.push.online;

import com.navinfo.opentsp.platform.push.api.MessageDeliverer;
import com.navinfo.opentsp.platform.push.api.MessageParcel;
import com.navinfo.opentsp.platform.push.api.UserDeviceRecipient;
import com.navinfo.opentsp.platform.push.api.UserRecipient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Deliverer which send messages (or give ability to obtain them) to online users.
 */
@Component(OnlineDeliverer.ID)
public class OnlineDeliverer implements MessageDeliverer {

    public static final String ID = "onlineDeliverer";
    private final OnlineUsers onlineUsers;

    @Autowired
    public OnlineDeliverer(OnlineUsers onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    @Override
    public void deliver(MessageParcel parcel) throws Exception {
        Object rec = parcel.getRecipient();
        if(rec instanceof UserRecipient) {
            UserRecipient ur = (UserRecipient) rec;
            final String username = ur.getUsername();
            this.onlineUsers.deliverToUser(parcel, username);
        } else if(rec instanceof UserDeviceRecipient) {
            UserDeviceRecipient udr = (UserDeviceRecipient) rec;
            this.onlineUsers.deliverToUserDevice(parcel, udr.getDeviceId());
        } else {
            throw new RuntimeException("Unsupported recipient: " + rec);
        }
    }
}
