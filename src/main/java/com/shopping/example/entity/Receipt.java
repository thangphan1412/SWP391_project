package com.shopping.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "Receipt")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receiptId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Supplier_Id")
    private Supplier supplier;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name ="Employee_Id")
    private Employee employee;

    @Column
    private LocalDate receiptDate;


    @OneToOne
    @JoinColumn(name ="Receipt_Detail_Id")
    private ReceiptDetail receiptDetail;





}
