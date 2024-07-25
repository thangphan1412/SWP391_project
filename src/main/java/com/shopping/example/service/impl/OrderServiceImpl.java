package com.shopping.example.service.impl;

import com.shopping.example.entity.Account;
import com.shopping.example.entity.Customer;
import com.shopping.example.entity.Order;
import com.shopping.example.repository.CustomerRepository;
import com.shopping.example.repository.OrderRepository;
import com.shopping.example.service.AccountService;
import com.shopping.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderServiceImpl implements OrderService {


    @Autowired
    private AccountService accountService;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll() ;
    }

    @Override
    public List<Order> getOrdersByCustomer(Customer currentCustomer) {
        return orderRepository.findByCustomerId(currentCustomer.getId());
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findByOrderId(id);
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByOrderStatus(status);
    }

    @Override
    public List<Order> findByCustomerIdAndOrderStatus(Long userId, String status) {
        return orderRepository.findByCustomerIdAndOrderStatus(userId, status);
    }

    @Override
    public List<Order> findByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }


    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrdersNotShip() {
        Account currentAccount = accountService.getCurrentAccount();
        if (currentAccount == null) {
            throw new IllegalArgumentException("User not found");
        }
        return orderRepository.getAllOrdersNotShipped(currentAccount.getShipper().getId());
    }

    @Override
    public List<Order> getAllOrdersRequestCancel() {
        return orderRepository.findAllByOrderStatusOnRequest();
    }

    @Override
    public void updateOrderStatus(Long orderId, String status) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setOrderStatus(status);
            order.setEmployee(accountService.getCurrentAccount().getEmployee());
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
    }

    @Override
    public void updatePaymentStatus(Long orderId, String paymentStatus) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setPaymentStatus(paymentStatus);
            orderRepository.save(order);
        } else {
            throw new RuntimeException("Order not found with id: " + orderId);
        }
    }




}
