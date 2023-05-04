package com.ttn.bootcamp.project.bootcampproject.entity.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "addressid")
    @SequenceGenerator(name="addressid",sequenceName ="address",allocationSize = 1)
    private Long id;
    private  String city;
    private String state;
    private String country;
    private String addressLine;
    private int zipCode;
    private String label; //(Ex. office/home)

    @ManyToOne
    private Customer customer;

    @OneToOne
    private Seller seller;




}
