package com.shopping.example.repository;


import com.shopping.example.entity.Voucher;

import org.springframework.data.jpa.repository.JpaRepository;


public interface VoucherRepository extends JpaRepository<Voucher, Long> {



}
