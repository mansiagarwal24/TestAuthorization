package com.ttn.bootcamp.project.bootcampproject.entity.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ttn.bootcamp.project.bootcampproject.Audit;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Seller;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="userid",sequenceName ="product",initialValue = 1,allocationSize = 1)
    private Long id;
    private String name;
    private String description;

    private String brand;
    private Boolean cancel=Boolean.FALSE,active=Boolean.FALSE,returnable=Boolean.FALSE,deleted=Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name="sellerId")
    @JsonIgnore
    private Seller seller;

    @ManyToOne
    @JoinColumn(name="categoryId")
    private Category category;

    @OneToMany(mappedBy = "product")

    @JsonIgnore
    private List<ProductVariation> productVariation;


}
