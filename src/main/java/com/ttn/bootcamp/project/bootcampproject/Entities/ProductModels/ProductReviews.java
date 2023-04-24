package com.ttn.bootcamp.project.bootcampproject.Entities.ProductModels;

import com.ttn.bootcamp.project.bootcampproject.Entities.CompositeKey.ProductReviewsId;
import com.ttn.bootcamp.project.bootcampproject.Entities.UserModels.Customer;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
public class ProductReviews {
    @EmbeddedId
    private ProductReviewsId productReviewsId;

    private String review;
    private int rating;

    @ManyToOne
    @JoinColumn(name = "customerUserId")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="productId")
    private Product product;

}
