package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.dto.LoginDTO;
import com.ttn.bootcamp.project.bootcampproject.dto.ResetPasswordDTO;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Token;
import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.GenericMessageException;
import com.ttn.bootcamp.project.bootcampproject.exceptionhandler.ResourcesNotFoundException;
import com.ttn.bootcamp.project.bootcampproject.repository.TokenRepo;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import com.ttn.bootcamp.project.bootcampproject.security.JWTGenerator;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
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
import java.util.Optional;
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
            return new ResponseEntity<>("Your account is not activated!!", HttpStatus.UNAUTHORIZED);
        }
        if (user.isLocked()) {
            return new ResponseEntity<>("Your account is locked. please contact to admin", HttpStatus.UNAUTHORIZED);
        }
        if (user.isExpired()) {
            return new ResponseEntity<>("Your account is expired", HttpStatus.UNAUTHORIZED);
        }
        if (user.isDeleted()) {
            return new ResponseEntity<>("Your account is deleted.", HttpStatus.UNAUTHORIZED);
        }

        Token activeToken=tokenRepo.findByEmail(user.getEmail());
        if(!Objects.isNull(activeToken)){
            tokenRepo.delete(activeToken);
        }

        log.info(user.getEmail());
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
                emailService.sendMail(loginDTO.getEmail(),"Account Status","Your account has been locked,You have successively 3 wrong attempts!!");
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
            user.setInvalidAttemptCount(0);
            userRepo.save(user);
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
        if(!userRepo.existsByEmail(email)) {
            return new ResponseEntity<>(i18Service.getMsg("user.forgotPassword"),HttpStatus.BAD_REQUEST);

        }
        String token = jwtGenerator.generateToken(email);
        emailService.sendMail(email,"Reset Password Link","Please use this link to reset your password again: "+"\n http://localhost:8080/user/resetPassword?token="+token);
        return new ResponseEntity<>("Reset password link sent successfully ",HttpStatus.OK);
    }

    public ResponseEntity<?> resetPassword(String token,ResetPasswordDTO resetDTO){
        String email = jwtGenerator.getEmailFromJWT(token);
        User user = userRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("User doesn't exist!!"));
        if (StringUtils.isBlank(resetDTO.getPassword())) {
            log.error("Password is Blank!!");
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
        User user =userRepo.findByRegistrationToken(token).orElseThrow(()-> new RuntimeException("Invalid Token!!"));
        if(user.getExpiryTime().isBefore(LocalDateTime.now())){
            return new ResponseEntity<>("your token is expired!! please go to resend email activation link!!",HttpStatus.BAD_REQUEST);
        }
        if(user==null){
            return new ResponseEntity<>("User not found",HttpStatus.BAD_REQUEST);
        }
        user.setActive(true);
        userRepo.save(user);
        emailService.sendMail(user.getEmail(), "Account Status ", "Your account has been activated!!");
        return new ResponseEntity<>(i18Service.getMsg("user.activate"),HttpStatus.OK);
    }

    public void resendEmail(String email){
        if(!userRepo.existsByEmail(email)) {
            throw new ResourcesNotFoundException("Email not found!!");
        }

        User user = userRepo.findByEmail(email).get();
        if(user.isActive()){
            throw new GenericMessageException("Your account is already activated!");
        }
        String token  = UUID.randomUUID().toString();
        user.setRegistrationToken(token);
        userRepo.save(user);
        emailService.sendMail(email, "Activation Link ", "Please Activate your account by clicking on the below link" + "\n http://localhost:8080/user/activate?token=" + token);
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public Optional<User> findByEmail(String email){
        return userRepo.findByEmail(email);
    }
}

