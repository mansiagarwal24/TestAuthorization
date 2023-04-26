package com.ttn.bootcamp.project.bootcampproject.security;

import com.ttn.bootcamp.project.bootcampproject.entity.user.Role;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.ttn.bootcamp.project.bootcampproject.entity.user.User user = userRepo.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Email not Found"));
        return new User(user.getEmail(),user.getPassword(),mapRolesToAuthority(user.getRole()));
    }

    public Collection<GrantedAuthority> mapRolesToAuthority(List<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority().toString())).collect(Collectors.toList());
    }

}
