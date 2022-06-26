package com.invygo.staffscheduling.controller;

import com.invygo.staffscheduling.dto.LoginDTO;
import com.invygo.staffscheduling.jwt.JwtUtil;
import com.invygo.staffscheduling.repository.RoleRepository;
import com.invygo.staffscheduling.repository.UserRepository;
import com.invygo.staffscheduling.testutil.UnitTestUtils;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @InjectMocks
    AuthController authController;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtUtil jwtUtil;
    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder bCryptPasswordEncoder;

    @Test
    void testLoginSuccess() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("abc@gmail.com");
        loginDTO.setPassword("abc");
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(UnitTestUtils.getNewUser())); //exist can login
        ResponseEntity res = authController.login(loginDTO);
        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    void testLoginFail() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("abc@gmail.com");
        loginDTO.setPassword("abc");
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty()); //not exist can't login
        ResponseEntity res = authController.login(loginDTO);
        assertEquals(401, res.getStatusCode().value());
    }

    @Test
    void testRegisterSuccess() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("abc@gmail.com");
        loginDTO.setPassword("abc");
        Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty()); //not exist can register
        ResponseEntity res = authController.register(loginDTO);
        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    void testRegisterFail() {
        BadCredentialsException thrown = Assertions.assertThrows(BadCredentialsException.class, () -> {
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setEmail("abc@gmail.com");
            loginDTO.setPassword("abc");
            Mockito.when(userRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(UnitTestUtils.getNewUser())); //pretend already exists
            ResponseEntity res = authController.register(loginDTO);
        }, "BadCredentialsException was expected");
    }

}