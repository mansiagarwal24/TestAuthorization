package com.ttn.bootcamp.project.bootcampproject.Entities.UserModels;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="userid",sequenceName ="user",initialValue = 1,allocationSize = 1)
    private Long userId;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String password;
    private boolean isDeleted=false,isActive=false,isExpired=false,isLocked = false;
    private int invalidAttemptCount;
    private Date passwordUpdateDate;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Role> role;

}
