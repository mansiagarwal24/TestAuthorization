package com.ttn.bootcamp.project.bootcampproject.entities.usermodels;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
//@PrimaryKeyJoinColumn(name="userId")
public class Seller extends User{
    private String gst;
    private Long companyContact;
    private String companyName;

    @OneToOne(mappedBy = "seller")
    private Address address;
}
