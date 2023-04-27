package com.ttn.bootcamp.project.bootcampproject.repository;

import com.ttn.bootcamp.project.bootcampproject.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPassword(String password);
    boolean existsByEmail(String email);
    boolean existsByPassword(String password);
    Optional<User> findByToken(String token);
}
