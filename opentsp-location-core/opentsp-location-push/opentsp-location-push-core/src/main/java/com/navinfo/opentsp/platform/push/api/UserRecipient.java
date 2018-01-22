package com.navinfo.opentsp.platform.push.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User as recipient for message parcels
 */
public class UserRecipient {
    private final String username;

    @JsonCreator
    public UserRecipient(@JsonProperty("username") String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserRecipient)) {
            return false;
        }

        UserRecipient that = (UserRecipient) o;

        if (username != null ? !username.equals(that.username) : that.username != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserRecipient{" +
          "username='" + username + '\'' +
          '}';
    }
}
