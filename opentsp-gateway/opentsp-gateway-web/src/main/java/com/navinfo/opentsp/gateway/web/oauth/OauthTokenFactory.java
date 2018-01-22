package com.navinfo.opentsp.gateway.web.oauth;

import com.navinfo.opentsp.common.messaging.Command;
import com.navinfo.opentsp.gateway.web.auth.AuthTokenFactory;
import com.navinfo.opentsp.users.commands.OuthLoginCommand;
import com.navinfo.opentsp.users.commands.dto.OauthCredentials;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

/**
 * 'Authentication' token factory for {@link com.navinfo.opentsp.users.commands.OuthLoginCommand}
 */
@Component
public class OauthTokenFactory implements AuthTokenFactory {

    private static final Set<Class<?>> SET = Collections.<Class<?>>singleton(OuthLoginCommand.class);

    @Override
    public Set<Class<?>> getSupportedCommands() {
        return SET;
    }

    @Override
    public Authentication createAuthToken(Command<?> c) {
        OuthLoginCommand command = (OuthLoginCommand) c;
        OauthCredentials credentials = OauthCredentials.builder()
          .username(command.getUsername())
          .accessToken(command.getAccessToken())
          .openId(command.getOpenId())
          .type(command.getType())
          .build();
        return new OauthAuthenticationToken(credentials, command);
    }
}
