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

//    @GetMapping("/viewProfile")
//    public ResponseEntity<?> findOne(@RequestParam String token){
//        return sellerService.viewProfile(token);
//    }

    @PatchMapping("/updateProfile")
    public ResponseEntity<?> updateSellerProfile(HttpServletRequest request, @RequestBody SellerUpdateDTO sellerUpdateDTO){
        String token = request.getHeader("Authorization").substring(7);
        return sellerService.updateProfile(token,sellerUpdateDTO);
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<?> updateSellerPassword(HttpServletRequest request,@RequestBody ResetPasswordDTO resetPasswordDTO){
        String token = request.getHeader("Authorization").substring(7);
        return sellerService.updatePassword(token,resetPasswordDTO);
    }

    @PatchMapping("/updateAddress")
    public ResponseEntity<?> updateSellerAddress(HttpServletRequest request,@RequestBody AddressDTO addressDTO){
        String token= request.getHeader("Authorization").substring(7);
        return sellerService.updateAddress(token,addressDTO);
    }
}
