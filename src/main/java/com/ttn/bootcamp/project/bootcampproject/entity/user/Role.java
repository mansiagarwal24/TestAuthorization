package com.ttn.bootcamp.project.bootcampproject.entity.user;

import com.ttn.bootcamp.project.bootcampproject.enums.Authority;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity
@Setter
@Getter
public class Role{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @ManyToMany
    private List<User> user;


}
