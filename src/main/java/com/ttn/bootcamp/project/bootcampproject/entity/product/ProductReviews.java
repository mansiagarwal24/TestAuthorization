package com.ttn.bootcamp.project.bootcampproject.entity.product;

import com.ttn.bootcamp.project.bootcampproject.entity.compositekeys.ProductReviewsId;
import com.ttn.bootcamp.project.bootcampproject.entity.user.Customer;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class ProductReviews {
    @EmbeddedId
    private ProductReviewsId productReviewsId;

    private String review;
    private int rating;

    @ManyToOne
    @MapsId("customerUserId")
    private Customer customer;

    @ManyToOne
    @MapsId("productId")
    private Product product;

}
