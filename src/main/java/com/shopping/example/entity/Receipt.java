package com.shopping.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "Receipt")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long receiptId;

    @ManyToOne
    @JoinColumn(name = "Supplier_Id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name ="Employee_Id")
    private Employee employee;

    @Column
    private LocalDate receiptDate;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    private List<ReceiptDetail> receiptDetails;

    @Column
    private String receiptStatus = "Pending";
}
