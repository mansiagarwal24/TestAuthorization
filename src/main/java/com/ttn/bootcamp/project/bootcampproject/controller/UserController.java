package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.LoginDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.ResetDTO;
import com.ttn.bootcamp.project.bootcampproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
     return userService.login(loginDTO);
    }

    @PutMapping("/activate")
    public ResponseEntity<?> activateAccount(@RequestParam String token){
        boolean isActivate=userService.activate(token);
        if(isActivate==true){
            return new ResponseEntity<>("Your Account has been activated",HttpStatus.OK);
        }
        return new ResponseEntity<>("Your token has expired or incorrect ",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam String email){
        boolean isValid = userService.forgotPassword(email);
        if(isValid==true){
            return  new ResponseEntity<>("Reset Password email sent successfully!!",HttpStatus.OK);
        }
        return  new ResponseEntity<>("Email doesn't exist!!",HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetDTO resetDTO){
        return userService.resetPassword(resetDTO);

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String token){
        return userService.logout(token);
    }

}
