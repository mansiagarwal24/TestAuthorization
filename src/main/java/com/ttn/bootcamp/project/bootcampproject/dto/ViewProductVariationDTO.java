package com.ttn.bootcamp.project.bootcampproject.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ViewProductVariationDTO {
    private Long productId;
    private int quantity;
    private Long price;
    private Map<String,String> metadataValues;
    private boolean isActive;
    private String productName;
    private String brand;
    private String description;

}
