package com.ttn.bootcamp.project.bootcampproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;
    private String link="http://localhost:8080/activate?token=";

    public void sendMail(String receiver,String token){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("mansi.aggarwal@tothenew.com");
        simpleMailMessage.setTo(receiver);
        simpleMailMessage.setSubject("Registeration Activation Code");
        simpleMailMessage.setText("Your account has been created please activate your account "+link+token);
        javaMailSender.send(simpleMailMessage);


    }
}
