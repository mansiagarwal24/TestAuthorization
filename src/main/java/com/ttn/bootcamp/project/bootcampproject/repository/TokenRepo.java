package com.ttn.bootcamp.project.bootcampproject.repository;

import com.ttn.bootcamp.project.bootcampproject.entity.user.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepo extends JpaRepository<Token,Long> {
    Optional<Token> findByTokenAndEmail(String email,String token);
    Optional<Token> findByEmail(String email);
    Optional<Token> findByToken(String token);
    boolean existsByToken(String token);

}
