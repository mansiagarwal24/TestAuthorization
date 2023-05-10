package com.ttn.bootcamp.project.bootcampproject.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private String productName;
    private String brand;
    private String description;
    private boolean isCancelable;
    private boolean isReturnable;
    private Long categoryId;



}
