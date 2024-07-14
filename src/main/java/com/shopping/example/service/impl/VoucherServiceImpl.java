package com.shopping.example.service.impl;

import com.shopping.example.entity.Voucher;
import com.shopping.example.repository.VoucherRepository;
import com.shopping.example.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class VoucherServiceImpl implements VoucherService {

   @Autowired
   private VoucherRepository voucherRepository;


    @Override
    public Voucher save(Voucher voucher) {
        return voucherRepository.save(voucher);
    }

    @Override
    public List<Voucher> findAll() {
        return voucherRepository.findAll();
    }





    @Override
    public void deleteById(Long id) {
        voucherRepository.deleteById(id);
    }

    @Override
    public Optional<Voucher> findById(Long id) {
        return voucherRepository.findById(id);
    }


    @Override
    public double VoucherDiscount(String voucherCode) {
        Double discount = voucherRepository.findPercentageDiscountByVoucherCode(voucherCode);
        if (discount == null) {
            return 0.0;
        }
        return discount;
    }

    @Override
    public Voucher findVoucherWithCoce(String voucherCode) {
        return voucherRepository.findByVoucherCodeContainingIgnoreCase(voucherCode);
    }


}
