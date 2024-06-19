package com.shopping.example.service;


import com.shopping.example.entity.CartItems;
import com.shopping.example.entity.ProductType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CartItemsService  {

    CartItems save(CartItems cartItems);

    List<CartItems> findByCartId(Long cartId);

    void delete(Long cartItemId);

    CartItems getCartItemByProductType(ProductType productType);

    Optional<CartItems> findById(Long cartItemId);

    void updateCartItemQuantity(Long cartItemId, int quantity);

}
