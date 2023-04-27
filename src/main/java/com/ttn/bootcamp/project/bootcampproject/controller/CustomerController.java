package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.CustomerDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import com.ttn.bootcamp.project.bootcampproject.repository.CustomerRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import com.ttn.bootcamp.project.bootcampproject.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    JWTGenerator jwtGenerator;
    @Autowired
    UserRepo userRepo;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CustomerDTO customerDTO){
        if(customerRepo.existsByEmail(customerDTO.getEmail())){
            return new ResponseEntity<>("Email is already registered", HttpStatus.BAD_REQUEST);
        }
        if(!customerDTO.getPassword().equals(customerDTO.getConfirmPassword())) {
            return new ResponseEntity<>("Password doesn't match.", HttpStatus.BAD_REQUEST);
        }
        customerService.createCustomer(customerDTO);
        return new ResponseEntity<>("Register Successfully!!", HttpStatus.OK);
    }

    @PutMapping("/activate")
    public ResponseEntity<?> activateAccount(@RequestParam String token){
        boolean isActivate=customerService.activateCustomer(token);
        if(isActivate==true){
        return new ResponseEntity<>("Your Account has been activated",HttpStatus.OK);
        }
        return new ResponseEntity<>("Your token has expired or incorrect ",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String token){
        boolean isvalid =customerService.logoutCustomer(token);
        if(isvalid==true) {
            return new ResponseEntity<>("Account logout successfully!!", HttpStatus.OK);
        }
        return new ResponseEntity<>("You are not logged in.",HttpStatus.BAD_REQUEST);
    }


}
