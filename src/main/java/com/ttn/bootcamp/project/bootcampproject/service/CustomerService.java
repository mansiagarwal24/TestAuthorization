package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.*;
import com.ttn.bootcamp.project.bootcampproject.entity.user.*;
import com.ttn.bootcamp.project.bootcampproject.enums.Authority;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.GenericMessageException;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.ResourcesNotFoundException;
import com.ttn.bootcamp.project.bootcampproject.repository.*;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class CustomerService {
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    AddressRepo addressRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    EmailService emailService;
    @Autowired
    JWTGenerator jwtService;
    @Autowired
    RoleRepo roleRepo;

    @Autowired
    I18Service i18Service;
//    @Autowired
//    ImageService imageService;
//    @Value("${basePath}")
//    String basePath;


    public void createCustomer(CustomerDTO customerDTO) {
        if(customerRepo.existsByEmail(customerDTO.getEmail())){
            log.error("User input email: " + customerDTO.getEmail() + " Email already registered!");
            throw new GenericMessageException("Email Already Registered!!");
        }
        if(!customerDTO.getPassword().equals(customerDTO.getConfirmPassword())) {
            throw new GenericMessageException("Password and confirm password should be same!!");
        }
        Customer customer = new Customer();
        customer.setEmail(customerDTO.getEmail());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPassword(encoder.encode(customerDTO.getPassword()));
        customer.setContact(customerDTO.getPhoneNo());

        Address address =new Address();
        address.setCity(customerDTO.getCity());
        address.setAddressLine(customerDTO.getAddressLine());
        address.setLabel(customerDTO.getLabel());
        address.setCountry(customerDTO.getCountry());
        address.setZipCode(customerDTO.getZipCode());
        address.setState(customerDTO.getState());
        address.setCustomer(customer);


        Role role = roleRepo.findByAuthority(Authority.CUSTOMER).orElse(null);
        customer.setRole(Collections.singletonList(role));
        String uuid = String.valueOf(UUID.randomUUID());
        customer.setRegistrationToken(uuid);

        userRepo.save(customer);
        addressRepo.save(address);
        emailService.sendMail(customerDTO.getEmail(), "Activation Code ", "Please Activate your account by clicking on the below link" + "\n http://localhost:8080/user/activate?token=" + uuid);
    }

    public CustomerResponseDTO viewProfile() {
        String email=SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found!!"));

        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
        customerResponseDTO.setLastName((customer.getLastName()));
        customerResponseDTO.setFirstName(customer.getFirstName());
        customerResponseDTO.setEmail(customer.getEmail());
        customerResponseDTO.setId(customer.getUserId());
        customerResponseDTO.setActive(customer.isActive());
        customerResponseDTO.setImage(customer.getProfileImage());

        return customerResponseDTO;

    }
    private String basePath="/home/mansi/Downloads/bootcamp-project/images/";

    public void updateProfile(CustomerUpdateDTO customerUpdateDTO) throws IOException {
        String email  = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepo.findByEmail(email).orElseThrow(()-> new ResourcesNotFoundException("User doesn't exist!!"));
        if(customerUpdateDTO.getFirstName()!=null){
            customer.setFirstName(customerUpdateDTO.getFirstName());
        }
        if(customerUpdateDTO.getLastName()!=null){
            customer.setLastName(customerUpdateDTO.getLastName());
        }
        if(customerUpdateDTO.getContactNo()!=null){
            customer.setContact(customerUpdateDTO.getContactNo());
        }
        if(customerUpdateDTO.getMiddleName()!=null){
            customer.setMiddleName(customerUpdateDTO.getMiddleName());
        }
        customer.setProfileImage(basePath+customerUpdateDTO.getImage().getOriginalFilename());
        customerRepo.save(customer);
        customerUpdateDTO.getImage().transferTo(new File(basePath+customerUpdateDTO.getImage().getOriginalFilename()));
    }

    public void updatePassword( ResetPasswordDTO resetPasswordDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!resetPasswordDTO.getPassword().equals(resetPasswordDTO.getConfirmPassword())){
            throw new GenericMessageException("Password and confirm password should be same!!");
        }
        Customer customer = customerRepo.findByEmail(email).orElseThrow(()-> new ResourcesNotFoundException("user doesn't exist!!"));
        customer.setPassword(encoder.encode(resetPasswordDTO.getPassword()));
        customer.setPasswordUpdateDate(LocalDate.now());
        customerRepo.save(customer);
        emailService.sendMail(customer.getEmail(), "Password Reset","Your password has been updated successfully");
    }

    public void updateAddress(Long id ,AddressDTO addressDTO){
        if(!addressRepo.existsById(id)){
            throw new GenericMessageException("Id doesn't exist!!");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepo.findByEmail(email).orElseThrow(()-> new ResourcesNotFoundException("User doesn't exist!!"));
        Address address = addressRepo.findByIdAndCustomer(id,customer).orElseThrow(()->new GenericMessageException("This id doesn't belong to customer!!"));
            if (addressDTO.getCity() != null) {
                address.setCity(addressDTO.getCity());
            }
            if (addressDTO.getAddressLine() != null) {
                address.setAddressLine(addressDTO.getAddressLine());
            }
            if (addressDTO.getCountry() != null) {
                address.setCountry(addressDTO.getCountry());
            }
            if (addressDTO.getState() != null) {
                address.setState(addressDTO.getState());
            }
            if (addressDTO.getZipCode() != 0) {
                address.setZipCode(addressDTO.getZipCode());
            }
            if (addressDTO.getLabel() != null) {
                address.setLabel(addressDTO.getLabel());
            }
            addressRepo.save(address);
    }

    public void addNewAddress(AddressDTO addressDTO){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer  = customerRepo.findByEmail(email).orElseThrow(()-> new ResourcesNotFoundException("User not found!!"));

        Address address = new Address();
        address.setAddressLine(addressDTO.getAddressLine());
        address.setLabel(addressDTO.getLabel());
        address.setZipCode(addressDTO.getZipCode());
        address.setCountry(addressDTO.getCountry());
        address.setState(addressDTO.getState());
        address.setCity(addressDTO.getCity());
        address.setCustomer(customer);

        addressRepo.save(address);
    }

    public List<Address> viewAddress(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepo.findByEmail(email).orElseThrow(()-> new ResourcesNotFoundException("user not found"));
        return customer.getAddressList();

    }


    public void deleteAddress(Long id){
        if(!addressRepo.existsById(id)){
            throw new GenericMessageException("Id not exist!!");
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepo.findByEmail(email).orElseThrow(()-> new ResourcesNotFoundException("user not found"));
        Address address = addressRepo.findByIdAndCustomer(id,customer).orElseThrow(()->new GenericMessageException("This id doesn't belong to customer!!"));
        address.setDelete(true);
        addressRepo.save(address);
    }


}
