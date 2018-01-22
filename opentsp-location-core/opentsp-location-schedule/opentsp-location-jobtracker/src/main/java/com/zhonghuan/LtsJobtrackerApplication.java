package com.zhonghuan;

import com.github.ltsopensource.spring.boot.annotation.EnableJobTracker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @author
 */
@SpringBootApplication
@EnableJobTracker
public class LtsJobtrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LtsJobtrackerApplication.class, args);
	}
}
