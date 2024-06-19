package com.shopping.example.service.impl;

import com.shopping.example.entity.Color;
import com.shopping.example.repository.ColorRepository;
import com.shopping.example.service.ColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ColorServiceImpl implements ColorService {
    @Autowired
    private ColorRepository colorRepository;

    @Override
    public Optional<Color> getColorById(Long colorId) {
        return colorRepository.findById(colorId);
    }

    @Override
    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    @Override
    public Color saveColor(Color color) {
        return colorRepository.save(color);
    }
}
