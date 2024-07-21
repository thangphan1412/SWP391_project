package com.shopping.example.service.impl;

import com.shopping.example.entity.Role;
import com.shopping.example.repository.RoleRepository;
import com.shopping.example.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
