package com.navinfo;


import com.github.ltsopensource.spring.boot.annotation.EnableJobClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @author
 */
@SpringBootApplication
@EnableJobClient
public class OpentspQqiLocationJobApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(OpentspQqiLocationJobApplication.class, args);
	}
}
