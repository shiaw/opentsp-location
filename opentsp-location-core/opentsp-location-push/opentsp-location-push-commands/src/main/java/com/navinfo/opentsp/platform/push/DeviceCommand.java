package com.navinfo.opentsp.platform.push;

import com.google.common.base.MoreObjects;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of message to car management device.
 */
@MessageGroup(PushModuleConstants.QUEUE)
public class DeviceCommand extends AbstractUserDeviceCommand<DeviceResult> {

    @NotNull
    private String command;
    private int  resultCode;
    private Map<String, Object> arguments;

    /**
     * The string which identity command on device. Usually this string is dot delimited, also we have some predefined
     * commands in {@link com.navinfo.opentspcore.common.userdevice.UserDeviceUtils }
     * @return
     */
    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    /**
     * Command arguments. Restrictions to value same as {@link #getAttributes() }
     * @return
     */
    public Map<String, Object> getArguments() {
        return arguments;
    }

    public <T> T getArgument(String key, Class<T> type) {
        if(arguments == null) {
            return null;
        }
        Object obj = arguments.get(key);
        return type.cast(obj);
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }

    @Override
    public Class<? extends DeviceResult> getResultType() {
        return DeviceResult.class;
    }

    @Override
    protected void toString(MoreObjects.ToStringHelper tsh) {
        super.toString(tsh);
        tsh.add("command", command);
        tsh.add("arguments", arguments);
    }

    @Override
    public DeviceCommand clone() {
        DeviceCommand clone = (DeviceCommand) super.clone();
        clone.arguments = this.arguments == null? null : new HashMap<>(this.arguments);
        return clone;
    }


    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
