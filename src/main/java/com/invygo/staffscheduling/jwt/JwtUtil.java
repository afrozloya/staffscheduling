package com.invygo.staffscheduling.jwt;

import com.invygo.staffscheduling.models.Role;
import com.invygo.staffscheduling.models.User;
import com.invygo.staffscheduling.repository.RoleRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class); //afroz

    @Value("${security.jwt.token.expire-length:3600}")
    private long EXPIRE_DURATION = 3600;

    @Setter
    @Value("${security.jwt.token.secret-key:secret}")
    private String SECRET_KEY;

    @Autowired
    RoleRepository roleRepository;

    public String createToken(User user) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", user.getId(), user.getEmail()))
                .setIssuer("staffscheduling")
                .claim("roles",
                        user.getRoles().stream().map(Role::getRole).collect(Collectors.toList()).toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION*1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            LOGGER.error("JWT expired", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Token is blank", ex.getMessage());
        } catch (JwtException ex) {
            LOGGER.error("Invalid JWT", ex);
        }
        return false;
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public UserDetails getUserDetails(String token) {
        User userDetails = new User();
        Claims claims = parseClaims(token);
        String subject = (String) claims.get(Claims.SUBJECT);
        String[] jwtSubject = subject.split(",");
        String roles = (String) claims.get("roles");
        roles = roles.replace("[", "").replace("]", "");
        String[] roleNames = roles.split(",");
        Set<Role> roleSet = new HashSet<>();
        for (String aRoleName : roleNames) {
            roleSet.add(roleRepository.findByRole(aRoleName.trim()));
        }
        userDetails.setRoles(roleSet);
        userDetails.setId(jwtSubject[0]);
        userDetails.setEmail(jwtSubject[1]);
        return userDetails;
    }

}
