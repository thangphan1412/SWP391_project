package com.shopping.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity

public class ProductTech {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long techId;

    @Column
    private int ram;

    @Column
    private double size;

    @Column
    private int memory;




}
