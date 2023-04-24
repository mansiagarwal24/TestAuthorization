package com.ttn.bootcamp.project.bootcampproject.Entities.UserModels;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Data
//@PrimaryKeyJoinColumn(name="userId")
public class Customer extends User{

    private Long contact;

    @OneToMany(mappedBy = "customer")
    private List<Address> addressList;
}
