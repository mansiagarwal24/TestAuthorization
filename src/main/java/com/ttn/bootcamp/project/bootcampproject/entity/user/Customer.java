package com.ttn.bootcamp.project.bootcampproject.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;

import java.util.List;

@Entity
@Data
//@PrimaryKeyJoinColumn(name="user_id")
public class Customer extends User{

    private String contact;

    @OneToMany(mappedBy = "customer")
    private List<Address> addressList;
}
