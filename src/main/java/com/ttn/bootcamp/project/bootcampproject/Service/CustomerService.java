package com.ttn.bootcamp.project.bootcampproject.Service;

import com.ttn.bootcamp.project.bootcampproject.DTO.CustomerDTO;
import com.ttn.bootcamp.project.bootcampproject.Entities.UserModels.Customer;
import com.ttn.bootcamp.project.bootcampproject.Repositories.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    EmailService emailService;

    @Autowired
    JWTService jwtService;

    public ResponseEntity<?> createCustomer(CustomerDTO customerDTO){
        Customer customer=new Customer();
        customer.setEmail(customerDTO.getEmail());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        if(customerDTO.getPassword().equals(customerDTO.getConfirmPassword())){
            customer.setPassword(customerDTO.getPassword());
        } else {
            return ResponseEntity.badRequest().body("Password doesn't match.");
        }
        customer.setContact(customerDTO.getPhoneNo());
        customerRepo.save(customer);

        emailService.sendMail(customer.getEmail(),jwtService.generateToken(customer.getEmail()));
        return ResponseEntity.ok("Customer is created");
    }
}
