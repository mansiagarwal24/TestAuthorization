package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.*;
import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import com.ttn.bootcamp.project.bootcampproject.repository.CustomerRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import com.ttn.bootcamp.project.bootcampproject.service.CategoryService;
import com.ttn.bootcamp.project.bootcampproject.service.CustomerService;
import com.ttn.bootcamp.project.bootcampproject.service.I18Service;
import com.ttn.bootcamp.project.bootcampproject.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
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
    public ResponseEntity<?> updateCustomerProfile(@Valid @ModelAttribute CustomerUpdateDTO customerUpdateDTO) throws IOException {
        customerService.updateProfile(customerUpdateDTO);
        return new ResponseEntity<>("Profile updated Successfully!!",HttpStatus.OK);
    }

    @GetMapping("/viewAddress")
    public ResponseEntity<?> viewCustomerAddress(){
        return new ResponseEntity<>(customerService.viewAddress(),HttpStatus.OK);
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<?> updateCustomerPassword( @Valid @RequestBody ResetPasswordDTO resetPasswordDTO){
        customerService.updatePassword(resetPasswordDTO);
        return new ResponseEntity<>("Password update Successfully!!",HttpStatus.OK);
    }

    @PatchMapping("/updateAddress")
    public ResponseEntity<?> updateCustomerAddress(@RequestParam Long id, @RequestBody AddressDTO addressDTO){
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
        customerService.deleteAddress(id);
        return new ResponseEntity<>("Address Deleted Successfully!!",HttpStatus.OK);
    }


    //CATEGORY API

    @GetMapping("/viewCategory")
    public ResponseEntity<?> viewCategoryById(@RequestParam Long id){
        return new ResponseEntity<>(categoryService.viewCategoryForCustomer(id),HttpStatus.OK);
    }


    //PRODUCT API
    @GetMapping("/viewProduct")
    public ResponseEntity<?> viewProductById(@RequestParam Long id){
        return new ResponseEntity<>(productService.viewProductByCustomer(id),HttpStatus.OK);
    }

    @GetMapping("/viewAllProduct")
    public ResponseEntity<?> viewAllProducts(@RequestParam Long id){
        return new ResponseEntity<>(productService.viewAllProductByCustomer(id),HttpStatus.OK);
    }

    @GetMapping("/viewAllSimilarProduct/{id}")
    public ResponseEntity<?> viewAllSimilarProducts(@PathVariable Long id,@RequestParam int offSet,@RequestParam int size,@RequestParam String orderBy,@RequestParam String sortBy){
        return new ResponseEntity<>(productService.viewSimilarCustomerProducts(id,size,offSet,orderBy,sortBy),HttpStatus.OK);
    }
}
