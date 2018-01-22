package com.navinfo.opentsp.platform.da.core.persistence.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 给mongo实体类添加注解
 *
 * @author jin_s
 *
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LCDbIndex {

	/**
	 * @return 数据库表名
	 */
	String[] name();
}
