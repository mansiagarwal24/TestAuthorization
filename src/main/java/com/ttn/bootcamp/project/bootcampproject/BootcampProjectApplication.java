package com.ttn.bootcamp.project.bootcampproject;

import com.ttn.bootcamp.project.bootcampproject.service.AuditService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableJpaAuditing
public class BootcampProjectApplication {

	@Bean
	public AuditorAware<String> auditorAware(){
		return new AuditService();
	}
	public static void main(String[] args) {
		SpringApplication.run(BootcampProjectApplication.class, args);
	}

}
