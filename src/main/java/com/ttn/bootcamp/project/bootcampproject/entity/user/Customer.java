package com.ttn.bootcamp.project.bootcampproject.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
//@PrimaryKeyJoinColumn(name="user_id")
public class Customer extends User{

    private String contact;

    @JsonIgnore
    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    private List<Address> addressList;
}
