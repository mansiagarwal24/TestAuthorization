package com.ttn.bootcamp.project.bootcampproject.dto;

import com.ttn.bootcamp.project.bootcampproject.entity.user.Address;
import lombok.Data;

@Data
public class SellerResponseDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String companyName;
    private boolean isActive;
    private AddressDTO companyAddress;
    private String companyContact;
    private String image;
}
