package com.ttn.bootcamp.project.bootcampproject.entities.productmodels;

import com.ttn.bootcamp.project.bootcampproject.entities.usermodels.Seller;
import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="userid",sequenceName ="product",initialValue = 1,allocationSize = 1)
    private Long id;
    private String name;
    private String description;

    private String brand;
    private boolean isCancellable=false,isActive=false,isReturnable=false,isDeleted=false;
    @ManyToOne
    @JoinColumn(name="sellerId")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name="categoryId")
    private Category category;


}
