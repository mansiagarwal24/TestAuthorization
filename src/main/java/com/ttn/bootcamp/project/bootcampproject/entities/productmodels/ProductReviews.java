package com.ttn.bootcamp.project.bootcampproject.entities.productmodels;

import com.ttn.bootcamp.project.bootcampproject.entities.compositekeys.ProductReviewsId;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class ProductReviews {
    @EmbeddedId
    private ProductReviewsId productReviewsId;

    private String review;
    private int rating;
//
//    @ManyToOne
//    private Customer customer;
//
//    @ManyToOne
//    private Product product;

}
