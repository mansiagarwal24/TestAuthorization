package com.ttn.bootcamp.project.bootcampproject.entity.user;

import com.ttn.bootcamp.project.bootcampproject.Audit;
import jakarta.persistence.*;
import lombok.Data;

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

    @ManyToOne
    private Customer customer;

    @OneToOne
    private Seller seller;




}
