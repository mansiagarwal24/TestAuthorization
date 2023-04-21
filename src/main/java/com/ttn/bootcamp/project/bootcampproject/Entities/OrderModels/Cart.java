package com.ttn.bootcamp.project.bootcampproject.Entities.OrderModels;

import com.ttn.bootcamp.project.bootcampproject.Entities.ProductModels.ProductVariation;
import com.ttn.bootcamp.project.bootcampproject.Entities.UserModels.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Cart {
    @OneToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    //private int quantity;
    private boolean isWishListItem;

    @ManyToOne
    @JoinColumn(name = "productVariationId")
    private ProductVariation productVariation;
}
