package com.shopping.example.service;

import com.shopping.example.entity.ReceiptDetail;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public interface ReceiptDetailService {

    Optional<ReceiptDetail> getReceiptDetail(Long id);
}
