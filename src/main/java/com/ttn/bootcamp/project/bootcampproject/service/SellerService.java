package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.AddressDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.ResetPasswordDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.SellerDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.SellerUpdateDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Address;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Role;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Seller;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Token;
import com.ttn.bootcamp.project.bootcampproject.enums.Authority;
import com.ttn.bootcamp.project.bootcampproject.repository.AddressRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.RoleRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.SellerRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.TokenRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@Service
public class SellerService {

    @Autowired
    SellerRepo sellerRepo;
    @Autowired
    AddressRepo addressRepo;
    @Autowired
    EmailService emailService;
    @Autowired
    JWTGenerator jwtService;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    TokenRepo tokenRepo;


    public void createSeller(SellerDTO sellerDTO){
        Seller seller=new Seller();
        seller.setEmail(sellerDTO.getEmail());
        seller.setFirstName(sellerDTO.getFirstName());
        seller.setLastName(sellerDTO.getLastName());
        seller.setPassword(encoder.encode(sellerDTO.getPassword()));
        seller.setCompanyContact(sellerDTO.getCompanyContact());
        seller.setCompanyName(sellerDTO.getCompanyName());
        seller.setGstNo(sellerDTO.getGstNO());

        Address address =new Address();
        address.setCity(sellerDTO.getCity());
        address.setAddressLine(sellerDTO.getAddressLine());
        address.setLabel(sellerDTO.getLabel());
        address.setCountry(sellerDTO.getCountry());
        address.setZipCode(sellerDTO.getZipCode());
        address.setState(sellerDTO.getState());
        address.setSeller(seller);

        Role role = roleRepo.findByAuthority(Authority.SELLER).orElse(null);
        seller.setRole(Collections.singletonList(role));
        String uuid = String.valueOf(UUID.randomUUID());
        seller.setToken(uuid);

        sellerRepo.save(seller);
        addressRepo.save(address);
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

    public ResponseEntity<?> updateProfile(String token,SellerUpdateDTO sellerUpdateDTO) {
        if(jwtService.validateToken(token)){
            Token accessToken = tokenRepo.findByToken(token).orElseThrow(()->{throw new RuntimeException("Token not found!!");});
            if(accessToken.isDelete()==true){
                return new ResponseEntity<>("your token is expired or incorrect",HttpStatus.UNAUTHORIZED);
            }
            String email  = jwtService.getEmailFromJWT(token);
            Seller seller = sellerRepo.findByEmail(email).orElseThrow(()->{throw new RuntimeException("User doesn't exist!!");});

            if(sellerUpdateDTO.getFirstName()!=null){
                seller.setFirstName(sellerUpdateDTO.getFirstName());
            }
            if(sellerUpdateDTO.getLastName()!=null){
                seller.setLastName(sellerUpdateDTO.getLastName());
            }
            if(sellerUpdateDTO.getCompanyContact()!=null){
                seller.setCompanyContact(sellerUpdateDTO.getCompanyContact());
            }
            if(sellerUpdateDTO.getMiddleName()!=null){
                seller.setMiddleName(sellerUpdateDTO.getMiddleName());
            }
            if(sellerUpdateDTO.getCompanyName()!=null){
                seller.setCompanyName(sellerUpdateDTO.getCompanyName());
            }
            sellerRepo.save(seller);

            return new ResponseEntity<>("Update Successfully!!",HttpStatus.OK);

        }
        return new ResponseEntity<>("Token is not valid or expired!!",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> updatePassword(String token, ResetPasswordDTO resetPasswordDTO){
        if(jwtService.validateToken(token)){
            String email = jwtService.getEmailFromJWT(token);
            Seller seller = sellerRepo.findByEmail(email).orElseThrow(()->{throw new RuntimeException("user doesn't exist!!");});
            if(Objects.equals(resetPasswordDTO.getPassword(),resetPasswordDTO.getConfirmPassword())){
                seller.setPassword(encoder.encode(resetPasswordDTO.getPassword()));
                sellerRepo.save(seller);
                emailService.sendMail(seller.getEmail(), "Password Reset","Your password has been updated successfully");

                return new ResponseEntity<>("Password Update Successfully!!",HttpStatus.OK);
            }
            return new ResponseEntity<>("Password and Confirm Password should be same",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Token is not valid or expire",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> updateAddress(String token, AddressDTO addressDTO){
        if(jwtService.validateToken(token)){
            String email = jwtService.getEmailFromJWT(token);
            Seller seller = sellerRepo.findByEmail(email).orElseThrow(()->{throw new RuntimeException("User doesn't exist");});
            Address address = addressRepo.findBySeller(seller).orElseThrow(()->{throw new RuntimeException("User doesn't found!!");});

            if(addressDTO.getCity()!=null){
                address.setCity(addressDTO.getCity());
            }
            if(addressDTO.getAddressLine()!=null){
                address.setAddressLine(addressDTO.getAddressLine());
            }
            if(addressDTO.getCountry()!=null){
                address.setCountry(addressDTO.getCountry());
            }
            if(addressDTO.getState()!=null){
                address.setState(addressDTO.getState());
            }
            if(addressDTO.getZipCode()!=0){
                address.setZipCode(addressDTO.getZipCode());
            }
            if(addressDTO.getLabel()!=null){
                address.setLabel(addressDTO.getLabel());
            }
            addressRepo.save(address);
            return new ResponseEntity<>("Address Updated Successfully!!",HttpStatus.OK);
        }
        return new ResponseEntity<>("Token is invalid or expire!!",HttpStatus.BAD_REQUEST);
    }

}
