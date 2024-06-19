package com.shopping.example.repository;

import com.shopping.example.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color,Long> {
}
