package com.ttn.bootcamp.project.bootcampproject.Entities.CompositeKey;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ProductReviewsId {
    private Long customerUserId;
    private Long productId;

    public ProductReviewsId(Long customerUserId, Long productId) {
        this.customerUserId = customerUserId;
        this.productId = productId;
    }
}
