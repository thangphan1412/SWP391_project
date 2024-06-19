package com.shopping.example.service;


import com.shopping.example.entity.Voucher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface VoucherService {

    Voucher save(Voucher voucher);

    List<Voucher> findAll();


    void deleteById(Long id);

    Optional<Voucher> findById(Long id);




}
