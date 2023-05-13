package com.ttn.bootcamp.project.bootcampproject.Scheduler;

import com.ttn.bootcamp.project.bootcampproject.entity.user.Customer;
import com.ttn.bootcamp.project.bootcampproject.repository.CustomerRepo;
import com.ttn.bootcamp.project.bootcampproject.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmailScheduler {
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    EmailService emailService;

    @Scheduled(cron = "0 12 * * * ?")
    public void sendEmailToUser(){
        List<Customer> customerList = customerRepo.findAll();
        for(Customer customer:customerList){
            emailService.sendMail(customer.getEmail(), "Special Offer",
                    "It’s the end-of-season SALE !!\n" +
                            "Enjoy incredible discounts of up to 40% on our top styles.\n" +
                            "Don’t miss out, shop now!!");
        }
    }
}
