package com.ttn.bootcamp.project.bootcampproject.Entities.UserModels;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@PrimaryKeyJoinColumn(name="userId")
public class Seller extends User{
    private String gst;
    private String companyContact;
    private String companyName;
}
