package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.*;
import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import com.ttn.bootcamp.project.bootcampproject.repository.CustomerRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import com.ttn.bootcamp.project.bootcampproject.service.CustomerService;
import com.ttn.bootcamp.project.bootcampproject.service.I18Service;
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
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    I18Service i18Service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CustomerDTO customerDTO){
        customerService.createCustomer(customerDTO);
        return new ResponseEntity<>(i18Service.getMsg("customer.register"),HttpStatus.OK);
    }

    @GetMapping("/viewProfile")
    public ResponseEntity<?> viewProfile(){
        return new ResponseEntity<>(customerService.viewProfile(),HttpStatus.OK);
    }

    @PatchMapping("/updateProfile")
    public ResponseEntity<?> updateCustomerProfile(@RequestBody CustomerUpdateDTO customerUpdateDTO){
        customerService.updateProfile(customerUpdateDTO);
        return new ResponseEntity<>("Profile updated Successfully!!",HttpStatus.OK);
    }

    @GetMapping("/viewAddress")
    public ResponseEntity<?> viewCustomerAddress(){
        return new ResponseEntity<>(customerService.viewAddress(),HttpStatus.OK);
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<?> updateCustomerPassword( @Valid @RequestBody ResetPasswordDTO resetPasswordDTO){
        if(!resetPasswordDTO.getPassword().equals(resetPasswordDTO.getConfirmPassword())){
            return new ResponseEntity<>("Password should be same!!",HttpStatus.BAD_REQUEST);
        }
        customerService.updatePassword(resetPasswordDTO);
        return new ResponseEntity<>("Password update Successfully!!",HttpStatus.OK);
    }

    @PatchMapping("/updateAddress")
    public ResponseEntity<?> updateCustomerAddress(@RequestParam Long id, @RequestBody AddressDTO addressDTO){
        if(!customerService.checkAddressId(id)){
            return new ResponseEntity<>("This id doesn't belong to customer!!",HttpStatus.BAD_REQUEST);
        }
        customerService.updateAddress(id,addressDTO);
        return new ResponseEntity<>("Address Update Successfully!!",HttpStatus.OK);
    }

    @PostMapping("/addNewAddress")
    public ResponseEntity<?> addNewCustomerAddress(@RequestBody AddressDTO addressDTO) {
        customerService.addNewAddress(addressDTO);
        return new ResponseEntity<>("Address added Successfully!!",HttpStatus.OK);
    }

    @DeleteMapping("/deleteAddress")
    public ResponseEntity<?> deleteCustomerAddress(@RequestParam Long id) {
        if(!customerService.checkAddressId(id)){
            return new ResponseEntity<>("This Id does not belongs to Customer",HttpStatus.BAD_REQUEST);
        }
        customerService.deleteAddress(id);
        return new ResponseEntity<>("Address Deleted Successfully!!",HttpStatus.OK);
    }
}
