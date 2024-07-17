package com.shopping.example.service.impl;

import com.shopping.example.DTO.request.RegisterRequest;
import com.shopping.example.entity.Account;
import com.shopping.example.entity.Customer;
import com.shopping.example.entity.Shipper;
import com.shopping.example.repository.AccountRepository;
import com.shopping.example.repository.RoleRepository;
import com.shopping.example.repository.ShipperRepository;
import com.shopping.example.service.IShipperService;
import com.shopping.example.utility.Contant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ShipperService implements IShipperService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ShipperRepository shipperRepository;

    @Override
    public Shipper register(RegisterRequest registerRequest) {
        if (accountRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("DUPLICATE_EMAIL");
        }

        // Tạo tài khoản mới
        Account account = new Account();
        account.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        account.setRoles(Collections.singletonList(roleRepository.findByName(Contant.ROLE_SHIPPER)));
        account.setEmail(registerRequest.getEmail());
        account.setAccountNonLocked(true);
        account.setEnabled(true);

        // Lưu tài khoản vào cơ sở dữ liệu
        accountRepository.save(account);

        // Tạo khách hàng mới
        Shipper shipper = new Shipper();
        shipper.setId(new Date().getTime());
        shipper.setName(registerRequest.getName());
        shipper.setAccount(account);
        //customer.setOrders(Collections.emptyList());

        // Lưu thông tin khách hàng vào cơ sở dữ liệu
        return shipperRepository.save(shipper);
    }


    public List<Shipper> getAll() {
        List<Shipper> shippers = new ArrayList<>();
        try {
            shippers = shipperRepository.findAll();
        } catch (Exception e){
            System.out.println(e);
        }
        return shippers;
    }

//    public List<Shipper> getShipperByName(String name) {
//        return shipperRepository.findShipperByName(name.toLowerCase());
//    }
}
