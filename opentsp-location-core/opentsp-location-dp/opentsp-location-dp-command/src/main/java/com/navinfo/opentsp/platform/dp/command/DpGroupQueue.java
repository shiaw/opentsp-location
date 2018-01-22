package com.navinfo.opentsp.platform.dp.command;

import com.navinfo.opentsp.common.messaging.routing.Routes;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;
import org.springframework.security.access.annotation.Secured;

import java.lang.annotation.*;

/**
 * User: zhanhk
 * Date: 16/9/6
 * Time: 下午4:52
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Secured("ROLE_USER")
@MessageGroup(value = "dpqueue" ,exchange = Routes.EXCHANGE_SYSTEM)
public @interface DpGroupQueue {
}
