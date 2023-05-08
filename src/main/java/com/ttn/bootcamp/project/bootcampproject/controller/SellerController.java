package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.AddressDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.ResetPasswordDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.SellerDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.SellerUpdateDTO;
import com.ttn.bootcamp.project.bootcampproject.repository.SellerRepo;
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
    public ResponseEntity<?> updateSellerPassword(@RequestBody ResetPasswordDTO resetPasswordDTO){
        return sellerService.updatePassword(resetPasswordDTO);
    }

    @PatchMapping("/updateAddress")
    public ResponseEntity<?> updateSellerAddress(@RequestBody AddressDTO addressDTO){
        return sellerService.updateAddress(addressDTO);
    }
}
