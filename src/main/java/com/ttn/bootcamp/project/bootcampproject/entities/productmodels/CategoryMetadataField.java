package com.ttn.bootcamp.project.bootcampproject.entities.productmodels;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CategoryMetadataField {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="userid",sequenceName ="category_metadata_field",initialValue = 1,allocationSize = 1)
    private Long id;
    private String name;

}
