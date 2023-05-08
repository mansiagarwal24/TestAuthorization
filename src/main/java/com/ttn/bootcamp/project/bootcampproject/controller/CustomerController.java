package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.*;
import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import com.ttn.bootcamp.project.bootcampproject.repository.CustomerRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import com.ttn.bootcamp.project.bootcampproject.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CustomerDTO customerDTO){
        return customerService.createCustomer(customerDTO);
    }

    @GetMapping("/viewProfile")
    public ResponseEntity<?> viewProfile(){
        return customerService.viewProfile();
    }

    @PatchMapping("/updateProfile")
    public ResponseEntity<?> updateCustomerProfile(@RequestBody CustomerUpdateDTO customerUpdateDTO){
        return customerService.updateProfile(customerUpdateDTO);
    }

    @GetMapping("/viewAddress")
    public ResponseEntity<?> viewCustomerAddress(){
        return customerService.viewAddress();
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<?> updateCustomerPassword( @RequestBody ResetPasswordDTO resetPasswordDTO){
        return customerService.updatePassword(resetPasswordDTO);
    }

    @PatchMapping("/updateAddress")
    public ResponseEntity<?> updateCustomerAddress(@RequestParam Long id, @RequestBody AddressDTO addressDTO){
        return customerService.updateAddress(id,addressDTO);
    }

    @PostMapping("/addNewAddress")
    public ResponseEntity<?> addNewCustomerAddress(@RequestBody AddressDTO addressDTO) {
        return customerService.addNewAddress(addressDTO);
    }

    @DeleteMapping("/deleteAddress")
    public ResponseEntity<?> deleteCustomerAddress(@RequestParam Long id) {
        return customerService.deleteAddress(id);
    }
}
