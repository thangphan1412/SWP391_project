package com.shopping.example.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemsResponse {
    private Long cartItemsId;
    private Long productTypeId;
    private int quantity;
    private double productTypePrice;
    private String productName;
    private String productImage;
    private double size;
    private int memory;
    private String color;
    private String ram;
}
