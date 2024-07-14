package com.shopping.example.repository;

import com.shopping.example.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    Order findByOrderId(Long orderId);

    List<Order> findByOrderStatus(String status);

    @Query("SELECT o FROM Order o WHERE o.customer.id = :userId AND o.orderStatus = :status")
    List<Order> findByCustomerIdAndOrderStatus(Long userId, String status);

    @Query("SELECT o FROM Order o WHERE o.customer.account.email = :email")
    List<Order> findByCustomerEmail(String email);

    @Query("SELECT o FROM Order o WHERE o.orderStatus = 'Pending' OR o.employee.id = :id")
    List<Order> getAllOrdersNotShipped(@Param("id") Long id);

}
