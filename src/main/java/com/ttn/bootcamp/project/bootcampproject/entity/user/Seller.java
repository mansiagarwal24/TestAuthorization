package com.ttn.bootcamp.project.bootcampproject.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@PrimaryKeyJoinColumn(name="user_id")
public class Seller extends User{
    private String gstNo;
    private String companyContact;
    private String companyName;

    @JsonIgnore
    @OneToOne(mappedBy = "seller",cascade = CascadeType.ALL)
    private Address address;
}
