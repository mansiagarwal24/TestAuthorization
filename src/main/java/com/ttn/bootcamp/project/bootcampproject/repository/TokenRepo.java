package com.ttn.bootcamp.project.bootcampproject.repository;

import com.ttn.bootcamp.project.bootcampproject.entity.user.Token;
import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepo extends JpaRepository<Token,Long> {
//    Optional<Token> findByTokenAndEmail(String email,String token);
    Token findByEmail(String email);
    Optional<Token> findByToken(String token);
//    Token findByUser(User user);
//    boolean existsByToken(String token);
//
//    boolean existsByEmail(String email);
//
//    void deleteByEmail(String email);
}
