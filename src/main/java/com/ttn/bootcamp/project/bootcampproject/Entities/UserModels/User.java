package com.ttn.bootcamp.project.bootcampproject.Entities.UserModels;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Entity
@Setter
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;
    private boolean isDeleted,isActive,isExpired,isLocked;
    private int invalidAttemptCount;
    private Date passwordUpdateDate;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Role> role;

    @OneToMany(mappedBy = "user")
    private Set<Address> address;

}
