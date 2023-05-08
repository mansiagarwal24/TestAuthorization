package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.*;
import com.ttn.bootcamp.project.bootcampproject.entity.user.*;
import com.ttn.bootcamp.project.bootcampproject.enums.Authority;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.ResourcesNotFoundException;
import com.ttn.bootcamp.project.bootcampproject.repository.AddressRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.RoleRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.SellerRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.TokenRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    RoleRepo roleRepo;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    I18Service i18Service;



    public ResponseEntity<?> createSeller(SellerDTO sellerDTO){
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

        return new ResponseEntity<>(i18Service.getMsg("seller.register"),HttpStatus.OK);

    }

    public ResponseEntity<?> viewProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("User doesn't exist!!"));

        SellerResponseDTO sellerResponseDTO =new SellerResponseDTO();
        sellerResponseDTO.setLastName((seller.getLastName()));
        sellerResponseDTO.setFirstName(seller.getFirstName());
        sellerResponseDTO.setEmail(seller.getEmail());
        sellerResponseDTO.setCompanyContact(seller.getCompanyContact());
        sellerResponseDTO.setId(seller.getUserId());
        sellerResponseDTO.setCompanyName(seller.getCompanyName());
        sellerResponseDTO.setActive(seller.isActive());
        return new ResponseEntity<>(sellerResponseDTO,HttpStatus.OK);

    }

    public ResponseEntity<?> updateProfile(SellerUpdateDTO sellerUpdateDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("User doesn't exist!!"));

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

    public ResponseEntity<?> updatePassword(ResetPasswordDTO resetPasswordDTO){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("user doesn't exist!!"));
        if(Objects.equals(resetPasswordDTO.getPassword(),resetPasswordDTO.getConfirmPassword())){
            seller.setPassword(encoder.encode(resetPasswordDTO.getPassword()));
            seller.setPasswordUpdateDate(LocalDate.now());
            sellerRepo.save(seller);
            emailService.sendMail(seller.getEmail(), "Password Reset","Your password has been updated successfully");
            return new ResponseEntity<>("Password Update Successfully!!",HttpStatus.OK);
        }
        return new ResponseEntity<>("Password and Confirm Password should be same",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> updateAddress(Long id,AddressDTO addressDTO){
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        Seller seller = sellerRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("User doesn't exist"));
//        Address address = addressRepo.findBySeller(seller).orElseThrow(()-> new RuntimeException("User doesn't found!!"));
        Address address = addressRepo.findById(id).orElseThrow(()-> new ResourcesNotFoundException("Address not found!!"));
        if(address.getSeller()==null){
            return new ResponseEntity<>("This id doesn't belong to seller!!",HttpStatus.BAD_REQUEST);
        }
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

}
