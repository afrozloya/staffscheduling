package com.invygo.staffscheduling.misc;

import com.invygo.staffscheduling.models.Role;
import com.invygo.staffscheduling.models.User;
import com.invygo.staffscheduling.repository.RoleRepository;
import com.invygo.staffscheduling.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class PrePopulateData implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        {
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
        }
    }
}
