package com.shopping.example;

import com.shopping.example.entity.Account;
import com.shopping.example.entity.Employee;
import com.shopping.example.entity.Role;
import com.shopping.example.repository.AccountRepository;
import com.shopping.example.repository.CustomerRepository;
import com.shopping.example.repository.EmployeeRepository;
import com.shopping.example.repository.RoleRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.shopping.example.utility.Contant.*;

@SpringBootApplication
@EnableJpaAuditing
public class ShoppingApplication {



    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    public static void main(String[] args) {
        SpringApplication.run(ShoppingApplication.class, args);
    }

}
