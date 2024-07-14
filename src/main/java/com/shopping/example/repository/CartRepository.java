package com.shopping.example.repository;

import com.shopping.example.entity.Cart;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CartRepository extends JpaRepository<Cart,Long> {

    Cart findByCustomer_Id(Long customerId);

    boolean deleteAllByCartId(Long cartId);

}
