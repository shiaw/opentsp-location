package com.navinfo.opentsp.platform.da.core.rmi;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DA映射Command注解
 * User: wzw
 * 2016年9月20日11:46:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface DaRmiNo {

    String id() default "";

    String version() default "";
}