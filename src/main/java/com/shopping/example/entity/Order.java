package com.shopping.example.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.*;

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

    @Column(nullable = false)
    private String addressOfReceiver;

    @Column(nullable = false)
    private String phoneOfReceiver;

    @Column(nullable = false)
    private Date createDate;

    @Column
    private Date approvalDate;

    @Column
    private String paymentStatus;

    @Column
    private String paymentMethod;

    @Column(nullable = false)
    private String orderStatus;

    @OneToMany
    @JoinColumn
    private List<OrderDetail> orderDetails;




}
