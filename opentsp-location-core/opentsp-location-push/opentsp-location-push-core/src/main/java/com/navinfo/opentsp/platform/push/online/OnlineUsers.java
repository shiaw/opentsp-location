package com.navinfo.opentsp.platform.push.online;

import com.navinfo.opentsp.platform.push.DeviceStatus;
import com.navinfo.opentsp.platform.push.api.MessageParcel;
import com.navinfo.opentspcore.common.gateway.events.ClientConnectedEvent;
import com.navinfo.opentspcore.common.security.ExtendedUserDetails;
import com.navinfo.opentspcore.common.userdevice.UserDeviceCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Processor which do some work on delivering parcel to online user (like resolving online user and its devices).
 */
@Component
public class OnlineUsers {

    private static final Logger logger = LoggerFactory.getLogger(OnlineUsers.class);

    private Function<String, UserRegistration> registrationFactory = new Function<String, UserRegistration>() {
        @Override
        public UserRegistration apply(String username) {
            return new UserRegistration(username);
        }
    };

    private final ConcurrentMap<String, UserRegistration> online = new ConcurrentHashMap<>();
    private final OnlineStatusStorage storage;
    private final UserDeviceSender sender;
    private final DeviceChooserFactory dcf;

    @Autowired
    public OnlineUsers(OnlineStatusStorage storage, UserDeviceSender sender, DeviceChooserFactory dcf) {
        this.storage = storage;
        this.sender = sender;
        this.dcf = dcf;
    }

    public void online(String userDeviceId, String returnAddress) {
        DeviceRegistration dr = new DeviceRegistration();
        dr.setDevice(userDeviceId);
        dr.setReturnAddress(returnAddress);
       // dr.setTtl(event.getTtl());
        dr.setStatus(DeviceStatus.ONLINE);
        this.storage.register(dr);
    }

    public void offline(String userDeviceId) {
        DeviceRegistration dr = this.storage.get(userDeviceId);
        if(dr==null){
            logger.error("deviceId:{} offline ",userDeviceId);
            return;
        }
        // dr.setTtl(event.getTtl());
        dr.setStatus(DeviceStatus.OFFLINE);
        this.storage.register(dr);
    }

    public void heartbeat(String userDeviceId) {
        this.storage.heartbeat(userDeviceId);
    }

//    public void offline(String userDeviceId) {
//        this.storage.unregister(userDeviceId);
//    }

    /**
     * This method try deliver parcel to user by any affordable way.
     *
     * @param parcel
     * @param username
     */
    void deliverToUser(MessageParcel parcel, String username) throws Exception {
        UserRegistration consumer = online.get(username);
        if (consumer != null) {
            consumer.deliver(parcel);
        } else {
            DeviceChooserFactory.DeviceChooser chooser = dcf.newChooser(username);
            storage.enumerate(username, chooser);
            DeviceRegistration deviceRegistration = chooser.getDeviceRegistration();
            if(deviceRegistration != null) {
                invokeSend(parcel, username, deviceRegistration);
            }
        }
    }

    void deliverToUserDevice(MessageParcel parcel, String userDeviceId) throws Exception {
        DeviceRegistration deviceRegistration = this.storage.get( userDeviceId);
        if(deviceRegistration == null) {
            // device is offline
            return;
        }
        invokeSend(parcel, userDeviceId, deviceRegistration);
    }

    private void invokeSend(MessageParcel parcel, String userDeviceId, DeviceRegistration deviceRegistration) throws Exception {
        Object message = parcel.getTask().getMessage();
        if(!(message instanceof UserDeviceCommand)) {
            throw new RuntimeException("Service support only UserDeviceCommand instances as message");
        }
        UserDeviceCommand templateCmd = (UserDeviceCommand) message;
        // we must not modify original message
        UserDeviceCommand cmd = templateCmd.clone();
        cmd.setDevice(deviceRegistration.getDevice());
        cmd.setUsername(null);
        UserDeviceSenderContext ctx = new UserDeviceSenderContext(parcel, cmd, deviceRegistration.getReturnAddress());
        this.sender.send(ctx);
    }

    void remove(OnlineUser onlineUser) {
        UserRegistration registration = online.get(onlineUser.getUsername());
        if(registration != null) {
            registration.remove(onlineUser);
        }
    }

    public OnlineUser onlineTemporary(ExtendedUserDetails userDetails, Consumer<MessageParcel> consumer) {
        final OnlineUser onlineUser = new OnlineUser(this, userDetails, consumer);
        UserRegistration userRegistration = online.computeIfAbsent(userDetails.getUsername(), registrationFactory);
        userRegistration.register(onlineUser);
        return onlineUser;
    }

}
