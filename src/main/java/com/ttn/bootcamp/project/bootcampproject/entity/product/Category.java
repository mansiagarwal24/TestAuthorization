package com.ttn.bootcamp.project.bootcampproject.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ttn.bootcamp.project.bootcampproject.Audit;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "parentCategoryId")
    private Category parentCategory;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    List<CategoryMetadataFieldValues> categoryMetadataFieldValues;
}
