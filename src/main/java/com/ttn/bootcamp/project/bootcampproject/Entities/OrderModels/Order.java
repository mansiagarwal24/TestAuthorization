package com.ttn.bootcamp.project.bootcampproject.Entities.OrderModels;

import com.ttn.bootcamp.project.bootcampproject.Entities.UserModels.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.Date;
import java.util.List;
@Entity
public class Order {
    private Long Id;
    private Long amountPaid;
    private Date dateCreated;
    private String paymentMethod;
    private String customerAddressCity;
    private String customerAddressState;
    private String customerAddressCountry;
    private String customerAddressLine;
    private int customerAddressZipCode;
    private String customerAddressLabel;
    @ManyToOne
    @JoinColumn(name = "customerUserId")
    private Customer customer;
}
