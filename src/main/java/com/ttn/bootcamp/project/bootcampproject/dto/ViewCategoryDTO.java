package com.ttn.bootcamp.project.bootcampproject.dto;

import com.ttn.bootcamp.project.bootcampproject.entity.product.Category;
import lombok.Data;

@Data
public class ViewCategoryDTO {
    private Long id;
    private String categoryName;
    private Category parentCategoryId;
}
