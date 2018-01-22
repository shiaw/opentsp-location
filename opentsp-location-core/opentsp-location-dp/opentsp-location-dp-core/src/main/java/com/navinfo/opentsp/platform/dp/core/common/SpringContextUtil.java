package com.navinfo.opentsp.platform.dp.core.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext;
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		SpringContextUtil.applicationContext = arg0;
	}

	public static ApplicationContext getApplicationContext() {
	return applicationContext;
	}
	public static Object getBean(String name) throws BeansException {
	return applicationContext.getBean(name);
	}


	public static Object getBean(String name, Class requiredType) throws BeansException {
	return applicationContext.getBean(name, requiredType);
	}


	public static boolean containsBean(String name) {
	return applicationContext.containsBean(name);
	}


	public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
	return applicationContext.isSingleton(name);
	}


	public static Class getType(String name) throws NoSuchBeanDefinitionException {
	return applicationContext.getType(name);
	}


	public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
	return applicationContext.getAliases(name);
	}

}
