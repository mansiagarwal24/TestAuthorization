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


    @GetMapping("/customers")
    public ResponseEntity<?> getCustomer(@RequestParam int pageOffSet,@RequestParam int pageSize,@RequestParam String sort){
        return adminService.findAllCustomer(pageOffSet,pageSize,sort);
    }

    @GetMapping("/sellers")
    public ResponseEntity<?> getSeller(@RequestParam(defaultValue = "10",required = false) int pageOffSet,@RequestParam int pageSize,@RequestParam String sort){
        return adminService.findAllSeller(pageOffSet,pageSize,sort);

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
