package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.SellerDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Seller;
import com.ttn.bootcamp.project.bootcampproject.repository.SellerRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    @Autowired
    SellerRepo sellerRepo;
    @Autowired
    EmailService emailService;
    @Autowired
    JWTGenerator jwtService;


    public void createSeller(SellerDTO sellerDTO){
        Seller seller=new Seller();
        seller.setEmail(sellerDTO.getEmail());
        seller.setFirstName(sellerDTO.getFirstName());
        seller.setLastName(sellerDTO.getLastName());
        seller.setPassword(sellerDTO.getPassword());
        seller.setCompanyContact(sellerDTO.getCompanyContact());
        seller.setCompanyName(sellerDTO.getCompanyName());
        //seller.setAddress(sellerDTO.getCompanyAddress());
        seller.setGst(sellerDTO.getGst());

        sellerRepo.save(seller);

        emailService.sendMail(seller.getEmail(),jwtService.generateToken(seller.getEmail()));

    }

}
