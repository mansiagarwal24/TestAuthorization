package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.LoginDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.ResetPasswordDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Token;
import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.ResourcesNotFoundException;
import com.ttn.bootcamp.project.bootcampproject.repository.TokenRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
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
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
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

    @Autowired
    I18Service i18Service;

    public ResponseEntity<?> login(LoginDTO loginDTO){
        User user = userRepo.findByEmail(loginDTO.getEmail()).orElseThrow(()->new ResourcesNotFoundException("Email not found!!"));
        if (!user.isActive()) {
            return new ResponseEntity<>("please activate your account", HttpStatus.UNAUTHORIZED);
        }
        if (user.isLocked()) {
            return new ResponseEntity<>("Your account is locked. please contact to admin", HttpStatus.UNAUTHORIZED);
        }
//        log.info(user.getEmail());
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

    public ResponseEntity<?> logout(String accessToken){
        Token token = tokenRepo.findByToken(accessToken).orElseThrow(() -> new RuntimeException("token  doesn't exist!!"));
        if (token.isDelete()) {
            return new ResponseEntity<>("You are not logged in", HttpStatus.UNAUTHORIZED);
        } else {
            token.setDelete(true);
            tokenRepo.save(token);
            return new ResponseEntity<>("Account logout successfully!!", HttpStatus.OK);
        }
    }

    public ResponseEntity<?> forgotPassword(String email){
        if(userRepo.existsByEmail(email)){
            String token = jwtGenerator.generateToken(email);
            emailService.sendMail(email,"Reset Password Link","Please use this link to reset your password again: "+"\n http://localhost:8080/user/resetPassword?token="+token);
            return new ResponseEntity<>("Reset password link sent successfully ",HttpStatus.OK);
        }
        return new ResponseEntity<>(i18Service.getMsg("user.forgotPassword"),HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<?> resetPassword(String token,ResetPasswordDTO resetDTO){
        String email = jwtGenerator.getEmailFromJWT(token);
        User user = userRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("User doesn't exist!!"));
        if (StringUtils.isBlank(resetDTO.getPassword())) {
            return new ResponseEntity<>("Password cannot be blank", HttpStatus.BAD_REQUEST);
        }
        if (!resetDTO.getPassword().equals(resetDTO.getConfirmPassword())) {
            return new ResponseEntity<>("Password doesn't match!!", HttpStatus.BAD_REQUEST);
        } else {
            user.setPassword(encoder.encode(resetDTO.getPassword()));
            user.setLocked(false);
            user.setActive(true);
            user.setInvalidAttemptCount(0);
            LocalDate date = LocalDate.now();
            user.setPasswordUpdateDate(date);
            userRepo.save(user);
            return new ResponseEntity<>("Password reset successfully!!", HttpStatus.OK);
        }
    }

    public ResponseEntity<?> activate(String token) {
        User user =userRepo.findByToken(token).orElseThrow(()-> new RuntimeException("Invalid Token!!"));
        if(user.getExpiryTime().isBefore(LocalDateTime.now())){
            return new ResponseEntity<>("your token is expired!! please go to resend email activation link!!",HttpStatus.BAD_REQUEST);
        }
        if(user==null){
            return new ResponseEntity<>("User not found",HttpStatus.BAD_REQUEST);
        }
        user.setActive(true);
        userRepo.save(user);
        return new ResponseEntity<>(i18Service.getMsg("user.activate"),HttpStatus.OK);
    }

    public ResponseEntity<?> resendEmail(String email){
        if(userRepo.existsByEmail(email)){
            User user = userRepo.findByEmail(email).orElseThrow(()->new ResourcesNotFoundException("Email not found!!"));
            if(user.isActive()){
                return new ResponseEntity<>("Your account is already activated!!",HttpStatus.OK);
            }
            String token  = UUID.randomUUID().toString();
            emailService.sendMail(email, "Activation Link ", "Please Activate your account by clicking on the below link" + "\n http://localhost:8080/user/activate?token=" + token);
            return new ResponseEntity<>("Email is sent!!",HttpStatus.OK);
        }
        return new ResponseEntity<>("Email is not registered!!",HttpStatus.BAD_REQUEST);
    }
}

