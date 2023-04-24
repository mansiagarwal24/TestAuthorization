package com.ttn.bootcamp.project.bootcampproject.Entities.UserModels;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
//@PrimaryKeyJoinColumn(name="userId")
public class Seller extends User{
    private String gst;
    private String companyContact;
    private String companyName;

    @OneToOne(mappedBy = "seller")
    private Address address;
}
