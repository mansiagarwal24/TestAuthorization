package com.ttn.bootcamp.project.bootcampproject.entity.product;

import com.ttn.bootcamp.project.bootcampproject.converter.HashMapConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Map;

@Entity
@Data
public class ProductVariation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="userid",sequenceName ="product_variation",initialValue = 1,allocationSize = 1)
    private Long id;
    private int quantityAvailable;
    private Long price;
    private boolean isActive;

    @Convert(converter = HashMapConverter.class)
    private Map<String,String> metaData;
//    //image
//    private String primaryImageName;

    @ManyToOne
    @JoinColumn(name="productId")
    private Product product;




}
