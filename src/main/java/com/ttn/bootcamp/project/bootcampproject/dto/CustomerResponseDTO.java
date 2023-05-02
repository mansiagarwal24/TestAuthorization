package com.ttn.bootcamp.project.bootcampproject.dto;

import lombok.Data;

@Data
public class CustomerResponseDTO {
    private String firstName;
    private String lastName;
    private Long id;
    private String email;
    private boolean isActive;

}
