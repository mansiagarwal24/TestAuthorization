package com.ttn.bootcamp.project.bootcampproject.controller;

import com.ttn.bootcamp.project.bootcampproject.dto.AuthResponseDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.LoginDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Token;
import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import com.ttn.bootcamp.project.bootcampproject.repository.RoleRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.TokenRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    TokenRepo tokenRepo;
    @Autowired
    DaoAuthenticationProvider provider;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(),loginDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        User user=userRepo.findByEmail(loginDTO.getEmail()).get();
        int count = 0;
        String password =provider.setPasswordEncoder();
        if(isvalid==true){
            count++;
            return new ResponseEntity<>("Try Again,Wrong Credentials!!",HttpStatus.UNAUTHORIZED);
        }
        if(count==3){
            user.setLocked(true);
            return new ResponseEntity<>("Invalid Attempts!! \n your account has been Locked.",HttpStatus.UNAUTHORIZED);
        }
         if(!user.isActive()){
             return new ResponseEntity<>("please activate your account",HttpStatus.BAD_REQUEST);
         }
        Token accessToken = new Token();
         accessToken.setToken(token);
         accessToken.setUser(user);
         accessToken.setEmail(user.getEmail());
         tokenRepo.save(accessToken);

        return new ResponseEntity<>("Login Successfully. Your access token is : "+token, HttpStatus.OK);
    }
}
