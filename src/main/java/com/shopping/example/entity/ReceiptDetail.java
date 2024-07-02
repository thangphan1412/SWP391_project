package com.shopping.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "ProductType_Id")
    private ProductType productTypes;

    @ManyToOne
    @JoinColumn(name = "Receipt_Id")
    private Receipt receipt;
}
