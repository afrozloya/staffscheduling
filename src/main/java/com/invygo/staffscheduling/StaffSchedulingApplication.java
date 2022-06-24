package com.invygo.staffscheduling;

import com.invygo.staffscheduling.models.Role;
import com.invygo.staffscheduling.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class StaffSchedulingApplication {

	public static void main(String[] args) {
		SpringApplication.run(StaffSchedulingApplication.class, args);
	}

	@Bean
	CommandLineRunner init(RoleRepository roleRepository) {
		return args -> {
			//afroz go flyway
			String[] roles = {"ROLE_ADMIN", "ROLE_USER"};
			Arrays.stream(roles).forEach(roleName -> {
				Role role = roleRepository.findByRole(roleName);
				if (role == null) {
					Role newRole = new  Role(roleName);
					roleRepository.save(newRole);
				}
			});
		};
	}
}
