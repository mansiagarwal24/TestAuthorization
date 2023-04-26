package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.CustomerDTO;
import com.ttn.bootcamp.project.bootcampproject.repository.CustomerRepo;
import com.ttn.bootcamp.project.bootcampproject.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerRepo customerRepo;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CustomerDTO customerDTO){
        if(customerRepo.existsByEmail(customerDTO.getEmail())){
            return new ResponseEntity<>("Email is already registered", HttpStatus.BAD_REQUEST);
        }
        if(!customerDTO.getPassword().equals(customerDTO.getConfirmPassword())) {
            return new ResponseEntity<>("Password doesn't match.", HttpStatus.BAD_REQUEST);
        }
        customerService.createCustomer(customerDTO);
        return new ResponseEntity<>("Register Successfully!!", HttpStatus.OK);
    }
    @PutMapping("/activate")
    public ResponseEntity<?> activateAccount(@RequestParam String token){
     return new ResponseEntity<>("Account is activated",HttpStatus.OK);
    }
}
