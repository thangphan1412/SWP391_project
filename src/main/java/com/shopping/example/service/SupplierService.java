package com.shopping.example.service;

import com.shopping.example.entity.Supplier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SupplierService {
    Optional<Supplier> findSupplierById(long id);
    List<Supplier> findAllSuppliers();
    Supplier saveSupplier(Supplier supplier);



}
