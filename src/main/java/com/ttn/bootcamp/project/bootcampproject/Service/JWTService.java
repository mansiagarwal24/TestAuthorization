package com.ttn.bootcamp.project.bootcampproject.Service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class JWTService {

    public String generateToken(String email){
        Date currentDate = new Date();
        Date expirationDateTime = new Date(currentDate.getTime()+100000);
        String token = Jwts.builder().setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expirationDateTime)
                .signWith(SignatureAlgorithm.HS512,"privateKey")
                .compact();
        return token;
    }


}
