package com.ttn.bootcamp.project.bootcampproject.entity.user;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

@Entity
@Data
//@Where(clause = "is_Delete=0")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "tokenid")
    @SequenceGenerator(name="tokenid",sequenceName ="token",allocationSize = 1)
    private Long Id;
    private String email;
    private String token;
    private boolean isDelete=false;
    @OneToOne(cascade = CascadeType.ALL)
    private User user;
}
