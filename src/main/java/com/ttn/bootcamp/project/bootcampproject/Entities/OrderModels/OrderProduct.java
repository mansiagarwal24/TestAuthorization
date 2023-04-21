package com.ttn.bootcamp.project.bootcampproject.Entities.OrderModels;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderProduct {
    private Long id;
    //ORDER_ID
    private int quantity;
    private Long price;
    //PRODUCT_VARIATION_ID
}
