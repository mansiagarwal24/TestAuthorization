package com.ttn.bootcamp.project.bootcampproject.entity.product;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CategoryMetadataField {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="userId")
    @SequenceGenerator(name="userid",sequenceName ="category_metadata_field",allocationSize = 1)
    private Long id;
    private String name;

}
