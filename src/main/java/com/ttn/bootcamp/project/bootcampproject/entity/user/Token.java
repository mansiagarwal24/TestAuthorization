package com.ttn.bootcamp.project.bootcampproject.entity.user;

import com.ttn.bootcamp.project.bootcampproject.Audit;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

@Entity
@Data
//@Where(clause = "is_Delete=0")
public class Token extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String email;
    private String token;
    private boolean isDelete=false;

    @OneToOne
    private User user;
}
