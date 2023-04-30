package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.CustomerDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Customer;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Seller;
import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import com.ttn.bootcamp.project.bootcampproject.repository.CustomerRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.SellerRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class AdminService {
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    SellerRepo sellerRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    EmailService emailService;


    public List<Customer> findAllCustomers() {
        List<Customer> users = customerRepo.findAll();
        return users;
    }

    public List<Seller> findAllSellers() {
        List<Seller> users = sellerRepo.findAll();
        return users;
    }

    public ResponseEntity<?> activateUser(Long id) {
        if(userRepo.existsById(id)){
            User user = userRepo.findById(id).get();
            if (!user.isActive()) {
                user.setActive(true);
                userRepo.save(user);
                emailService.sendMail(user.getEmail(), "Account Activation Status", "Your account has been activated.");
                return new ResponseEntity<>("Account has been activated!!",HttpStatus.OK);
            }
            return new ResponseEntity<>("Account is already activated!!",HttpStatus.OK);
        }
        return new ResponseEntity<>("User doesn't exist!!", HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<?> deactivateUser(Long id) {
        if(userRepo.existsById(id)){
            User user = userRepo.findById(id).get();
            if (user.isActive()) {
                user.setActive(false);
                userRepo.save(user);
                emailService.sendMail(user.getEmail(), "Account Activation Status", "Your account has been deactivated.");
                return new ResponseEntity<>("Account has been deactivated!!",HttpStatus.OK);
            }
            return new ResponseEntity<>("Account is already deactivated!!",HttpStatus.OK);
        }
        return new ResponseEntity<>("User doesn't exist!!", HttpStatus.BAD_REQUEST);
    }

}

