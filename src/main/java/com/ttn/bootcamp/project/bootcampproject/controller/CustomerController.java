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
    public ResponseEntity<?> viewProfile(HttpServletRequest request, @RequestBody CustomerResponseDTO customerResponseDTO){
        String token = request.getHeader("Authorization").substring(7);
        return customerService.viewProfile(token,customerResponseDTO);
    }

    @PatchMapping("/updateProfile")
    public ResponseEntity<?> updateCustomerProfile(HttpServletRequest request,@RequestBody CustomerUpdateDTO customerUpdateDTO){
        String token=request.getHeader("Authorization").substring(7);
        return customerService.updateProfile(token,customerUpdateDTO);
    }

    @GetMapping("/viewAddress")
    public ResponseEntity<?> viewCustomerAddress(HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        return customerService.viewAddress(token);
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<?> updateCustomerPassword(HttpServletRequest request, @RequestBody ResetPasswordDTO resetPasswordDTO){
        String token = request.getHeader("Authorization").substring(7);
        return customerService.updatePassword(token,resetPasswordDTO);
    }

    @PatchMapping("/updateAddress")
    public ResponseEntity<?> updateCustomerAddress(HttpServletRequest request, @RequestBody AddressDTO addressDTO){
        String token = request.getHeader("Authorization").substring(7);
        return customerService.updateAddress(token,addressDTO);
    }

    @PostMapping("/addNewAddress")
    public ResponseEntity<?> addNewCustomerAddress(HttpServletRequest request, @RequestBody AddressDTO addressDTO) {
        String token = request.getHeader("Authorization").substring(7);
        return customerService.addNewAddress(token, addressDTO);
    }

    @DeleteMapping("/deleteAddress")
    public ResponseEntity<?> deleteCustomerAddress(HttpServletRequest request,@RequestParam Long id) {
        String token = request.getHeader("Authorization").substring(7);
        return customerService.deleteAddress(token,id);
    }
}
