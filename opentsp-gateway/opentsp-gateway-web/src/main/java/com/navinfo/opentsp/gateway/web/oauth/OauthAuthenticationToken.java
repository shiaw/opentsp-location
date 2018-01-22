package com.navinfo.opentsp.gateway.web.oauth;

import com.navinfo.opentsp.users.commands.OuthLoginCommand;
import com.navinfo.opentsp.users.commands.dto.OauthCredentials;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;

/**
 */
public class OauthAuthenticationToken extends AbstractAuthenticationToken {

    private OauthCredentials credentials;
    private final OuthLoginCommand command;

    public OauthAuthenticationToken(OauthCredentials credentials, OuthLoginCommand command) {
        super(Collections.<GrantedAuthority>emptySet());
        this.credentials = credentials;
        this.command = command;
    }

    @Override
    public OauthCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(OauthCredentials credentials) {
        this.credentials = credentials;
    }

    public OuthLoginCommand getCommand() {
        return command;
    }

    @Override
    public String getPrincipal() {
        return credentials.getUsername();
    }
}
