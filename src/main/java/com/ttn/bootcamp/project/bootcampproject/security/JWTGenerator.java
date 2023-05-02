package com.ttn.bootcamp.project.bootcampproject.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class JWTGenerator {

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDateTime = new Date(currentDate.getTime()+SecurityConstant.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDateTime)
                .signWith(SignatureAlgorithm.HS512,SecurityConstant.SECRET_KEY)
                .compact();
        return token;
    }
    public String generateToken(String email){
        Date currentDate = new Date();
        Date expirationDateTime = new Date(currentDate.getTime()+SecurityConstant.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDateTime)
                .signWith(SignatureAlgorithm.HS512,SecurityConstant.SECRET_KEY)
                .compact();
        return token;
    }

    public String getEmailFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstant.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(SecurityConstant.SECRET_KEY).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("Token has Expired or Incorrect");
        }
    }
}
