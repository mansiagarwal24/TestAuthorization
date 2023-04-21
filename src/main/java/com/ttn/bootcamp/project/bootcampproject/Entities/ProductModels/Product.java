package com.ttn.bootcamp.project.bootcampproject.Entities.ProductModels;

import com.ttn.bootcamp.project.bootcampproject.Entities.UserModels.Seller;
import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    //seller_user_id(foreign key)
    private String name;
    private String description;

    //Category id
    private String brand;
    private boolean isCancellable,isActive,isReturnable,isDeleted;
    @ManyToOne
    @JoinColumn(name="sellerId")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name="categoryId")
    private Category category;


}
