package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.*;
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


    @GetMapping("/viewProfile")
    public ResponseEntity<?> viewProfile(@RequestParam String token, CustomerResponseDTO customerResponseDTO){
        return customerService.viewProfile(token,customerResponseDTO);
    }

    @PatchMapping("/updateProfile")
    public ResponseEntity<?> updateCustomerProfile(@RequestParam String token, CustomerUpdateDTO customerUpdateDTO){
        return customerService.updateProfile(token,customerUpdateDTO);
    }

    @GetMapping("/viewAddress")
    public ResponseEntity<?> viewCustomerAddress(){
        return customerService.viewAddress();
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<?> updateCustomerPassword(@RequestParam String token, ResetPasswordDTO resetPasswordDTO){
        return customerService.updatePassword(token,resetPasswordDTO);
    }

    @PatchMapping("/updateAddress")
    public ResponseEntity<?> updateCustomerAddress(@RequestParam String token, AddressDTO addressDTO){
        return customerService.updateAddress(token,addressDTO);
    }
}
