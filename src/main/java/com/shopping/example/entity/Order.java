package com.shopping.example.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId ;

    @ManyToOne
    @JoinColumn(name = "Customer_Id", nullable = false)
    private Customer customer ;

    @ManyToOne
    @JoinColumn(name ="Employee_Id")
    private Employee employee ;

//    @ManyToOne
//    @JoinColumn(name ="Shipper_Id",nullable = false)
//    private Shipper shipper;

    @Column(nullable = false)
    private String addressOfReceiver;

    @Column(nullable = false)
    private String phoneOfReceiver;

    @Column(nullable = false)
    private LocalDate createDate;

    @Column
    private LocalDate approvalDate;

    @Column
    private String paymentStatus;

    @Column
    private String paymentMethod;

    @Column(nullable = false)
    private String orderStatus;

    @Column
    private double orderAmount;

    @Column
    private boolean orderRequestCancel = Boolean.parseBoolean("False");

    @OneToMany
    @JoinColumn
    private List<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn
    private Voucher voucher;



}
