package com.ttn.bootcamp.project.bootcampproject.dto;

import lombok.Data;

@Data
public class ProductUpdateDTO {
    private String productName;
    private String description;
    private Boolean isCancel;
    private Boolean isReturn;
    private String brand;
}
