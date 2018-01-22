package com.navinfo.opentsp.gateway.web.proxy;

import com.navinfo.opentsp.common.messaging.Command;
import com.navinfo.opentsp.common.utils.Closeables;
import com.navinfo.opentsp.gateway.balancer.web.proxy.common.HttpProxy;
import com.navinfo.opentsp.gateway.web.controller.ControllerSuccessor;
import com.navinfo.opentspcore.common.log.AmqpAppender;
import com.navinfo.opentspcore.common.log.DiagnosticInfo;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Abstract command controller
 */
public abstract class AbstractController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private DiagnosticInfo diagnosticInfo;

    @Autowired
    private ProxyCommandResolver proxyCommandResolver;

    @Autowired
    private HttpProxy httpProxy;

    private ControllerSuccessor[] successors;

    protected void wrapRequest(final NativeWebRequest webRequest,
                                               final HttpServletRequest request,
                                               final HttpServletResponse response) throws Exception {
        try (AutoCloseable context = setupContext(null, request)) {
            try {
                doRequest(webRequest, request, response);
            } catch (BindException | MethodArgumentNotValidException | IllegalArgumentException | IllegalStateException e) {
                throw e;
            } catch (Exception e) {
                LOGGER.error("Can't process request {" + request.getRequestURL() + "}", e);
                throw new HttpException(e.getMessage());
            }
        }
    }

    /**
     * Fetch response
     */
    abstract protected void doRequest(final NativeWebRequest webRequest,
                                                       final HttpServletRequest request,
                                                       final HttpServletResponse response) throws Exception;

    /**
     * Setup context: gather metric statistic injects auth info, run chain of successors
     *
     * @param command
     * @param request
     * @return
     */
    protected AutoCloseable setupContext(Command<?> command, HttpServletRequest request) {
        final AutoCloseable context = open(command, request);
        return new AutoCloseable() {
            @Override
            public void close() {
                // we cannot throw any exception, because in catch result value will be overwrited with exception
                Closeables.close(context);
            }
        };
    }

    public AutoCloseable open(Command<?> command, HttpServletRequest request) {

        String uuid = request.getHeader(AmqpAppender.REQUEST_UUID);
        final AutoCloseable diagnosticInfoContext = diagnosticInfo.injectToContext(uuid, request.getRequestURI());

        return new AutoCloseable() {
            @Override
            public void close() throws Exception {
                //end time metering
                Closeables.close(diagnosticInfoContext);

            }
        };
    }

    @Autowired(required = false)
    public void setControllerSuccessor(final List<ControllerSuccessor> controllerSuccessors) {
        Collections.sort(controllerSuccessors, new Comparator<ControllerSuccessor>() {
            @Override
            public int compare(ControllerSuccessor o1, ControllerSuccessor o2) {
                return Integer.compare(o1.getOrder(), o2.getOrder());
            }
        });
        successors = controllerSuccessors.toArray(new ControllerSuccessor[controllerSuccessors.size()]);
    }

    public DiagnosticInfo getDiagnosticInfo() {
        return diagnosticInfo;
    }

    public HttpProxy getHttpProxy() {
        return httpProxy;
    }

    public ProxyCommandResolver getProxyCommandResolver() {
        return proxyCommandResolver;
    }

    public ControllerSuccessor[] getSuccessors() {
        return successors;
    }
}
