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
        Date expirationDateTime = new Date(currentDate.getTime()+60*60*24*1000);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDateTime)
                .signWith(SignatureAlgorithm.HS512,"privateKey")
                .compact();
        return token;
    }
    public String generateToken(String email){
        Date currentDate = new Date();
        Date expirationDateTime = new Date(currentDate.getTime()+100000);

        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDateTime)
                .signWith(SignatureAlgorithm.HS512,"privateKey")
                .compact();
        return token;
    }

    public String getEmailFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey("privateKey")
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();

    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey("privateKey").parseClaimsJws(token);
            return true;
        }catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("Token has Expired or Incorrect");
        }
    }


}
