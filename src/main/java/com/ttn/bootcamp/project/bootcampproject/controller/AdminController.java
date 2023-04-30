package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.CustomerDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Customer;
import com.ttn.bootcamp.project.bootcampproject.repository.CustomerRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import com.ttn.bootcamp.project.bootcampproject.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    UserRepo userRepo;
    @GetMapping("/customers")
    public ResponseEntity<?> getCustomer(){
        return new ResponseEntity<>(adminService.findAllCustomers(), HttpStatus.OK);

    }

    @GetMapping("/sellers")
    public ResponseEntity<?> getSeller(){
        return new ResponseEntity<>(adminService.findAllSellers(), HttpStatus.OK);

    }

    @PutMapping("/activate-user/{id}")
    public ResponseEntity<?> activateUser(@PathVariable Long id){
        return adminService.activateUser(id);
    }

    @PutMapping("/deactivate-user/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id){
       return adminService.deactivateUser(id);
    }
}
