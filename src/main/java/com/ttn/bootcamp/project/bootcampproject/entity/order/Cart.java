package com.ttn.bootcamp.project.bootcampproject.entity.order;

import com.ttn.bootcamp.project.bootcampproject.Audit;
import com.ttn.bootcamp.project.bootcampproject.entities.compositekeys.CartId;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Cart extends Audit {
    @EmbeddedId
    private CartId cardId;

    //private int quantity;
    private boolean isWishListItem;

}
