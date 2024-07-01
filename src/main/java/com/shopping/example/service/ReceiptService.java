package com.shopping.example.service;

import com.shopping.example.entity.Receipt;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface ReceiptService {

    void addReceipt(Receipt receipt);

    List<Receipt> getReceipts();

}
