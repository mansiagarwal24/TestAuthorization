package com.ttn.bootcamp.project.bootcampproject.entity.compositekeys;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class ProductReviewsId implements Serializable {
    private Long customerUserId;
    private Long productId;

    public ProductReviewsId(Long customerUserId, Long productId) {
        this.customerUserId = customerUserId;
        this.productId = productId;
    }
}
