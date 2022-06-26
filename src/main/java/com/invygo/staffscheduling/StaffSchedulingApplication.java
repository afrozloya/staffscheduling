package com.invygo.staffscheduling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class StaffSchedulingApplication {

	public static void main(String[] args) {
		SpringApplication.run(StaffSchedulingApplication.class, args);
	}

}
