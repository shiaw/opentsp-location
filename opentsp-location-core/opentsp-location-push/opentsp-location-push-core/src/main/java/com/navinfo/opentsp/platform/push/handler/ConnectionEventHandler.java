package com.navinfo.opentsp.platform.push.handler;

import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.push.online.OnlineUsers;
import com.navinfo.opentsp.users.commands.dto.AuthenticationDetailsData;
import com.navinfo.opentspcore.common.gateway.ConnectionEvent;
import com.navinfo.opentspcore.common.gateway.ConnectionEventCommand;
import com.navinfo.opentspcore.common.gateway.events.ClientConnectedEvent;
import com.navinfo.opentspcore.common.gateway.events.ClientDisconnectedEvent;
import com.navinfo.opentspcore.common.gateway.events.ClientHeartbeatEvent;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Handler which listen user status and save it changes into {@link com.navinfo.opentsp.platform.push.online.OnlineUsers }
 */
@Component
public class ConnectionEventHandler extends AbstractCommandHandler<ConnectionEventCommand, CommandResult> {

    private final OnlineUsers onlineUsers;

    @Autowired
    public ConnectionEventHandler(OnlineUsers onlineUsers) {
        super(ConnectionEventCommand.class, CommandResult.class);
        this.onlineUsers = onlineUsers;
    }

    @Override
    public CommandResult handle(ConnectionEventCommand command) {
        String username = null;
        String userDeviceId = null;
        Authentication authentication = command.getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if(userDetails != null) {
            username = userDetails.getUsername();
        }
        AuthenticationDetailsData detailsData = (AuthenticationDetailsData) authentication.getDetails();
        if(detailsData != null) {
            userDeviceId = detailsData.getUserDeviceId();
        }
        if(userDeviceId != null && username != null) {
            ConnectionEvent event = command.getEvent();
            if(event instanceof ClientConnectedEvent) {
//                onlineUsers.online(username, userDeviceId, detailsData.getReturnAddress(), (ClientConnectedEvent) event);
            } else if(event instanceof ClientDisconnectedEvent) {
                onlineUsers.offline( userDeviceId);
            } else if(event instanceof ClientHeartbeatEvent) {
                onlineUsers.heartbeat( userDeviceId);
            }
        }
        return new CommandResult().fillResult(ResultCode.OK);
    }
}
