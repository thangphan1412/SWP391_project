package com.shopping.example.service;

import com.shopping.example.entity.Receipt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface ReceiptService {

    void addReceipt(Receipt receipt);

    List<Receipt> getReceipts();

    Optional<Receipt> getReceipt(Long id);

}
