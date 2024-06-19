package com.shopping.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data


public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long colorId;

    @Column
    private String colorName;

}
