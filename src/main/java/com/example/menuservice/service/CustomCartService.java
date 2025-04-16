package com.example.menuservice.service;

import com.example.menuservice.domain.*;
import com.example.menuservice.dto.CustomCartRequestDTO;
import com.example.menuservice.dto.CustomCartResponseDTO;
import com.example.menuservice.exception.CustomCartNotFoundException;
import com.example.menuservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomCartService {

    private final CustomCartRepository customCartRepository;

    private final BreadRepository breadRepository;
    private final MaterialRepository materialRepository;
    private final CheeseRepository cheeseRepository;
    private final VegetableRepository vegetableRepository;
    private final SauceRepository sauceRepository;

    // === CRUD ===
    public CustomCartResponseDTO viewCustomCart(Long uid) {
        CustomCart customCart = customCartRepository.findById(uid)
                .orElseThrow(() -> new CustomCartNotFoundException("ID: " + uid));
        return CustomCartResponseDTO.fromEntity(customCart);
    }

    @Transactional
    public CustomCartResponseDTO addCustomCart(CustomCartRequestDTO dto, String sessionId) {
        // 필수 항목이 null이 아닌지 확인
        if (dto.getBread() == null || dto.getMaterial1() == null) {
            throw new IllegalArgumentException("Bread or Material1 ID must not be null");
        }
        System.out.println(dto.getBread());

        CustomCart customCart = CustomCart.builder()
                .bread(getBread(dto.getBread()))
                .material1(getMaterial(dto.getMaterial1()))
                .material2(getOptionalMaterial(dto.getMaterial2()))
                .material3(getOptionalMaterial(dto.getMaterial3()))
                .cheese(getOptionalCheese(dto.getCheese()))
                .vegetable1(getVegetable(dto.getVegetable1()))
                .vegetable2(getOptionalVegetable(dto.getVegetable2()))
                .vegetable3(getOptionalVegetable(dto.getVegetable3()))
                .vegetable4(getOptionalVegetable(dto.getVegetable4()))
                .vegetable5(getOptionalVegetable(dto.getVegetable5()))
                .vegetable6(getOptionalVegetable(dto.getVegetable6()))
                .vegetable7(getOptionalVegetable(dto.getVegetable7()))
                .vegetable8(getOptionalVegetable(dto.getVegetable8()))
                .sauce1(getSauce(dto.getSauce1()))
                .sauce2(getOptionalSauce(dto.getSauce2()))
                .sauce3(getOptionalSauce(dto.getSauce3()))
                .build();

        return CustomCartResponseDTO.fromEntity(customCartRepository.save(customCart));
    }


    // === Entity fetch helpers ===
    private Bread getBread(Long id) {
        return breadRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Bread not found: " + id));
    }

    private Material getMaterial(Long id) {
        return materialRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Material not found: " + id));
    }

    private Material getOptionalMaterial(Long id) {
        return id != null ? getMaterial(id) : null;
    }

    private Cheese getOptionalCheese(Long id) {
        return id != null ? cheeseRepository.findById(id).orElse(null) : null;
    }

    private Vegetable getVegetable(Long id) {
        return vegetableRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Vegetable not found: " + id));
    }

    private Vegetable getOptionalVegetable(Long id) {
        return id != null ? getVegetable(id) : null;
    }

    private Sauce getSauce(Long id) {
        return sauceRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Sauce not found: " + id));
    }

    private Sauce getOptionalSauce(Long id) {
        return id != null ? getSauce(id) : null;
    }
}
