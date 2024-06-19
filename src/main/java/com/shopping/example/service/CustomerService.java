package com.shopping.example.service;

import com.shopping.example.DTO.request.RegisterRequest;
import com.shopping.example.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CustomerService {
    Customer register (RegisterRequest registerRequest);

    Optional<Customer> getCustomerByID(Long id);

    void save (Optional<Customer> customer);


     Customer findCustomerByEmail(String email);
     Optional<Customer> findCustomerById(Long id);

     Customer saveCus(Customer customer);


     List<Customer> getAll();

    public Optional<String> getCurrentUserEmail();

}
