package com.ttn.bootcamp.project.bootcampproject.Entities.ProductModels;

import com.ttn.bootcamp.project.bootcampproject.Entities.UserModels.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProductReviews {
    @ManyToOne
    @JoinColumn(name = "customerUserId")
    private Customer customer;
    private String review;
    private int rating;
    @ManyToOne
    @JoinColumn(name="productId")
    private Product product;

}
