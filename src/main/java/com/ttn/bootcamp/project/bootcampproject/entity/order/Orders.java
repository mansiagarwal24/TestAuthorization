package com.ttn.bootcamp.project.bootcampproject.entity.order;

import com.ttn.bootcamp.project.bootcampproject.Audit;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Customer;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Orders extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="userid",sequenceName ="order",initialValue = 1,allocationSize = 1)
    private Long id;
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
