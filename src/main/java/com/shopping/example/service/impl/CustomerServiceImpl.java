package com.shopping.example.service.impl;

import com.shopping.example.DTO.request.RegisterRequest;
import com.shopping.example.entity.Account;
import com.shopping.example.entity.Customer;
import com.shopping.example.repository.AccountRepository;
import com.shopping.example.repository.CustomerRepository;
import com.shopping.example.repository.RoleRepository;
import com.shopping.example.security.MyUserDetails;
import com.shopping.example.service.CustomerService;
import com.shopping.example.utility.Contant;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer register(RegisterRequest registerRequest) {
        // Kiểm tra xem email đã tồn tại trong hệ thống chưa
        if (accountRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("DUPLICATE_EMAIL");
        }

        // Tạo tài khoản mới
        Account account = new Account();
        account.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        account.setRoles(Collections.singletonList(roleRepository.findByName(Contant.ROLE_USER)));
        account.setEmail(registerRequest.getEmail());
        account.setAccountNonLocked(true);
        account.setEnabled(true);

        // Lưu tài khoản vào cơ sở dữ liệu
        accountRepository.save(account);

        // Tạo khách hàng mới
        Customer customer = new Customer();
        customer.setId(new Date().getTime());
        customer.setName(registerRequest.getName());
        customer.setAccount(account);
        //customer.setOrders(Collections.emptyList());

        // Lưu thông tin khách hàng vào cơ sở dữ liệu
        Customer savedCustomer = customerRepository.save(customer);

        // Trả về thông tin khách hàng đã đăng ký thành công
        return customerRepository.save(savedCustomer);
    }

    @Override
    public Optional<Customer> getCustomerByID(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public void save(Optional<Customer> customer) {
         customerRepository.save(customer);
    }

    @Override
    public Customer findCustomerByEmail(String email) {
        return customerRepository.findCustomerByAccount_Email(email);
    }

    @Override
    public Optional<Customer> findCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer saveCus(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> customers = new ArrayList<>();
        try {
            customers = customerRepository.findAll();
        } catch (Exception e){
            System.out.println(e);
        }
        return customers;
    }

    @Override
    public Optional<String> getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof MyUserDetails) {
            return Optional.of(((MyUserDetails) principal).getEmail());
        }
        return Optional.empty();

    }
}
