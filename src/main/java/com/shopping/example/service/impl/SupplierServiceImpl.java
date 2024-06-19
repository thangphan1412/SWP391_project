package com.shopping.example.service.impl;

import com.shopping.example.entity.Supplier;
import com.shopping.example.repository.SupplierRepository;
import com.shopping.example.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public Optional<Supplier> findSupplierById(long id) {
        return  supplierRepository.findById(id);
    }

    @Override
    public List<Supplier> findAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public Supplier saveSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }
}
