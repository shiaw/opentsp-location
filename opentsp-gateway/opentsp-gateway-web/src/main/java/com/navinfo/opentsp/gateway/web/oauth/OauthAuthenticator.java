package com.navinfo.opentsp.gateway.web.oauth;

import com.navinfo.opentsp.common.messaging.HttpResult;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.users.commands.OauthValidateCommand;
import com.navinfo.opentsp.users.commands.dto.OauthCredentials;
import com.navinfo.opentspcore.common.security.Authenticator;
import com.navinfo.opentspcore.common.security.AuthenticatorRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class OauthAuthenticator implements Authenticator {

    @Autowired
    private MessageChannel messageChannel;

    @Override
    public boolean authenticate(AuthenticatorRequest authenticatorRequest) throws AuthenticationException {
        OauthAuthenticationToken oauth = (OauthAuthenticationToken) authenticatorRequest.getAuthentication();
        OauthValidateCommand command = new OauthValidateCommand();
        command.setCredentials(oauth.getCredentials());
        try {
            OauthValidateCommand.Result result = messageChannel.sendAndReceive(command);
            final OauthCredentials cred = oauth.getCredentials().fromPrototype(oauth.getCredentials()).username(result.getUsername()).build();
            oauth.setCredentials(cred);
            oauth.getCommand().setUsername(result.getUsername());
            return result.getResultCode() == HttpResult.OK.code();
        } catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }
}
