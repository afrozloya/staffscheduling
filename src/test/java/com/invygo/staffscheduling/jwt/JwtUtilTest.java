package com.invygo.staffscheduling.jwt;

import com.invygo.staffscheduling.models.Role;
import com.invygo.staffscheduling.models.User;
import com.invygo.staffscheduling.repository.RoleRepository;
import com.invygo.staffscheduling.testutil.UnitTestUtils;
import io.jsonwebtoken.Claims;
import org.bson.Document;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class JwtUtilTest {

    @InjectMocks
    JwtUtil jwtUtil;
    @Mock
    RoleRepository roleRepository;

    @BeforeEach
    public void setUp() throws Exception {
        jwtUtil.setSECRET_KEY("secret");
    }

    @Test
    void createToken() {
        String token = jwtUtil.createToken(UnitTestUtils.getNewUser());
        assertThat(token).isNotNull();
    }

    @Test
    void validateAccessTokenValid() {
        String token = jwtUtil.createToken(UnitTestUtils.getNewUser());
        boolean res = jwtUtil.validateAccessToken(token);
        assertEquals(true, res);
    }

    @Test
    void validateAccessTokenInValid() {
        boolean res = jwtUtil.validateAccessToken("ka");
        assertEquals(false, res);
    }

    @Test
    void validateAccessTokenBlank() {
        boolean res = jwtUtil.validateAccessToken("");
        assertEquals(false, res);
    }

    @Test
    void parseClaims() {
        String token = jwtUtil.createToken(UnitTestUtils.getNewUser());
        Claims claims = jwtUtil.parseClaims(token);
        assertThat(claims.getSubject()).isNotNull();
    }

    @Test
    void getUserDetails() {
        User user = UnitTestUtils.getNewUser();
        Mockito.when(roleRepository.findByRole(Mockito.any()))
                .thenReturn(new Role("ADMIN"));
        String token = jwtUtil.createToken(user);
        UserDetails details = jwtUtil.getUserDetails(token);
        assertEquals(details.getUsername(), user.getUsername());
    }

}