package com.shopping.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Table(name = "customers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    private Long id;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private String city;
    @Column
    private String district;
    @Column
    private String ward;
    @Column
    private String addressDetail;
    @Column
    private String fullname;
    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

}
