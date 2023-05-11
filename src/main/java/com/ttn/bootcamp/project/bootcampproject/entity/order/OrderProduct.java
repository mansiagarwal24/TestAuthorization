package com.ttn.bootcamp.project.bootcampproject.entity.order;

import com.ttn.bootcamp.project.bootcampproject.Audit;
import com.ttn.bootcamp.project.bootcampproject.entity.product.ProductVariation;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderProduct extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="userid",sequenceName ="order_product",initialValue = 1,allocationSize = 1)
    private Long id;

    private int quantity;
    private Long price;

    @ManyToOne
    @JoinColumn(name="orderId")
    private Order order;

    @OneToOne
    @JoinColumn(name="productVariation")
    private ProductVariation productVariation;
}
