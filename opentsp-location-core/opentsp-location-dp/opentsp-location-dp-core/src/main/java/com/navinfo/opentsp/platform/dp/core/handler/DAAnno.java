package com.navinfo.opentsp.platform.dp.core.handler;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DA映射Command注解
 * User: yinsh
 * Date: 16/9/19
 * Time: 下午3:17
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface DAAnno {

    String id() default "";

    String version() default "";
}
