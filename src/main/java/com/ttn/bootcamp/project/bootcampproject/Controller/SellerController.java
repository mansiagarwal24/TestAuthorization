package com.ttn.bootcamp.project.bootcampproject.Controller;

import com.ttn.bootcamp.project.bootcampproject.Entities.UserModels.Customer;
import com.ttn.bootcamp.project.bootcampproject.Entities.UserModels.Seller;
import com.ttn.bootcamp.project.bootcampproject.Service.CustomerService;
import com.ttn.bootcamp.project.bootcampproject.Service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String register(@RequestBody Seller seller){
        return sellerService.createSeller(seller);
    }
}
