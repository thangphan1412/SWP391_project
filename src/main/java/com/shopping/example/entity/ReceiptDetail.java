package com.shopping.example.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReceiptDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receiptDetailId;


    @Column
    private int quantity;

    @Column
    private double price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn( name="ProductType_Id")
    private ProductType productTypes;




}
