package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import com.ttn.bootcamp.project.bootcampproject.repository.UserRepo;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;


public class AuditService implements AuditorAware<String> {
    @Autowired
    UserService userService;
    @Override
    public @NotNull Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || !authentication.isAuthenticated()){
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();

        Optional<User> user=userService.findByEmail(principal.toString());
        if(user.isEmpty()){
            return Optional.empty();        }
        return Optional.of(user.get().getUserId().toString());
    }
}
