package com.invygo.staffscheduling;

import com.invygo.staffscheduling.models.User;
import com.invygo.staffscheduling.testutil.UnitTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@Configuration
public class TestConfig {
//    @Bean
//    public UserDetailsService userDetailsService(){
//        User user = UnitTestUtils.getNewUser();
//        user.setEmail("abc@gmail.com");
//        user.setFullName("abc@gmail.com");
//        return new InMemoryUserDetailsManager(Arrays.asList(user));
//    }
}
