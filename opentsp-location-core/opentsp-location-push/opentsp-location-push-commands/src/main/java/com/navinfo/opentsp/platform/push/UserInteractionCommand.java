package com.navinfo.opentsp.platform.push;

import com.google.common.base.MoreObjects;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Command which show message or dialog to user. <p/>
 * If command has input components then result will contains map with 'component.id' -> 'value' pairs.
 * @see com.navinfo.opentspcore.common.userdevice.UserDeviceUtils#USER_INTERACT
 * @see com.navinfo.opentspcore.common.userdevice.UserDeviceUtils#USER_DISPLAY
 */
@MessageGroup(PushModuleConstants.QUEUE)
public class UserInteractionCommand extends AbstractUserDeviceCommand<UserInteractionCommand.Result> {

    @NotNull
    private String title;
    @NotNull
    private String body;
    private String mimeType;
    private List<DataRequest> requests;
    private List<UserChoice> choices;

    public UserInteractionCommand() {
    }

    /**
     * Title of message in plain text.
     * @return
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Text body of message, depend of mime type it must be an plain text, html or any other text format (rtf for example).
     * System must support only plain text and simplified html (b, i, u tags at least).
     * @return
     */
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    /**
     * Mime type of body, receiver MUST support text/plain and simplified (b, i, u tags at least) text/html
     * if device does not support formatted text, then all HTML tags must be removed.
     * @return
     */
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * List of requests of data to user. If is has any request then it will display to user as input field or any component
     * in unspecified layout (it depend from device) and result of command filled with values of
     * data which user was input.
     * @return
     */
    public List<DataRequest> getRequests() {
        return requests;
    }

    /**
     * List of requests of data to user. If is has any request then it will display to user as input field or any component
     * in unspecified layout (it depend from device) and result of command filled with values of
     * data which user was input.
     * @param requests
     */
    public void setRequests(List<DataRequest> requests) {
        this.requests = requests;
    }

    /**
     * Ordered list of possible variants to end interaction. It may be displayed as group of buttons or any other.
     * Id of chosen 'choice' will be placed into {@link com.navinfo.opentsp.platform.push.UserInteractionCommand.Result#getChoosen()}
     * @return
     */
    public List<UserChoice> getChoices() {
        return choices;
    }

    /**
     * Ordered list of possible variants to end interaction. It may be displayed as group of buttons or any other.
     * @param choices
     */
    public void setChoices(List<UserChoice> choices) {
        this.choices = choices;
    }

    @Override
    public Class<? extends Result> getResultType() {
        return Result.class;
    }

    @Override
    protected void toString(MoreObjects.ToStringHelper tsh) {
        tsh.add("title", title);
        tsh.add("body", body);
        tsh.add("mimeType", mimeType);
        tsh.add("requests", requests);
    }

    @Override
    public UserInteractionCommand clone() {
        UserInteractionCommand clone = (UserInteractionCommand) super.clone();
        if(this.choices != null) {
            final int choicesSize = this.choices.size();
            clone.choices = new ArrayList<>(choicesSize);
            for(int i = 0; i < choicesSize; ++i) {
                UserChoice userChoice = this.choices.get(i);
                clone.choices.add(userChoice.clone());
            }
        }
        if(this.requests != null) {
            final int requestsSize = this.requests.size();
            clone.requests = new ArrayList<>(requestsSize);
            for(int i = 0; i < requestsSize; ++i) {
                DataRequest dataRequest = this.requests.get(i);
                clone.requests.add(dataRequest.clone());
            }
        }
        return clone;
    }

    public static class Result extends DeviceResult {
        private String choosen;

        /**
         * Id of selected {@link #getChoices()}
         * @return
         */
        public String getChoosen() {
            return choosen;
        }

        /**
         * Id of selected {@link #getChoices()}
         * @param choosen
         */
        public void setChoosen(String choosen) {
            this.choosen = choosen;
        }
    }
}
