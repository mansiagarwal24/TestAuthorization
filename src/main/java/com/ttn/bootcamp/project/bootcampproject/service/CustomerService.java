package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.CustomerDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.user.*;
import com.ttn.bootcamp.project.bootcampproject.enums.Authority;
import com.ttn.bootcamp.project.bootcampproject.repository.CustomerRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.RoleRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.TokenRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTAuthenticationFilter;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
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
        emailService.sendMail(customerDTO.getEmail(),"Activation Code ","Please Activate your account by clicking on the below link"+"\n http://localhost:8080/user/activate?token="+uuid);
    }

    public ResponseEntity<?> viewProfile(String token){
        if(jwtService.validateToken(token)){
            String email = jwtService.getEmailFromJWT(token);
            Customer customer = customerRepo.findByEmail(email).orElseThrow(()->{throw new RuntimeException("Email doesn't exist");});
//            customerRepo.getAll(customer);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("Token is invalid or expire!!", HttpStatus.UNAUTHORIZED);
    }


}
