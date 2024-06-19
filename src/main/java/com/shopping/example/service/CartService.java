package com.shopping.example.service;


import com.shopping.example.entity.Cart;
import com.shopping.example.entity.Customer;

import org.springframework.stereotype.Service;

@Service
public interface CartService {
    Cart save(Cart cart);

    Cart getCartByCustomer(Customer customer);

}
