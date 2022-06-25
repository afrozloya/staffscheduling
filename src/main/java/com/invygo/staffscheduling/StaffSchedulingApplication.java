package com.invygo.staffscheduling;

import com.invygo.staffscheduling.misc.ScheduleConstants;
import com.invygo.staffscheduling.models.Role;
import com.invygo.staffscheduling.models.User;
import com.invygo.staffscheduling.repository.RoleRepository;
import com.invygo.staffscheduling.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class StaffSchedulingApplication {

	public static void main(String[] args) {
		SpringApplication.run(StaffSchedulingApplication.class, args);
	}

	@Bean
	CommandLineRunner init(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder encoder) {
		return args -> {
			//afroz go flyway
			String[] roles = ScheduleConstants.ROLES;
			Arrays.stream(roles).forEach(roleName -> {
				Role role = roleRepository.findByRole(roleName);
				if (role == null) {
					Role newRole = new  Role(roleName);
					roleRepository.save(newRole);
				}
			});

			final String ADMIN_USER = "afrozml@gmail.com";
			User user = userRepository.findByEmail(ADMIN_USER).orElse(null);
			if (user == null) {
				User userNew = new User();
				userNew.setEnabled(true);
				userNew.setEmail(ADMIN_USER);
				userNew.setFullName(ADMIN_USER);
				userNew.setPassword(encoder.encode("abc123")); //afroz encode in one place
				Set<Role> roleSet = new HashSet<>();
				Role role = roleRepository.findByRole(ScheduleConstants.ADMIN);
				roleSet.add(role);
				role = roleRepository.findByRole(ScheduleConstants.USER);
				roleSet.add(role);
				userNew.setRoles(roleSet);
				userRepository.save(userNew);
			}
		};
	}
}
