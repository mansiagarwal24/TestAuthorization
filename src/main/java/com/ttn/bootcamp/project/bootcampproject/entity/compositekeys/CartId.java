package com.ttn.bootcamp.project.bootcampproject.entities.compositekeys;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class CartId {
    private Long customerId;
    private Long productVariationId;

    public CartId(Long customerId, Long productVariationId) {
        this.customerId = customerId;
        this.productVariationId = productVariationId;
    }
}
