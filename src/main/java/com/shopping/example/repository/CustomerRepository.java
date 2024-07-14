package com.shopping.example.repository;

import com.shopping.example.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Struct;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findCustomerByAccount_Email(String email);
    void save(Optional<Customer> customer);


}
