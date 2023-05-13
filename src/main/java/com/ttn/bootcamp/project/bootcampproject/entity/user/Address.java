package com.ttn.bootcamp.project.bootcampproject.entity.user;

import com.ttn.bootcamp.project.bootcampproject.Audit;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
public class Address extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private  String city;
    private String state;
    private String country;
    private String addressLine;
    private int zipCode;
    private String label; //(Ex. office/home)
    private boolean isDelete;

    @ManyToOne
    @ToString.Exclude
    private Customer customer;

    @OneToOne
    @ToString.Exclude
    private Seller seller;




}
