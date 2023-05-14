package com.ttn.bootcamp.project.bootcampproject.dto;

import com.ttn.bootcamp.project.bootcampproject.entity.product.Category;
import com.ttn.bootcamp.project.bootcampproject.entity.product.CategoryMetadataFieldValues;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ViewProductVariationDTO {
    private Long productId;
    private int quantity;
    private Long price;
    private String metadataValues;
    private boolean isActive;
    private String productName;
    private String brand;
    private String description;
    private String categoryName;
    private Long categoryId;
    private String imageName;
    private List<CategoryMetadataFieldValues> categoryMetadataValues;
//    private Category category;

}
