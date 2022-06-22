package com.invygo.staffscheduling.Service;

import com.invygo.staffscheduling.dto.RegisterDTO;
import com.invygo.staffscheduling.models.Role;
import com.invygo.staffscheduling.models.User;
import com.invygo.staffscheduling.repository.RoleRepository;
import com.invygo.staffscheduling.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if(user!=null){
            List<GrantedAuthority> authorities = getUserAuthorities(user.getRoles());
            return buildAuthUserDetails(user, authorities);
        } else {
            throw new UsernameNotFoundException("Username not found!");
        }
    }

    private UserDetails buildAuthUserDetails(User user, List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

    private List<GrantedAuthority> getUserAuthorities(Set<Role> roles) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRole()));
        });
        return new ArrayList<>(authorities);
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(RegisterDTO userDTO) {
        User user = new User();
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.setEnabled(true);
        HashSet<Role> roles = new HashSet<>();
        userDTO.getRoles().forEach(role -> {
            Role userRole = roleRepository.findByRole(role);
            roles.add(userRole);
        });
        user.setRoles(new HashSet<>(roles));
        userRepository.save(user);
    }

}
