package com.navinfo.opentsp.platform.push;

import com.google.common.base.MoreObjects;
import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.AbstractSecuredCommand;
import com.navinfo.opentsp.common.messaging.Command;
import com.navinfo.opentspcore.common.push.PushConfig;
import com.navinfo.opentspcore.common.userdevice.UserDeviceCommand;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic message to device
 */
public abstract class AbstractUserDeviceCommand<T extends Command.Result> extends AbstractCommand<T> implements UserDeviceCommand<T> {

    @NotNull
    private String id;
    private String username;
    @NotNull
    private String device;
    private PushConfig pushConfig;
    private String  queueName;

    private Map<String, Object> attributes;

    /**
     * Name of target user, use current user from authentication if this field is null.
     * @return
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * * Name of target user.
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public <T> T getAttribute(String key, Class<T> type) {
        if(attributes == null) {
            return  null;
        }
        Object o = attributes.get(key);
        return type.cast(o);
    }

    /**
     * Attributes of message. It is unspecified, but some receivers can require additional attributes.
     * Note that attribute value must immutable and serializable by Jackson Json library.
     * @param attributes
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * Attributes of message. It is unspecified, but some receivers can require additional attributes.
     * Note that attribute value must immutable and serializable by Jackson Json library.
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public PushConfig getPushConfig() {
        return pushConfig;
    }

    public void setPushConfig(PushConfig pushConfig) {
        this.pushConfig = pushConfig;
    }

    /**
     * For extending use {@link #toString(com.google.common.base.MoreObjects.ToStringHelper)}
     * @return
     */
    @Override
    public final String toString() {
        MoreObjects.ToStringHelper tsh = MoreObjects.toStringHelper(getClass());
        tsh.add("id", id);
        tsh.add("username", username);
        tsh.add("device", device);
        tsh.add("attributes", attributes);
        tsh.add("pushConfig", pushConfig);
        toString(tsh);
        return tsh.toString();
    }

    @Override
    public AbstractUserDeviceCommand<?> clone() {
        AbstractUserDeviceCommand<?> clone;
        try {
            clone = (AbstractUserDeviceCommand<?>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        // we do not clone attributes because can not know support they cloning or not
        clone.attributes = this.attributes == null? null : new HashMap<>(this.attributes);
        return clone;
    }

    /**
     * You must implement this method to extend {@link #toString()} output.
     * @param tsh
     */
    protected void toString(MoreObjects.ToStringHelper tsh) {
        //nothing
    }

    @Override
    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
