package com.shopping.example.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

@Table(name = "ProductTypies")
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_type_id;

    @Column
    private double product_type_price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "techId", nullable = false)
    private ProductTech productTech;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "colorId")
    private Color color;


    @Column
    private int product_type_quantity;

    @Column
    private String product_type_status ="Active";

    public void setProduct_type_quantity(int product_type_quantity) {
        this.product_type_quantity = product_type_quantity;
        if (product_type_quantity == 0) {
            this.product_type_status = "Close";
        }
    }







    //mapping OrderDetail


//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "OrderDetail_Id", foreignKey = @ForeignKey(name = "FK_ORDERTYPE_ORDERDETAIL"))
//    private OrderDetail orderDetail;







}
