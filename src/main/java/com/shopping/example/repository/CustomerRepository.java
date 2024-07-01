package com.shopping.example.repository;

import com.shopping.example.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Struct;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findCustomerByAccount_Email(String email);
    void save(Optional<Customer> customer);

    @Query("SELECT c FROM  Customer c WHERE lower(c.name) LIKE %:query% ")
    List<Customer> findCustomerByName(@Param("query") String query);
}
