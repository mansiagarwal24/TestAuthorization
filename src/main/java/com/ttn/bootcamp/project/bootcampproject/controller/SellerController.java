package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.*;
import com.ttn.bootcamp.project.bootcampproject.repository.SellerRepo;
import com.ttn.bootcamp.project.bootcampproject.service.ProductService;
import com.ttn.bootcamp.project.bootcampproject.service.SellerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    SellerService sellerService;
    @Autowired
    ProductService productService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid SellerDTO sellerDTO){
        return sellerService.createSeller(sellerDTO);
    }

    @GetMapping("/viewProfile")
    public ResponseEntity<?> viewSellerProfile(){
        return sellerService.viewProfile();
    }

    @PatchMapping("/updateProfile")
    public ResponseEntity<?> updateSellerProfile(@RequestBody SellerUpdateDTO sellerUpdateDTO){
        return sellerService.updateProfile(sellerUpdateDTO);
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<?> updateSellerPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO){
        return sellerService.updatePassword(resetPasswordDTO);
    }

    @PatchMapping("/updateAddress")
    public ResponseEntity<?> updateSellerAddress(@RequestParam Long id,@RequestBody AddressDTO addressDTO){
        return sellerService.updateAddress(id,addressDTO);
    }

    //PRODUCT API

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO){
        productService.addProduct(productDTO);
        return new ResponseEntity<>("Product added Successfully!!",HttpStatus.OK);
    }

    @GetMapping("/viewProduct")
    public ResponseEntity<?> viewProduct(@RequestParam Long productId){
        return new ResponseEntity<>(productService.viewProduct(productId),HttpStatus.OK);
    }

    @GetMapping("/viewAllProduct")
    public ResponseEntity<?> viewAllProduct(){
        return new ResponseEntity<>(productService.viewAllProducts(),HttpStatus.OK);
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<?> deleteProduct(@RequestParam Long id){
        productService.deleteProduct(id);
        return new ResponseEntity<>("Product deleted Successfully!!",HttpStatus.OK);
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<?> deleteProduct(@RequestParam Long id,@RequestBody ProductUpdateDTO productUpdateDTO){
        productService.updateProduct(id,productUpdateDTO);
        return new ResponseEntity<>("Product updated Successfully!!",HttpStatus.OK);
    }

}
