package com.ttn.bootcamp.project.bootcampproject.Entities.ProductModels;

import jakarta.persistence.*;

@Entity
public class ProductVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="userid",sequenceName ="product_variation",initialValue = 1,allocationSize = 1)
    private Long id;
    private int quantityAvailable;
    private Long price;
    private boolean isActive;
    //image
    private String primaryImageName;

    @ManyToOne
    @JoinColumn(name="productId")
    private Product product;




}
