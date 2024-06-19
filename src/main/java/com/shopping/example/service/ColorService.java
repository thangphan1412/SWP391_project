package com.shopping.example.service;

import com.shopping.example.entity.Color;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ColorService {

    Optional<Color> getColorById(Long colorId);

    List<Color> getAllColors();

    Color saveColor(Color color);




}
