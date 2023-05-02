package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.LoginDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.ResetPasswordDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Token;
import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import com.ttn.bootcamp.project.bootcampproject.repository.TokenRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTGenerator jwtGenerator;
    @Autowired
    TokenRepo tokenRepo;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    EmailService emailService;

    public ResponseEntity<?> login(LoginDTO loginDTO){
        User user = userRepo.findByEmail(loginDTO.getEmail()).get();
        if (!user.isActive()) {
            return new ResponseEntity<>("please activate your account", HttpStatus.UNAUTHORIZED);
        }
        if (user.isLocked()) {
            return new ResponseEntity<>("Your account is locked. please contact to admin", HttpStatus.UNAUTHORIZED);
        }

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        } catch (Exception e) {
            user.setInvalidAttemptCount(user.getInvalidAttemptCount() + 1);
            userRepo.save(user);

            if (user.getInvalidAttemptCount()==3) {
                user.setLocked(true);
                user.setActive(false);
                userRepo.save(user);
                return new ResponseEntity<>("Invalid Attempts!!\n your account has been locked!!", HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>("Incorrect Password!! please try again.",HttpStatus.UNAUTHORIZED);
        }
        if(authentication.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);

            Token accessToken = new Token();
            accessToken.setToken(token);
            accessToken.setUser(user);
            accessToken.setEmail(user.getEmail());
            tokenRepo.save(accessToken);

            return new ResponseEntity<>("Login Successfully. Your access token is : " + token, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Email!!",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> logout(String token){
        if(jwtGenerator.validateToken(token)) {
            String email =jwtGenerator.getEmailFromJWT(token);
            User user = userRepo.findByEmail(email).orElseThrow(() -> {
                throw new RuntimeException("User doesn't exist!!");
            });
            Token accesstoken = tokenRepo.findByToken(token).get();
            if (accesstoken.isDelete() == true) {
                return new ResponseEntity<>("You are not logged in", HttpStatus.UNAUTHORIZED);
            } else {
                accesstoken.setDelete(true);
                tokenRepo.save(accesstoken);
                return new ResponseEntity<>("Account logout successfully!!", HttpStatus.OK);
            }

        }
        return new ResponseEntity<>("Token is not valid or expire",HttpStatus.UNAUTHORIZED);
    }

    public boolean forgotPassword(String email){
        User user = userRepo.findByEmail(email).get();
        if(user == null) {
            return false;
        }
        String token = jwtGenerator.generateToken(email);
        emailService.sendMail(email,"Reset Password Link","Please use this link to reset your password again: "+"\n http://localhost:8080/user/resetPassword?token="+token);
        return true;
    }

    public ResponseEntity<?> resetPassword(String token, ResetPasswordDTO resetDTO){
        if(jwtGenerator.validateToken(token)) {
            String email = jwtGenerator.getEmailFromJWT(token);
            User user = userRepo.findByEmail(email).orElseThrow(()->{throw new RuntimeException("User doesn't exist!!");});
            if (StringUtils.isBlank(resetDTO.getPassword())) {
                return new ResponseEntity<>("Password cannot be blank", HttpStatus.BAD_REQUEST);
            }
            if (!resetDTO.getPassword().equals(resetDTO.getConfirmPassword())) {
                return new ResponseEntity<>("Password doesn't match!!", HttpStatus.BAD_REQUEST);
            } else {
                user.setPassword(encoder.encode(resetDTO.getPassword()));
                LocalDate date = LocalDate.now();
                user.setPasswordUpdateDate(date);
                userRepo.save(user);
                return new ResponseEntity<>("Password reset successfully!!", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Token is not valid or expired!!",HttpStatus.UNAUTHORIZED);

    }

    public boolean activate(String token) {
        User user = userRepo.findByToken(token).get();
        if(user==null){
            return false;
        }
        user.setActive(true);
        userRepo.save(user);
        return true;
    }
}

