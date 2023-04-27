package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.CustomerDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Customer;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Role;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Token;
import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import com.ttn.bootcamp.project.bootcampproject.enums.Authority;
import com.ttn.bootcamp.project.bootcampproject.repository.CustomerRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.RoleRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.TokenRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTAuthenticationFilter;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class CustomerService {
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    EmailService emailService;
    @Autowired
    JWTGenerator jwtService;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    JWTGenerator jwtGenerator;
    @Autowired
    JWTAuthenticationFilter authenticationFilter;
    @Autowired
    TokenRepo tokenRepo;

    public void createCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setEmail(customerDTO.getEmail());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPassword(encoder.encode(customerDTO.getPassword()));
        customer.setContact(customerDTO.getPhoneNo());
        Role role = roleRepo.findByAuthority(Authority.CUSTOMER).orElse(null);
        customer.setRole(Collections.singletonList(role));
        String uuid = String.valueOf(UUID.randomUUID());
        customer.setToken(uuid);
        userRepo.save(customer);
        emailService.sendMail(customerDTO.getEmail(),"Activation Code ","http://localhost:8080/customer/activate?token="+uuid);
    }

    public boolean activateCustomer(String token) {
//        boolean res = jwtGenerator.validateToken(token);
//        if (res = true) {
//            String email = jwtGenerator.getEmailFromJWT(token);
//            User user = userRepo.findByEmail(email).get();
//            user.setActive(true);
//            userRepo.save(user);
//            return true;
//        }
//        return false;
       User user = userRepo.findByToken(token).get();
       user.setActive(true);
       userRepo.save(user);
       return true;
    }

    public boolean logoutCustomer(String token){
        Token validToken = tokenRepo.findByToken(token).get();
        if(!validToken.getIsDeleted()){
            validToken.setIsDeleted(true);
            return true;
        }
        return false;
    }
}
