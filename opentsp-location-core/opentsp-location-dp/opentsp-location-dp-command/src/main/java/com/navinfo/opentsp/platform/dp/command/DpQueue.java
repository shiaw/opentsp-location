package com.navinfo.opentsp.platform.dp.command;

import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;
import org.springframework.security.access.annotation.Secured;

import java.lang.annotation.*;

/**
 * User: zhanhk
 * Date: 16/9/5
 * Time: 下午6:08
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Secured("ROLE_USER")
@MessageGroup("dpqueue")
public @interface DpQueue {
}
