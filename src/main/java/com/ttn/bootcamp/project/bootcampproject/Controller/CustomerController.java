package com.ttn.bootcamp.project.bootcampproject.Controller;

import com.ttn.bootcamp.project.bootcampproject.DTO.CustomerDTO;
import com.ttn.bootcamp.project.bootcampproject.Entities.UserModels.Customer;
import com.ttn.bootcamp.project.bootcampproject.Service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CustomerDTO customerDTO){
        return customerService.createCustomer(customerDTO);
    }
}
