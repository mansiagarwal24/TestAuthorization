package com.ttn.bootcamp.project.bootcampproject.service;

import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuditService implements AuditorAware<User> {
    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || !authentication.isAuthenticated() || authentication.getName().equals("anonymousUser")){
            return null;
        }
        return Optional.of((User) authentication.getPrincipal());
    }
}
