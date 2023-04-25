//package com.ttn.bootcamp.project.bootcampproject.entities.ordermodels;
//
//import com.ttn.bootcamp.project.bootcampproject.entities.productmodels.ProductVariation;
//import jakarta.persistence.*;
//import lombok.Data;
//
//@Entity
//@Data
//public class OrderProduct {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @SequenceGenerator(name="userid",sequenceName ="order_product",initialValue = 1,allocationSize = 1)
//    private Long id;
//
//    private int quantity;
//    private Long price;
//
//    @ManyToOne
//    @JoinColumn(name="orderId")
//    private Order order;
//
//    @OneToOne
//    @JoinColumn(name="productVariation")
//    private ProductVariation productVariation;
//}
