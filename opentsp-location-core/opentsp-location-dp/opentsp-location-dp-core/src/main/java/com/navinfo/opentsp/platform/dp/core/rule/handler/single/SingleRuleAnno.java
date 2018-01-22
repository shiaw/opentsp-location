package com.navinfo.opentsp.platform.dp.core.rule.handler.single;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: zhanhk
 * Date: 16/10/21
 * Time: 上午9:29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface SingleRuleAnno {

    /**
     * 规则ID
     */
    int ruleId() default 0;
}
