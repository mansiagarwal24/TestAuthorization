package com.ttn.bootcamp.project.bootcampproject.security;

import com.ttn.bootcamp.project.bootcampproject.entity.user.Token;
import com.ttn.bootcamp.project.bootcampproject.repository.TokenRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JWTGenerator jwtGenerator;
    @Autowired
    CustomUserDetailsService userDetailsService;
    @Autowired
    TokenRepo tokenRepo;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getJWTFromRequest(request);
        if(StringUtils.hasText(token) && jwtGenerator.validateToken(token)) {
            Token accessToken = tokenRepo.findByToken(token).orElse(null);
            if(accessToken==null){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().write("Token Not Found!!");
                return;
            }
            if(accessToken.isDelete()){
                response.setStatus(HttpStatus.NOT_FOUND.value());
                response.getWriter().write("Token is Expired or Invalid!!");
                return;
            }
            if(!accessToken.getUser().isActive() && accessToken.getUser().isLocked()){
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.getWriter().write("User not activated!!");
                return;
            }

            String email = jwtGenerator.getEmailFromJWT(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                    (userDetails, null,userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request,response);
    }

    public String getJWTFromRequest(HttpServletRequest request){
        String bearerToken =request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }
}
