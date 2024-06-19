package com.shopping.example.DTO.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemsRequest {

    private Long cartId;

    private Long productTypeId;

    @Min(value = 1, message = "Quantity must be greater than 0")
    private int quantity;
}
