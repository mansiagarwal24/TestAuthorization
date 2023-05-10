package com.ttn.bootcamp.project.bootcampproject.dto;

import lombok.Data;

import java.util.List;
@Data
public class ProductVariationDTO {
    private Long productId;
    private int quantity;
    private Long price;
    private List<String> metadataValues;

}
