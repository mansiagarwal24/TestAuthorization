package com.ttn.bootcamp.project.bootcampproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/home")
    public String getMessage(){
        return "hello World";
    }
}
