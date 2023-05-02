package com.ttn.bootcamp.project.bootcampproject.Bootstrap;

import com.ttn.bootcamp.project.bootcampproject.entity.user.Role;
import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import com.ttn.bootcamp.project.bootcampproject.enums.Authority;
import com.ttn.bootcamp.project.bootcampproject.repository.RoleRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class Bootstrap implements ApplicationListener {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    UserRepo userRepo;

    @Value("${admin.email}")
    String email;

    @Value("${admin.password}")
    CharSequence password;


    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent){
        if(!roleRepo.existsByAuthority(Authority.ADMIN)){
            Role role = new Role();
            role.setAuthority(Authority.ADMIN);
            roleRepo.save(role);
        }
        if(!roleRepo.existsByAuthority(Authority.CUSTOMER)){
            Role role = new Role();
            role.setAuthority(Authority.CUSTOMER);
            roleRepo.save(role);
        }

        if(!roleRepo.existsByAuthority(Authority.SELLER)){
            Role role = new Role();
            role.setAuthority(Authority.SELLER);
            roleRepo.save(role);
        }
        if(!userRepo.existsByEmail(email)){
            User user = new User();
            user.setEmail(email);
            user.setFirstName("Mansi");
            user.setLastName("Aggarwal");
            user.setPassword(passwordEncoder.encode(password));
            user.setActive(true);
            user.setPasswordUpdateDate(LocalDate.now());
            Optional<Role> byAuthority = roleRepo.findByAuthority(Authority.ADMIN);
            user.setRole(byAuthority.stream().toList());
            userRepo.save(user);
        }
    }
}
