package com.shopping.example.service;


import com.shopping.example.entity.Customer;
import com.shopping.example.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {


    List<Order> getAllOrders();

    List<Order> getOrdersByCustomer(Customer currentCustomer);

    Order getOrderById(Long id);

    List<Order> getOrdersByStatus(String status);


    List<Order> findByCustomerIdAndOrderStatus(Long userId, String status);


    List<Order> findByCustomerEmail(String email);

    Order save(Order order);
}
