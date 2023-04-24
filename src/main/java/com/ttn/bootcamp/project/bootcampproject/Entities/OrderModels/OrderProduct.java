package com.ttn.bootcamp.project.bootcampproject.Entities.OrderModels;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="userid",sequenceName ="order_product",initialValue = 1,allocationSize = 1)
    private Long id;
    //ORDER_ID
    private int quantity;
    private Long price;
    //PRODUCT_VARIATION_ID
}
