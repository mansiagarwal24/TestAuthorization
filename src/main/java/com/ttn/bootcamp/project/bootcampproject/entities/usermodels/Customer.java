package com.ttn.bootcamp.project.bootcampproject.entities.usermodels;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
//@PrimaryKeyJoinColumn(name="userId")
public class Customer extends User{

    private Long contact;

    @OneToMany(mappedBy = "customer")
    private List<Address> addressList;
}
