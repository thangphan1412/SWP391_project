package com.shopping.example.service.impl;

import com.shopping.example.DTO.request.RegisterRequest;
import com.shopping.example.entity.Account;
import com.shopping.example.entity.Employee;
import com.shopping.example.repository.AccountRepository;
import com.shopping.example.repository.EmployeeRepository;
import com.shopping.example.repository.RoleRepository;
import com.shopping.example.service.IEmployeeService;
import com.shopping.example.utility.Contant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
@Slf4j
public class EmployeeService implements IEmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public Employee register(RegisterRequest registerRequest) {
        if (accountRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("DUPLICATE_EMAIL");
        }

        // Tạo tài khoản mới
        Account account = new Account();
        account.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        account.setRoles(Collections.singletonList(roleRepository.findByName(Contant.ROLE_EMPLOYEE)));
        account.setEmail(registerRequest.getEmail());
        account.setAccountNonLocked(true);
        account.setEnabled(true);

        // Lưu tài khoản vào cơ sở dữ liệu
        accountRepository.save(account);

        // Tạo khách hàng mới
        Employee employee = new Employee();
        employee.setId(new Date().getTime());
        employee.setName(registerRequest.getName());
        employee.setAccount(account);
        //customer.setOrders(Collections.emptyList());

        // Lưu thông tin khách hàng vào cơ sở dữ liệu
        return employeeRepository.save(employee);
    }


}
