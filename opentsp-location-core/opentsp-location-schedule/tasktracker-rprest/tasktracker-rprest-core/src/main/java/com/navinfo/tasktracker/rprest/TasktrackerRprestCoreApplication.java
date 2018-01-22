package com.navinfo.tasktracker.rprest;

import com.github.ltsopensource.spring.boot.annotation.EnableTaskTracker;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableTaskTracker
@MapperScan("com.navinfo.tasktracker.rprest.dao")

public class TasktrackerRprestCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(TasktrackerRprestCoreApplication.class, args);
	}
}
