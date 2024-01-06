package com.accessauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@EnableEurekaClient
public class AccessAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccessAuthApplication.class, args);
	}

}
