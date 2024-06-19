package com.shopping.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemsId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "productType_Id")
    private ProductType productType;

    @Column
    private int quantity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Cart_Id")
    private Cart cart;
}
