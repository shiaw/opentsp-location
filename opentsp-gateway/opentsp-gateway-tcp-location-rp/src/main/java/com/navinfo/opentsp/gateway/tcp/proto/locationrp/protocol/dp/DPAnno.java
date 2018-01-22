package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface DPAnno {

    String id() default "";

    String version() default "";
}
