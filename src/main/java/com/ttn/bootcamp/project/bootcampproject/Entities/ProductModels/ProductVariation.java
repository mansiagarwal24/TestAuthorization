package com.ttn.bootcamp.project.bootcampproject.Entities.ProductModels;

import jakarta.persistence.*;

@Entity
public class ProductVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    //product_id(foreign key)
    private int quantityAvailable;
    private Long price;
    private boolean isActive;
    //image
    private String primaryImageName;

    @ManyToOne
    @JoinColumn(name="productId")
    private Product product;




}
