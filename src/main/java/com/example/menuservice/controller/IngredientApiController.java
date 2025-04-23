package com.example.menuservice.controller;

import com.example.menuservice.dto.*;
import com.example.menuservice.mapper.IngredientMapper;
import com.example.menuservice.repository.*;
import com.example.menuservice.status.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus/ingredients")
public class IngredientApiController {

    private final BreadRepository breadRepository;
    private final MaterialRepository materialRepository;
    private final CheeseRepository cheeseRepository;
    private final VegetableRepository vegetableRepository;
    private final SauceRepository sauceRepository;
    private final IngredientMapper ingredientMapper;

    @GetMapping("/breads")
    public List<BreadResponseDTO> getBread() {
        return breadRepository.findByStatus(BreadStatus.ACTIVE.name())
                .stream()
                .map(ingredientMapper::toBreadDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/materials")
    public List<MaterialResponseDTO> getMaterial() {
        return materialRepository.findByStatus(MaterialStatus.ACTIVE.name())
                .stream()
                .map(ingredientMapper::toMaterialDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/cheeses")
    public List<CheeseResponseDTO> getCheese() {
        return cheeseRepository.findByStatus(CheeseStatus.ACTIVE.name())
                .stream()
                .map(ingredientMapper::toCheeseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/vegetables")
    public List<VegetableResponseDTO> getVegetable() {
        return vegetableRepository.findByStatus(VegetableStatus.ACTIVE.name())
                .stream()
                .map(ingredientMapper::toVegetableDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/sauces")
    public List<SauceResponseDTO> getSauce() {
        return sauceRepository.findByStatus(SauceStatus.ACTIVE.name())
                .stream()
                .map(ingredientMapper::toSauceDTO)
                .collect(Collectors.toList());
    }
}
