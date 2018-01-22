package com.navinfo.tasktracker.nilocation;

import com.github.ltsopensource.spring.boot.annotation.EnableTaskTracker;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication(exclude = MybatisAutoConfiguration.class)
@EnableTaskTracker
@EnableAutoConfiguration
@MapperScan("com.navinfo.tasktracker.nilocation.mapper")

/**
 *@author zhangyue
 */
public class TasktrackerQqiLocationCoreApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(TasktrackerQqiLocationCoreApplication.class);
		application.setWebEnvironment(false);
		application.run(args);
	}
}
