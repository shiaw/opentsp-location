package com.navinfo.opentsp.platform.dp.core.handler;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RP映射Command注解
 * User: zhanhk
 * Date: 16/9/7
 * Time: 下午3:17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RPAnno {

    String id() default "";

    String version() default "";
}
