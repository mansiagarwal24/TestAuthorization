package com.ttn.bootcamp.project.bootcampproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class BootcampProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(BootcampProjectApplication.class, args);
	}

}
