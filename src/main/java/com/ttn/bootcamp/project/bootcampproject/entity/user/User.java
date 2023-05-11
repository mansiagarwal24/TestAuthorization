package com.ttn.bootcamp.project.bootcampproject.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString@Inheritance(strategy = InheritanceType.JOINED)
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
    private String filePath;
    private String token;
    private LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(30);

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> role;

    @OneToOne(fetch = FetchType.EAGER)
    private Token accessToken;

}
