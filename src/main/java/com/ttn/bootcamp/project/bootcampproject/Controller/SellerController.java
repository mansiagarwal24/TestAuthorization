package com.ttn.bootcamp.project.bootcampproject.Controller;

import com.ttn.bootcamp.project.bootcampproject.DTO.SellerDTO;
import com.ttn.bootcamp.project.bootcampproject.Service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SellerDTO sellerDTO){
        return sellerService.createSeller(sellerDTO);
    }
}
