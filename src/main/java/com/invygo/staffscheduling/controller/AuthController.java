package com.invygo.staffscheduling.controller;

import com.invygo.staffscheduling.dto.LoginDTO;
import com.invygo.staffscheduling.jwt.JwtUtil;
import com.invygo.staffscheduling.misc.ScheduleConstants;
import com.invygo.staffscheduling.models.Role;
import com.invygo.staffscheduling.models.User;
import com.invygo.staffscheduling.repository.RoleRepository;
import com.invygo.staffscheduling.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder bCryptPasswordEncoder;

    @ApiOperation(value = "Login using credentials", notes = "Returns username and JWT token")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved"),
            @ApiResponse(code = 401, message = "Unauthorized - The user credentials incorrect")
    })
    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginDTO data) {
        String username = data.getEmail();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username, data.getPassword())
        );
        User user = this.userRepository.findByEmail(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = jwtUtil.createToken(user);
        Map<Object, Object> model = new HashMap<>();
        model.put("username", username);
        model.put("token", token);
        return ResponseEntity.ok(model);
    }

    @ApiOperation(value = "Signup using credentials", notes = "Returns a successful message")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User registered successfully"),
            @ApiResponse(code = 401, message = "Unauthorized - The user already exits")
    })
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid LoginDTO user) {
        User userExists = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (userExists != null) {
            throw new BadCredentialsException("User with username: " + user.getEmail() + " already exists");
        }
        User newUser = createUser(user);
        userRepository.save(newUser);
        Map<Object, Object> model = new HashMap<>();
        model.put("message", "User registered successfully");
        return ResponseEntity.ok(model);
    }

    private User createUser(LoginDTO user) {
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        newUser.setEnabled(true);
        HashSet<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByRole(ScheduleConstants.USER);
        roles.add(userRole);
        newUser.setRoles(new HashSet<>(roles));
        return newUser;
    }
}
