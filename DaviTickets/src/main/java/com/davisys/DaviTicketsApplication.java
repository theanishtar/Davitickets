package com.davisys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.davisys.controller.ErrorControllerHandle;

@SpringBootApplication
public class DaviTicketsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaviTicketsApplication.class, args);
	}
	
//	@Bean
//	public ErrorControllerHandle errorControllerHandle() {
//		return new ErrorControllerHandle();
//	}

}
