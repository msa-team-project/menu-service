package com.example.menuservice.controller;


import com.example.menuservice.domain.*;
import com.example.menuservice.repository.*;
import com.example.menuservice.status.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus/ingredients")
public class IngredientApiController {

    private final BreadRepository breadRepository;
    private final MaterialRepository materialRepository;
    private final CheeseRepository cheeseRepository;
    private final VegetableRepository vegetableRepository;
    private final SauceRepository sauceRepository;


        @GetMapping("/breads")
        public List<Bread> getBreads() {
            return breadRepository.findByStatus(BreadStatus.ACTIVE.name());
        }

        @GetMapping("/materials")
        public List<Material> getMaterials() {
            return materialRepository.findByStatus(MaterialStatus.ACTIVE.name());
        }

        @GetMapping("/cheeses")
        public List<Cheese> getCheeses() {
            return cheeseRepository.findByStatus(CheeseStatus.ACTIVE.name());
        }

        @GetMapping("/vegetables")
        public List<Vegetable> getVegetables() {
            return vegetableRepository.findByStatus(VegetableStatus.ACTIVE.name());
        }

        @GetMapping("/sauces")
        public List<Sauce> getSauces() {
            return sauceRepository.findByStatus(SauceStatus.ACTIVE.name());
        }
    }


