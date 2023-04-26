package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.CustomerDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Customer;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Role;
import com.ttn.bootcamp.project.bootcampproject.enums.Authority;
import com.ttn.bootcamp.project.bootcampproject.repository.CustomerRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.RoleRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

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

    public void createCustomer(CustomerDTO customerDTO){
        Customer customer=new Customer();
        customer.setEmail(customerDTO.getEmail());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPassword(encoder.encode(customerDTO.getPassword()));
        customer.setContact(customerDTO.getPhoneNo());
        Role role = roleRepo.findByAuthority(Authority.CUSTOMER).orElse(null);
        customer.setRole(Collections.singletonList(role));
        userRepo.save(customer);

        emailService.sendMail(customer.getEmail(),jwtService.generateToken(customer.getEmail()));
    }

//    public ResponseEntity<?> activateCustomer(String token){
//
//    }
}
