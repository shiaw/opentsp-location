package com.navinfo.opentsp.gateway.web.controller;

import com.navinfo.opentsp.common.messaging.Command;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.ResultCallback;
import com.navinfo.opentsp.platform.cms.command.InnerStub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class InnerStubController extends DefaultControllerHandler {

    private final static ResponseEntity RESPONSE = new ResponseEntity(null, null, HttpStatus.OK);

    private final static ResultCallback RESULT_CALLBACK = new ResultCallback() {
        @Override
        public void onResult(Object o) { }
        @Override
        public void onError(RuntimeException e) { }
    };

    @Autowired
    public InnerStubController(MessageChannel messageChannel) {
        super(messageChannel);
    }

    @Override
    public ResponseEntity<Object> fetchData(Command<?> object, HttpServletRequest request) throws Exception {
        InnerStub innerStub = (InnerStub) object;
        Long count = innerStub.getCount();
        if (count == null || count == 0) {
            getMessageChannel().sendAndReceive(object);
            return RESPONSE;
        } else {
            for (int i = 0; i < count; i++) {
                getMessageChannel().sendAndReceive(object, RESULT_CALLBACK);
            }
            return RESPONSE;
        }
    }

    @Override
    public boolean supports(Class<? extends Command> aClass) {
        return InnerStub.class == aClass;
    }
}
