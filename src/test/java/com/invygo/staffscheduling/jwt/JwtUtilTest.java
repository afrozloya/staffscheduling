package com.invygo.staffscheduling.jwt;

import com.invygo.staffscheduling.models.User;
import com.invygo.staffscheduling.testutil.UnitTestUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class JwtUtilTest {

    @Autowired
    JwtUtil jwtUtil;
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
        String token = jwtUtil.createToken(user);
        UserDetails details = jwtUtil.getUserDetails(token);
        assertEquals(details.getUsername(), user.getUsername());
    }

}