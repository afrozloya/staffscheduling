package com.invygo.staffscheduling.controller;

import com.invygo.staffscheduling.dto.UserUpdateReqDTO;
import com.invygo.staffscheduling.models.User;
import com.invygo.staffscheduling.repository.RoleRepository;
import com.invygo.staffscheduling.repository.UserRepository;
import com.invygo.staffscheduling.testutil.UnitTestUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder bCryptPasswordEncoder;

    @Test
    void testFindAllUsers() {
        Iterable<User> users = userController.findAllUsers();
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testFindUserById() {
        Optional<User> user = userController.findUserById("1");
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    void testDelete() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(UnitTestUtils.getNewUser()));
        ResponseEntity response = userController.delete("1");
        Mockito.verify(userRepository, Mockito.times(1)).delete(Mockito.any());
    }

    @Test
    void testUpdateAllField() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(UnitTestUtils.getNewUser()));
        UserUpdateReqDTO updateReq = new UserUpdateReqDTO();
        updateReq.setFullName("kk");
        updateReq.setPassword("kk");
        Set<String> roleSet = new HashSet<>();
        roleSet.add("ADMIN");
        updateReq.setRoles(roleSet);
        User response = userController.update("1", updateReq);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void testUpdateNone() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.of(UnitTestUtils.getNewUser()));
        User response = userController.update("1", new UserUpdateReqDTO());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void testUpdateNotExist() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        User response = userController.update("1", new UserUpdateReqDTO());
        Mockito.verify(userRepository, Mockito.times(0)).save(Mockito.any());
    }

}