package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.*;
import com.ttn.bootcamp.project.bootcampproject.entity.user.*;
import com.ttn.bootcamp.project.bootcampproject.enums.Authority;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.GenericMessageException;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.ResourcesNotFoundException;
import com.ttn.bootcamp.project.bootcampproject.repository.AddressRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.RoleRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.SellerRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.TokenRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
//    @Autowired
//    ImageService imageService;

    private String basePath="/home/mansi/Downloads/bootcamp-project/images/";


    public void createSeller(SellerDTO sellerDTO){
        if(sellerRepo.existsByEmail(sellerDTO.getEmail())){
            throw new GenericMessageException("Email already exist!!");
        }
        if(!sellerDTO.getPassword().equals(sellerDTO.getConfirmPassword())) {
            throw new GenericMessageException("Password doesn't match!!");
        }
        if(sellerRepo.existsByCompanyName(sellerDTO.getCompanyName())){
            throw new GenericMessageException("Company Name is already registered with other seller");
        }
        if(sellerRepo.existsByGstNo(sellerDTO.getGstNO())){
            throw new GenericMessageException("GST Number is already registered");
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


        sellerRepo.save(seller);
        addressRepo.save(address);
        emailService.sendMail(sellerDTO.getEmail(),"Registration Successful ","Your account has been registered.we will update you once your account has been activated.");

    }

    public SellerResponseDTO viewProfile() {
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
        sellerResponseDTO.setImage(seller.getProfileImage());
        return sellerResponseDTO;

    }

//    private String basePath="/home/mansi/Downloads/bootcamp-project/images/";


    public void updateProfile(SellerUpdateDTO sellerUpdateDTO) throws IOException {
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

        seller.setProfileImage(basePath+seller.getUserId()+".jpg");
        sellerRepo.save(seller);
        sellerUpdateDTO.getImage().transferTo(new File(basePath+sellerUpdateDTO.getImage().getOriginalFilename()));
        sellerRepo.save(seller);
    }

    public void updatePassword(ResetPasswordDTO resetPasswordDTO){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("user doesn't exist!!"));
        if(Objects.equals(resetPasswordDTO.getPassword(),resetPasswordDTO.getConfirmPassword())){
            seller.setPassword(encoder.encode(resetPasswordDTO.getPassword()));
            seller.setPasswordUpdateDate(LocalDate.now());
            sellerRepo.save(seller);
            emailService.sendMail(seller.getEmail(), "Password Reset","Your password has been updated successfully");
        }
        throw  new GenericMessageException("Password and Confirm Password should be same");
    }

    public void updateAddress(Long id,AddressDTO addressDTO){
        if(!addressRepo.existsById(id)){
            throw new GenericMessageException("Id doesn't exist!!");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Seller seller = sellerRepo.findByEmail(email).orElseThrow(()-> new ResourcesNotFoundException("User doesn't exist!!"));
        Address address = addressRepo.findByIdAndSeller(id,seller).orElseThrow(()->new GenericMessageException("This id doesn't belong to seller!!"));

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

    }

}
