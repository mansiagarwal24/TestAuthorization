package com.ttn.bootcamp.project.bootcampproject.entity.user;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "userid")
    @SequenceGenerator(name="userid",sequenceName ="user_seq",allocationSize = 1)
    private Long userId;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;
    private boolean isDeleted=false,isActive=false,isExpired=false,isLocked = false;
    private int invalidAttemptCount;
    private LocalDate passwordUpdateDate;
    private String token;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> role;

}
