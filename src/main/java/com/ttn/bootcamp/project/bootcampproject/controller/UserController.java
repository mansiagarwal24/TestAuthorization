package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.LoginDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Token;
import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import com.ttn.bootcamp.project.bootcampproject.repository.RoleRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.TokenRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import com.ttn.bootcamp.project.bootcampproject.service.CustomerService;
import com.ttn.bootcamp.project.bootcampproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public ResponseEntity<?> resetPassword(@RequestParam String token,@RequestBody String password){
        boolean isPassowordSet =userService.resetPassword(token,password);
        if(isPassowordSet==true){
            return  new ResponseEntity<>("Password set successfully!!",HttpStatus.OK);
        }
        return  new ResponseEntity<>("Password cannot be empty!!",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String token){
        boolean isvalid = userService.logout(token);
        if(isvalid==true) {
            return new ResponseEntity<>("Account logout successfully!!", HttpStatus.OK);
        }
        return new ResponseEntity<>("You are not logged in.",HttpStatus.BAD_REQUEST);
    }
}
