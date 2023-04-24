package com.ttn.bootcamp.project.bootcampproject.Service;

import com.ttn.bootcamp.project.bootcampproject.DTO.SellerDTO;
import com.ttn.bootcamp.project.bootcampproject.Entities.UserModels.Customer;
import com.ttn.bootcamp.project.bootcampproject.Entities.UserModels.Seller;
import com.ttn.bootcamp.project.bootcampproject.Repositories.SellerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    @Autowired
    SellerRepo sellerRepo;

    @Autowired
    EmailService emailService;

    @Autowired
    JWTService jwtService;

    public ResponseEntity<?> createSeller(SellerDTO sellerDTO){
        Seller seller=new Seller();
        seller.setEmail(sellerDTO.getEmail());
        seller.setFirstName(sellerDTO.getFirstName());
        seller.setLastName(sellerDTO.getLastName());
        if(sellerDTO.getPassword().equals(sellerDTO.getConfirmPassword())){
            seller.setPassword(seller.getPassword());
        } else {
            return ResponseEntity.badRequest().body("Password doesn't match.");
        }
        seller.setCompanyContact(sellerDTO.getCompanyContact());
        seller.setCompanyName(sellerDTO.getCompanyName());
        //seller.setAddress(sellerDTO.getCompanyAddress());
        seller.setGst(sellerDTO.getGst());
        sellerRepo.save(seller);
        emailService.sendMail(seller.getEmail(),jwtService.generateToken(seller.getEmail()));

        return ResponseEntity.ok("Seller is registered");
    }

}
