package com.ttn.bootcamp.project.bootcampproject.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;

@Entity
@Data
@PrimaryKeyJoinColumn(name="user_id")
public class Seller extends User{
    private String gstNo;
    private String companyContact;
    private String companyName;

    @OneToOne(mappedBy = "seller")
    private Address address;
}
