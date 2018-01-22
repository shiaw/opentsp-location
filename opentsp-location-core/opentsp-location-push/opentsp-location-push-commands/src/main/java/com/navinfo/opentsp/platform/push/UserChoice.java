package com.navinfo.opentsp.platform.push;

/**
 * Is an one of possible user reacts on interaction. We provide default ids in constants, but do
 * not provide titles because they must be internationalised.
 */
public class UserChoice {

    /**
     * Id of default choice for most application, it usable also for informational messages, when we non need any
     * response from user.
     */
    public static final String ID_OK = "ok";
    public static final String ID_CANCEL = "cancel";

    private String id;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "UserChoice{" +
          "id='" + id + '\'' +
          ", title='" + title + '\'' +
          '}';
    }

    @Override
    public UserChoice clone() {
        try {
            return (UserChoice) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
