package com.ttn.bootcamp.project.bootcampproject.dto;

import com.ttn.bootcamp.project.bootcampproject.entity.product.Category;
import com.ttn.bootcamp.project.bootcampproject.entity.product.CategoryMetadataFieldValues;
import com.ttn.bootcamp.project.bootcampproject.entity.product.ProductVariation;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ViewProductDTO {
    private String productName;
    private Long productId;
    private String categoryName;
    private String brand;
    private String description;
    private Long categoryId;
    private List<ProductVariationDTO> productVariation;
}
