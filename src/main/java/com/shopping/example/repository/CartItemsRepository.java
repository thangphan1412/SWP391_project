package com.shopping.example.repository;


import com.shopping.example.entity.CartItems;
import com.shopping.example.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface CartItemsRepository extends JpaRepository<CartItems,Long> {



    List<CartItems> findCartItemsByCart_CartId(Long cartId);


    @Modifying
    @Query("DELETE FROM CartItems ci WHERE ci.cartItemsId = :cartId")
    void deleteByCartId(@Param("cartId") Long cartId);

    CartItems findByProductType(ProductType productType);
    @Modifying
    @Query("DELETE FROM CartItems ci WHERE ci.cart.cartId = :cartId")
    void deleteByCart(@Param("cartId") Long cartId);

}
