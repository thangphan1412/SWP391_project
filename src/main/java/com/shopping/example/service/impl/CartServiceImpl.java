package com.shopping.example.service.impl;


import com.shopping.example.entity.Cart;
import com.shopping.example.entity.Customer;
import com.shopping.example.repository.CartItemsRepository;
import com.shopping.example.repository.CartRepository;
import com.shopping.example.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartServiceImpl implements CartService {
     @Autowired
     private CartRepository cartRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartByCustomer(Customer customer) {
        return cartRepository.findByCustomer_Id(customer.getId());
    }

    @Override
    public boolean deleteCartByCart(Long cart) {
        cartItemsRepository.deleteByCart(cart);
        return cartRepository.deleteAllByCartId(cart);
    }


}
