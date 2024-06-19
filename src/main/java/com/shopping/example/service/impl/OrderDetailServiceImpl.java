package com.shopping.example.service.impl;



import com.shopping.example.entity.Order;
import com.shopping.example.entity.OrderDetail;
import com.shopping.example.repository.OrderDetailRepository;
import com.shopping.example.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;


    @Override
    public List<OrderDetail> getOrderDetailsByOrder(Order order) {
        return orderDetailRepository.findByOrder_Id(order.getOrderId());
    }

    @Override
    public Optional<OrderDetail> getOrderDetailById(Long orderDetailId) {
        return orderDetailRepository.findById(orderDetailId);
    }


}
