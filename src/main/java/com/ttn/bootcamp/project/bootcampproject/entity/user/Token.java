package com.ttn.bootcamp.project.bootcampproject.entity.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="tokenid",sequenceName ="token",initialValue = 1,allocationSize = 1)
    private Long Id;
    private String email;
    private String token;
    private boolean isDelete=false;
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
}
