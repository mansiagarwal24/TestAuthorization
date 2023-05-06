package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.*;
import com.ttn.bootcamp.project.bootcampproject.entity.user.*;
import com.ttn.bootcamp.project.bootcampproject.enums.Authority;
import com.ttn.bootcamp.project.bootcampproject.repository.*;
import com.ttn.bootcamp.project.bootcampproject.security.JWTAuthenticationFilter;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

@Service
@Slf4j
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
    JWTGenerator jwtGenerator;
    @Autowired
    JWTAuthenticationFilter authenticationFilter;
    @Autowired
    TokenRepo tokenRepo;


    public ResponseEntity<?> createCustomer(CustomerDTO customerDTO) {
        if(customerRepo.existsByEmail(customerDTO.getEmail())){
            return new ResponseEntity<>("Email is already registered", HttpStatus.BAD_REQUEST);
        }
        if(!customerDTO.getPassword().equals(customerDTO.getConfirmPassword())) {
            return new ResponseEntity<>("Password doesn't match.", HttpStatus.BAD_REQUEST);
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
        customer.setToken(uuid);

        userRepo.save(customer);
        addressRepo.save(address);
        emailService.sendMail(customerDTO.getEmail(), "Activation Code ", "Please Activate your account by clicking on the below link" + "\n http://localhost:8080/user/activate?token=" + uuid);

        return new ResponseEntity<>("Register Successfully",HttpStatus.OK);
    }

    public ResponseEntity<?> viewProfile(String token, CustomerResponseDTO customerResponseDTO) {
        if (jwtService.validateToken(token)) {
            String email = jwtService.getEmailFromJWT(token);
            Customer customer = customerRepo.findByEmail(email).orElseThrow(() -> {
                throw new RuntimeException("User doesn't exist");
            });

            customerResponseDTO.setLastName((customer.getLastName()));
            customerResponseDTO.setFirstName(customer.getFirstName());
            customerResponseDTO.setEmail(customer.getEmail());

            return new ResponseEntity<>(customerResponseDTO,HttpStatus.OK);
        }
        return new ResponseEntity<>("Token is invalid or expire!!", HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> updateProfile(String token, CustomerUpdateDTO customerUpdateDTO) {
        Token accessToken = tokenRepo.findByToken(token).orElseThrow(()->{throw new RuntimeException("Token not found!!");});
        if(accessToken.isDelete()){
            return new ResponseEntity<>("your token is expired or incorrect",HttpStatus.UNAUTHORIZED);
        }
        String email  = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepo.findByEmail(email).orElseThrow(()->{throw new RuntimeException("User doesn't exist!!");});

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
        customerRepo.save(customer);

        return new ResponseEntity<>("Update Successfully!!",HttpStatus.OK);

    }

    public ResponseEntity<?> updatePassword(String token, ResetPasswordDTO resetPasswordDTO){
        Token accessToken = tokenRepo.findByToken(token).orElseThrow(()->{throw new RuntimeException("Token not found!!");});
        if(accessToken.isDelete()){
            return new ResponseEntity<>("your token is expired or incorrect",HttpStatus.UNAUTHORIZED);
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepo.findByEmail(email).orElseThrow(()->{throw new RuntimeException("user doesn't exist!!");});
        if(Objects.equals(resetPasswordDTO.getPassword(),resetPasswordDTO.getConfirmPassword())){
            customer.setPassword(encoder.encode(resetPasswordDTO.getPassword()));
            customer.setPasswordUpdateDate(LocalDate.now());
            customerRepo.save(customer);
            emailService.sendMail(customer.getEmail(), "Password Reset","Your password has been updated successfully");

            return new ResponseEntity<>("Password Update Successfully!!",HttpStatus.OK);
        }
        return new ResponseEntity<>("Password should be match",HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<?> updateAddress(String token, AddressDTO addressDTO){
            Token accessToken = tokenRepo.findByToken(token).orElseThrow(()->{throw new RuntimeException("Token not found!!");});
            if(accessToken.isDelete()){
                return new ResponseEntity<>("your token is expired or incorrect",HttpStatus.UNAUTHORIZED);
            }
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Customer customer = customerRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("User doesn't exist"));
            Address address = addressRepo.findByCustomer(customer).orElseThrow(()->{throw new RuntimeException("User doesn't found!!");});

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

//    public ResponseEntity<?> viewAddress(){
//        if(jwtService.validateToken(token)){
//            Token accessToken = tokenRepo.findByToken(token).orElseThrow(()->{throw new RuntimeException("Token not found!!");});
//            if(accessToken.isDelete()==true){
//                return new ResponseEntity<>("your token is expired or incorrect",HttpStatus.UNAUTHORIZED);
//            }
//            String email= jwtGenerator.getEmailFromJWT(token);
//            Customer customer = customerRepo.findByEmail(email).orElseThrow(()->{throw new RuntimeException("user not found");});
//            Address address = addressRepo.findByCustomer(customer).orElseThrow(()->{throw new RuntimeException("user not found");});
//
//            return new ResponseEntity<>(address,HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Token is not valid or incorrect",HttpStatus.BAD_REQUEST);
//    }
    public ResponseEntity<?> addNewAddress(String token,AddressDTO addressDTO){
        Token accessToken = tokenRepo.findByToken(token).orElseThrow(()->{throw new RuntimeException("Token not found!!");});
        if(accessToken.isDelete()){
            return new ResponseEntity<>("Your token is invalid or expired!!",HttpStatus.BAD_REQUEST);
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer  = customerRepo.findByEmail(email).orElseThrow(()->{throw new RuntimeException("User not found!!");});
        Address address = new Address();
        address.setAddressLine(addressDTO.getAddressLine());
        address.setLabel(addressDTO.getLabel());
        address.setZipCode(addressDTO.getZipCode());
        address.setCountry(addressDTO.getCountry());
        address.setState(addressDTO.getState());
        address.setCity(addressDTO.getCity());
        address.setCustomer(customer);
        addressRepo.save(address);
        return new ResponseEntity<>("Address added successfully!!",HttpStatus.OK);
    }

    public ResponseEntity<?> viewAddress(String token){
        Token accessToken = tokenRepo.findByToken(token).orElseThrow(()->{throw new RuntimeException("Token not found!!");});
        if(accessToken.isDelete()){
            return new ResponseEntity<>("your token is expired or incorrect",HttpStatus.UNAUTHORIZED);
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepo.findByEmail(email).orElseThrow(()->{throw new RuntimeException("user not found");});
        Long customerId = customer.getUserId();
//        Address address = addressRepo.findByCustomer(customer).orElseThrow(()->{throw new RuntimeException("user not found");});
        Address address = addressRepo.findById(customerId).orElseThrow(()->{throw new RuntimeException("There is no address found to this corresponding user!!");});
        List<Address> addressList  = new ArrayList<>();
        addressList.add(address);
        return new ResponseEntity<>(addressList,HttpStatus.OK);
    }

    public ResponseEntity<?> deleteAddress(String token,Long id){
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Token accessToken = tokenRepo.findByToken(token).orElseThrow(()->{throw new RuntimeException("Token not found!!");});
        if(accessToken.isDelete()){
            return new ResponseEntity<>("your token is expired or incorrect",HttpStatus.UNAUTHORIZED);
        }

        Address address = addressRepo.findById(id).orElseThrow(()->{throw new RuntimeException("Address not found");});
        Long customerId= address.getCustomer().getUserId();
        Customer customer = customerRepo.findById(customerId).orElseThrow(()->{throw new RuntimeException("user not found");});
        if(customer.getUserId()==customerId){
            addressRepo.delete(address);
            return new ResponseEntity<>("Address deleted Successfully!!",HttpStatus.OK);
        }
        return new ResponseEntity<>("Id is incorrect",HttpStatus.BAD_REQUEST);
    }

}
