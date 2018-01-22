package com.navinfo.opentsp.gateway.tcp.proto.location.procotol;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/7/11
 * @modify
 * @copyright opentsp
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface LocationCommand {

    String id() default "";

    String version() default "";
}
