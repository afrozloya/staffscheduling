package com.invygo.staffscheduling.controller;

import com.invygo.staffscheduling.dto.UserUpdateReqDTO;
import com.invygo.staffscheduling.models.Role;
import com.invygo.staffscheduling.models.User;
import com.invygo.staffscheduling.repository.RoleRepository;
import com.invygo.staffscheduling.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RolesAllowed("ADMIN")
@RequestMapping("/api/admin")
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder bCryptPasswordEncoder;
    @ApiOperation(value = "Get all users", notes = "Returns list of users")
    @GetMapping("/users")
    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    @ApiOperation(value = "Get user by id", notes = "Returns one user based on id provided")
    @GetMapping("/users/{id}")
    public Optional<User> findUserById(@PathVariable String id) {
        return userRepository.findById(id);
    }

    @ApiOperation(value = "Delete user by id", notes = "Deletes one user based on id provided")
    @DeleteMapping("/users/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        userRepository.delete(user.get());
        Map<Object, Object> model = new HashMap<>();
        model.put("message", "User deleted successfully");
        return ResponseEntity.ok(model);
    }

    @ApiOperation(value = "Update user by id", notes = "Returns updated user information")
    @PutMapping("/users/{id}")
    public User update(@PathVariable String id, @RequestBody UserUpdateReqDTO updateReqDTO) {
        User user = userRepository.findById(id).orElse(null);
        if(user !=null){
            if (updateReqDTO.getPassword() != null)
                user.setPassword(bCryptPasswordEncoder.encode(updateReqDTO.getPassword()));
            if (updateReqDTO.getFullName() != null)
                user.setFullName(updateReqDTO.getFullName());
            if (updateReqDTO.getRoles() != null){
                HashSet<Role> roles = new HashSet<>();
                updateReqDTO.getRoles().stream().forEach(role -> {
                    Role newRole = roleRepository.findByRole(role);
                    roles.add(newRole);
                });
                user.setRoles(roles);
            }
            userRepository.save(user);
        }
        return user;
    }

}
