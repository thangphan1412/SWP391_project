package com.shopping.example.service.impl;

import com.shopping.example.entity.CartItems;
import com.shopping.example.entity.ProductType;
import com.shopping.example.repository.CartItemsRepository;
import com.shopping.example.service.CartItemsService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CartItemsServiceImpl implements CartItemsService {

    @Autowired
    CartItemsRepository cartItemsRepository;


    @Override
    public CartItems save(CartItems cartItems) {
        return cartItemsRepository.save(cartItems);
    }

    @Override
    public List<CartItems> findByCartId(Long cartId) {
        return cartItemsRepository.findCartItemsByCart_CartId(cartId);
    }

    @Override
    public void delete(Long cartItemId) {
        cartItemsRepository.deleteByCartId(cartItemId);
    }

    @Override
    public CartItems getCartItemByProductType(ProductType productType) {
        return cartItemsRepository.findByProductType(productType);
    }

    @Override
    public Optional<CartItems> findById(Long cartItemId) {
        return cartItemsRepository.findById(cartItemId);
    }

    @Transactional
    public void updateCartItemQuantity(Long cartItemId, int quantity) {
        CartItems cartItem = cartItemsRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));
        cartItem.setQuantity(quantity);
        cartItemsRepository.save(cartItem);
    }

}
