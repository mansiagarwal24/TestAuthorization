package com.ttn.bootcamp.project.bootcampproject.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private String addressLine;
    private String city;
    private String state;
    private String country;
    private String label;
    private int zipCode;
}
