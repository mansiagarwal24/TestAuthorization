package com.ttn.bootcamp.project.bootcampproject.Entities.UserModels;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="userid",sequenceName ="address",initialValue = 1,allocationSize = 1)
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
