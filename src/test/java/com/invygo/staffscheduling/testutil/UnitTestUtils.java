package com.invygo.staffscheduling.testutil;

import com.invygo.staffscheduling.models.Role;
import com.invygo.staffscheduling.models.Schedule;
import com.invygo.staffscheduling.models.User;

import java.util.HashSet;
import java.util.Set;

public class UnitTestUtils {

        public static User getNewUser() {
        User user = new User();
        user.setFullName("abc");
        user.setEmail("abc@gmail.com");
        user.setPassword("abc");
        user.setId("123");
        user.setEnabled(true);
        user.setRoles(getRoles());
        return user;
    }

    private static Set<Role> getRoles() {
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(getRole("ADMIN"));
        roleSet.add(getRole("USER"));
        return roleSet;
    }

    private static Role getRole(String name) {
        Role role = new Role(name);
        return role;
    }

    public static Schedule getNewSchedule() {
        Schedule schedule = new Schedule();
        return schedule;
    }

}
