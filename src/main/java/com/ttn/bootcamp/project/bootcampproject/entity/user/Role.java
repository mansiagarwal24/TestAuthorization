package com.ttn.bootcamp.project.bootcampproject.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ttn.bootcamp.project.bootcampproject.enums.Authority;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity
@Setter
@Getter
public class Role implements GrantedAuthority{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "roleId")
    @SequenceGenerator(name="roleId",sequenceName ="role",allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @ManyToMany(mappedBy = "role")
    @JsonIgnore
    private List<User> user;

    public String getAuthority(){
        return authority.toString();
    }

}
