package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.AddressDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.ResetPasswordDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.SellerDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.SellerUpdateDTO;
import com.ttn.bootcamp.project.bootcampproject.repository.SellerRepo;
import com.ttn.bootcamp.project.bootcampproject.service.SellerService;
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
    SellerRepo sellerRepo;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid SellerDTO sellerDTO){
        if(sellerRepo.existsByEmail(sellerDTO.getEmail())){
            return new ResponseEntity<>("Email is already registered", HttpStatus.BAD_REQUEST);
        }
        if(!sellerDTO.getPassword().equals(sellerDTO.getConfirmPassword())) {
            return new ResponseEntity<>("Password doesn't match.", HttpStatus.BAD_REQUEST);
        }
        if(sellerRepo.existsByCompanyName(sellerDTO.getCompanyName())){
            return new ResponseEntity<>("Company Name is already registered with other seller",HttpStatus.BAD_REQUEST);
        }
        if(sellerRepo.existsByGstNo(sellerDTO.getGstNO())){
            return new ResponseEntity<>("GST Number is already registered",HttpStatus.BAD_REQUEST);
        }
        sellerService.createSeller(sellerDTO);
        return new ResponseEntity<>("Register Successfully!!", HttpStatus.OK);
    }

//    @GetMapping("/viewProfile")
//    public ResponseEntity<?> findOne(@RequestParam String token){
//        return sellerService.viewProfile(token);
//    }

    @PatchMapping("/updateProfile")
    public ResponseEntity<?> updateSellerProfile(@RequestParam String token,@RequestBody SellerUpdateDTO sellerUpdateDTO){
        return sellerService.updateProfile(token,sellerUpdateDTO);
    }

    @PatchMapping("/updatePassword")
    public ResponseEntity<?> updateSellerPassword(@RequestParam String token, @RequestBody ResetPasswordDTO resetPasswordDTO){
        return sellerService.updatePassword(token,resetPasswordDTO);
    }

    @PatchMapping("/updateAddress")
    public ResponseEntity<?> updateSellerAddress(@RequestParam String token, @RequestBody AddressDTO addressDTO){
        return sellerService.updateAddress(token,addressDTO);
    }
}
