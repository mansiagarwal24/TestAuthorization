package com.ttn.bootcamp.project.bootcampproject.Service;

import com.ttn.bootcamp.project.bootcampproject.Entities.UserModels.Customer;
import com.ttn.bootcamp.project.bootcampproject.Entities.UserModels.Seller;
import com.ttn.bootcamp.project.bootcampproject.Repositories.SellerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    @Autowired
    SellerRepo sellerRepo;
    public String createSeller(Seller seller){
        sellerRepo.save(seller);
        return "Seller is created";
    }

}
