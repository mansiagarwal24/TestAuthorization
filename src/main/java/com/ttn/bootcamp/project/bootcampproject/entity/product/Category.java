package com.ttn.bootcamp.project.bootcampproject.entity.product;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "parentCategoryId")
    private Category parentCategory;
}
