package com.ttn.bootcamp.project.bootcampproject;

import com.ttn.bootcamp.project.bootcampproject.entity.user.Customer;
import com.ttn.bootcamp.project.bootcampproject.repository.CustomerRepo;
import com.ttn.bootcamp.project.bootcampproject.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Scheduler {
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    EmailService emailService;

    @Scheduled(cron = "0 15 10 15 * ?")
    public void sendEmailToUser(){
        List<Customer> customerList = customerRepo.findAll();
        for(Customer customer:customerList){
            emailService.sendMail(customer.getEmail(), "Account Information",
                    "This is an auto-generated email. This is to inform you that to update your password in every 45 days to secure your account.");
        }
    }
}
