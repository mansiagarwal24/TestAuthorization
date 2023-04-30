package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.SellerDTO;
import com.ttn.bootcamp.project.bootcampproject.repository.SellerRepo;
import com.ttn.bootcamp.project.bootcampproject.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
