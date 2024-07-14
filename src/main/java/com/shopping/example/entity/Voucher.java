package com.shopping.example.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voucherId;

    @Column
    private String voucherCode;

    @Column
    private LocalDate createDate;

    @Column
    private LocalDate endDate;

    @Column
    private double percentageDiscount;

    @Column
    private int quantity;

    @Column
    private String status;

}
