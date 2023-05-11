package com.ttn.bootcamp.project.bootcampproject.dto;

import com.ttn.bootcamp.project.bootcampproject.entity.product.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryResponseDTO {
    private Long id;
    private String name;
    private Category parentCategory;
    private List<Category> childCategory;

}
