package com.ttn.bootcamp.project.bootcampproject.entity.product;

import com.ttn.bootcamp.project.bootcampproject.entity.compositekeys.ProductReviewsId;
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
