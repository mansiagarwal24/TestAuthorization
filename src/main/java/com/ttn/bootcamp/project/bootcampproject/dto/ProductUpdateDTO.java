package com.ttn.bootcamp.project.bootcampproject.dto;

import lombok.Data;

@Data
public class ProductUpdateDTO {
    private String productName;
    private String description;
    private boolean isCancel;
    private boolean isReturn;
    private String brand;
}
