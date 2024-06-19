package com.shopping.example.service;

import com.shopping.example.entity.Order;
import com.shopping.example.entity.OrderDetail;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderDetailService {
    List<OrderDetail> getOrderDetailsByOrder(Order order);

    Optional<OrderDetail> getOrderDetailById(Long orderDetailId);
}
