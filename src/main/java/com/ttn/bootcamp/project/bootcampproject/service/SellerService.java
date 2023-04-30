package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.SellerDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Address;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Role;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Seller;
import com.ttn.bootcamp.project.bootcampproject.enums.Authority;
import com.ttn.bootcamp.project.bootcampproject.repository.RoleRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.SellerRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class SellerService {

    @Autowired
    SellerRepo sellerRepo;
    @Autowired
    EmailService emailService;
    @Autowired
    JWTGenerator jwtService;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    PasswordEncoder encoder;


    public void createSeller(SellerDTO sellerDTO){
        Seller seller=new Seller();
        seller.setEmail(sellerDTO.getEmail());
        seller.setFirstName(sellerDTO.getFirstName());
        seller.setLastName(sellerDTO.getLastName());
        seller.setPassword(encoder.encode(sellerDTO.getPassword()));
        seller.setCompanyContact(sellerDTO.getCompanyContact());
        seller.setCompanyName(sellerDTO.getCompanyName());
        seller.setGstNo(sellerDTO.getGstNO());
//        Address address =new Address();
//        address.setAddressLine(sellerDTO.);
//        seller.setAddress(sellerDTO.getCompanyAddress());
        seller.setGstNo(sellerDTO.getGstNO());
        Role role = roleRepo.findByAuthority(Authority.SELLER).orElse(null);
        seller.setRole(Collections.singletonList(role));
        String uuid = String.valueOf(UUID.randomUUID());
        seller.setToken(uuid);

        sellerRepo.save(seller);
        emailService.sendMail(sellerDTO.getEmail(),"Activation Code ","Please Activate your account by clicking on the below link"+"\n http://localhost:8080/user/activate?token="+uuid);

    }


//    public ResponseEntity<?> viewProfile(String token){
//        if(jwtService.validateToken(token)){
//            String email = jwtService.getEmailFromJWT(token);
//            Seller seller = sellerRepo.findByEmail(email).orElseThrow(()->{throw new RuntimeException("Email doesn't exist");});
//            sellerRepo.getAll(seller);
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Token is invalid or expire!!", HttpStatus.UNAUTHORIZED);
//    }

}
